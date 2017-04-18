package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.order.controller.dto.ExpressDto;
import com.jifenke.lepluslive.order.controller.dto.OnLineOrderDto;
import com.jifenke.lepluslive.order.domain.entities.ExpressInfo;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.ExpressInfoService;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.controller.dto.OrderDto;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by wcg on 16/4/1.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

  @Inject
  private OrderService orderService;

  @Inject
  private AddressService addressService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private ExpressInfoService expressInfoService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private OnlineOrderService onlineOrderService;

  @Inject
  private MessageService messageService;

  @ApiOperation("获取用户的订单")
  @RequestMapping(value = "/orderList", method = RequestMethod.POST)
  public LejiaResult orderList(@RequestParam(required = true) String token,
                               @ApiParam(value = "订单状态(0=待付款|1=待发货|2=待收货|3=已完成|5=全部)")
                               @RequestParam(required = true) Integer status) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(2002, messageService.getMsg("2002"));
    }
    List<OnLineOrder>
        onLineOrders =
        orderService.getCurrentUserOrders(leJiaUser, status);
    return LejiaResult.build(200, "ok", onLineOrders);
  }

  //todo:老版本，APP积分商品 以前版本人数非常少后可删除
  @ApiOperation("购物车生成订单信息")
  @RequestMapping(value = "/createCartOrder", method = RequestMethod.POST)
  public LejiaResult createCartOrder(
      @ApiParam(value = "用户身份标识token") @RequestParam(required = true) String token,
      @ApiParam(value = "商品id_商品规格id_数量,") @RequestParam(required = false) String cartDetailDtos) {

    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(2002, messageService.getMsg("2002"));
    }
    Long count = orderService.getCurrentUserObligationOrdersCount(leJiaUser);
    if (count >= 4) {
      return LejiaResult.build(5001, messageService.getMsg("5001"));
    }
    List<CartDetailDto> cartDetailDtoList = stringToList(cartDetailDtos);
    Address address = addressService.findAddressByLeJiaUserAndState(leJiaUser);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
    //免运费最低价格
    Integer
        FREIGHT_FREE_PRICE =
        Integer.parseInt(dictionaryService.findDictionaryById(1L).getValue());
    Map<String, Object>
        result =
        orderService.createCartOrder(cartDetailDtoList, leJiaUser, address, 1L);
    String status = "" + result.get("status");
    if (!"200".equals(status)) {
      if (result.get("arrays") != null) {
        return LejiaResult.build(Integer.valueOf(status),
                                 messageService.getMsg(status, (String[]) result.get("arrays")));
      }
      return LejiaResult.build(Integer.valueOf(status), messageService.getMsg(status));
    }
    OnLineOrderDto orderDto = new OnLineOrderDto();
    OnLineOrder onLineOrder = (OnLineOrder) result.get("data");
    try {
      BeanUtils.copyProperties(orderDto, onLineOrder);
      orderDto.setMinPrice(FREIGHT_FREE_PRICE);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    orderDto.setScoreB(scoreB.getScore());
    return LejiaResult.build(200, "ok", orderDto);
  }

  @ApiOperation("订单列表去支付按钮")
  @RequestMapping(value = "/orderDetail", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult orderDetail(
      @ApiParam(value = "用户身份标识token") @RequestParam(required = true) String token,
      @ApiParam(value = "订单id") @RequestParam(required = true) Long orderId) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(2002, messageService.getMsg("2002"));
    }

    OnLineOrder onLineOrder = orderService.findOnLineOrderById(orderId);
    OnLineOrderDto orderDto = new OnLineOrderDto();
    try {
      BeanUtils.copyProperties(orderDto, onLineOrder);
      orderDto.setMinPrice(Integer.valueOf(dictionaryService.findDictionaryById(1L).getValue()));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    if (onLineOrder.getType() == 1) {
      orderDto.setScoreB(scoreBService.findScoreBByWeiXinUser(leJiaUser).getScore());
    } else {
      orderDto.setScoreB(scoreCService.findScoreCByLeJiaUser(leJiaUser).getScore());
    }

    return LejiaResult.build(200, "ok", orderDto);
  }

  @ApiOperation("商品详情页立即购买")
  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  @ResponseBody
  public LejiaResult orderCreate(
      @ApiParam(value = "用户身份标识token") @RequestParam(required = true) String token,
      @RequestParam(required = true) Long productId,
      @RequestParam(required = true) Integer productNum,
      @RequestParam(required = true) Long productSpecId) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(2002, messageService.getMsg("2002"));
    }
    OrderDto orderDto = new OrderDto();
    orderDto.setProductId(productId);
    orderDto.setProductNum(productNum);
    orderDto.setProductSpec(productSpecId);
    Address address = addressService.findAddressByLeJiaUserAndState(leJiaUser);
    //免运费最低价格
    Integer
        FREIGHT_FREE_PRICE =
        Integer.parseInt(dictionaryService.findDictionaryById(1L).getValue());
    //创建商品的待支付订单
    OnLineOrder onLineOrder = null;
    try {
      Map
          result =
          orderService.createBuyOrder(productId, productSpecId, productNum, leJiaUser, address, 1L);
      String status = "" + result.get("status");
      if (!"200".equals(status)) {
        return LejiaResult.build(Integer.valueOf(status), messageService.getMsg(status));
      }
      onLineOrder = (OnLineOrder) result.get("data");
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "服务器异常");
    }
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
    OnLineOrderDto onLineOrderDto = new OnLineOrderDto();
    try {
      BeanUtils.copyProperties(onLineOrderDto, onLineOrder);
      onLineOrderDto.setMinPrice(FREIGHT_FREE_PRICE);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    onLineOrderDto.setScoreB(scoreB.getScore());
    return LejiaResult.build(200, "ok", onLineOrderDto);
  }

  @ApiOperation("APP微信支付接口")
  @RequestMapping(value = "/weixinpay", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult weixinPay(@RequestParam Long orderId, @RequestParam Long truePrice,
                        @RequestParam Long trueScore, HttpServletRequest request) {
    if (orderId == null || truePrice == null || trueScore == null) {
      return LejiaResult.build(5010, messageService.getMsg("5010"));
    }
    Map<String, Object>
        result =
        orderService.setPriceScoreForOrder(orderId, truePrice, trueScore, 2);
    String status = "" + result.get("status");
    if (!"200".equals(status)) {
      return LejiaResult.build(Integer.valueOf(status), messageService.getMsg(status));
    }

    //封装订单参数
    OnLineOrder order = (OnLineOrder) result.get("data");
    SortedMap<String, Object>
        map =
        weiXinPayService
            .buildAPPOrderParams(request, "乐加商城消费", order.getOrderSid(), "" + order.getTruePrice(),
                                 Constants.ONLINEORDER_NOTIFY_URL);
    //获取预支付id
    Map<String, Object> unifiedOrder = weiXinPayService.createUnifiedOrder(map);
    if (unifiedOrder.get("prepay_id") != null) {
      SortedMap<String, Object> sortedMap = weiXinPayService.buildAppParams(
          unifiedOrder.get("prepay_id").toString());
      return LejiaResult.build(200, "ok", sortedMap);
    } else {
      return LejiaResult
          .build(4001, messageService.getMsg("4001") + "," + String
              .valueOf(unifiedOrder.get("err_code_des")));
    }
  }


  @ApiOperation("全积分支付接口")
  @RequestMapping(value = "/payByScore", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult payByScore(@RequestParam Long orderId, @RequestParam Long trueScore) {
    if (orderId == null || trueScore == null) {
      return LejiaResult.build(5010, messageService.getMsg("5010"));
    }
    try {
      Map map = onlineOrderService.orderPayByScoreB(orderId, trueScore, 2, 9L);
      String status = "" + map.get("status");
      if (!"200".equals(status)) {
        return LejiaResult.build(Integer.valueOf(status), messageService.getMsg(status));
      }
      return LejiaResult.build((Integer) map.get("status"), "", map.get("data"));
    } catch (Exception e) {
      return LejiaResult.build(500, messageService.getMsg("500"));
    }
  }

  @ApiOperation("支付成功后请求数据")
  @RequestMapping(value = "/paySuccess", method = RequestMethod.POST)
  @ResponseBody
  public LejiaResult paySuccess(@RequestParam Long orderId) {
//    //查询是否掉单
//    orderService.orderStatusQuery(orderId, 2);
    OnLineOrder order = orderService.findOnLineOrderById(orderId);
    HashMap<String, Object> map = new HashMap<>();
    if (order != null) {
      map.put("sid", order.getOrderSid());
      map.put("truePrice", order.getTruePrice());
      map.put("trueScore", order.getTrueScore());
//      //为了防止微信处理失败或者慢导致未找到信息，使用计算数据
//      Integer
//          PAY_BACK_SCALE =
//          Integer.parseInt(dictionaryService.findDictionaryById(3L).getValue());
      map.put("payBackScore", order.getPayBackA());
      return LejiaResult.build(200, "ok", map);
    }
    return LejiaResult.build(5006, messageService.getMsg("5006"));
  }

  @ApiOperation("修改订单的收货地址")
  @RequestMapping(value = "/editOrderAddr", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult editAddress(@RequestParam(required = true) Long orderId,
                          @RequestParam(required = true) Long addrId) {
    OnLineOrder onLineOrder = orderService.findOnLineOrderById(orderId);
    Address address = addressService.findOneAddress(addrId);
    if (onLineOrder != null && address != null) {
      addressService.editOrderAddress(address, onLineOrder);
      return LejiaResult.build(200, "ok");
    } else {
      if (onLineOrder == null) {
        return LejiaResult.build(5006, messageService.getMsg("5006"));
      } else {
        return LejiaResult.build(7001, messageService.getMsg("7001"));
      }
    }
  }

  @ApiOperation("取消订单")
  @RequestMapping(value = "/orderCancle", method = RequestMethod.POST)
  public LejiaResult orderCancle(@RequestParam(required = true) Long orderId) {
    orderService.orderCancle(orderId);
    return LejiaResult.ok();
  }

  @ApiOperation("确认收货")
  @RequestMapping(value = "/orderConfirm", method = RequestMethod.POST)
  public LejiaResult orderConfim(@RequestParam(required = true) Long orderId) {
    orderService.confrimOrder(orderId);
    return LejiaResult.ok();
  }

  /**
   * 查看物流信息
   */
  @RequestMapping(value = "/showExpress/{id}", method = RequestMethod.GET)
  public ModelAndView showExpress(@PathVariable Long id, Model model) {

    OnLineOrder order = orderService.findOnLineOrderById(id);

    if (order.getExpressNumber() == null || "".equals(order.getExpressNumber())) {
      return MvUtil.go("/weixin/expressDetail");
    }
    //调接口获取物流信息，存入数据库
    ExpressInfo expressInfo = expressInfoService.findExpressAndSave(order);
    if (expressInfo == null) {
      return MvUtil.go("/weixin/expressDetail");
    }

    List<ExpressDto>
        expressDtoList =
        JsonUtils.jsonToList(expressInfo.getContent(), ExpressDto.class);
    model.addAttribute("expressList", expressDtoList);

    model.addAttribute("expressCompany", order.getExpressCompany());
    model.addAttribute("expressNumber", order.getExpressNumber());

    return MvUtil.go("/weixin/expressDetail");
  }

  /**
   * 查看订单详情信息
   */
  @RequestMapping(value = "/showOrderInfo/{id}", method = RequestMethod.GET)
  public ModelAndView showOrderInfo(@PathVariable Long id, Model model) {

    OnLineOrder order = orderService.findOnLineOrderById(id);

    model.addAttribute("order", order);

    model.addAttribute("address", order.getAddress());
    model.addAttribute("backA", dictionaryService.findDictionaryById(3L).getValue());

    return MvUtil.go("/order/orderDetail");
  }

  //todo:老版本，APP积分商品 以前版本人数非常少后可删除
  private List<CartDetailDto> stringToList(String cartDetails) {
    try {
      cartDetails = URLDecoder.decode(cartDetails, "utf8");
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<CartDetailDto> detailDtoList = new ArrayList<>();
    String[] details = cartDetails.split(",");
    for (String detail : details) {
      String[] s = detail.split("_");
      CartDetailDto cartDetailDto = new CartDetailDto();
      Product product = new Product();
      ProductSpec productSpec = new ProductSpec();
      product.setId(Long.parseLong(s[0]));
      productSpec.setId(Long.parseLong(s[1]));
      cartDetailDto.setProduct(product);
      cartDetailDto.setProductSpec(productSpec);
      cartDetailDto.setProductNumber(Integer.parseInt(s[2]));
      detailDtoList.add(cartDetailDto);
    }
    return detailDtoList;
  }
}
