package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.config.AppConstants;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.order.controller.dto.ExpressDto;
import com.jifenke.lepluslive.order.controller.dto.OnLineOrderDto;
import com.jifenke.lepluslive.order.domain.entities.ExpressInfo;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.ExpressInfoService;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.apache.commons.beanutils.BeanUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 臻品商城订单
 * Created by zhangwen on 16/4/1.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

  private static Logger log = LoggerFactory.getLogger(OrderController.class);

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

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ProductService productService;

  /**
   * 臻品创建订单（包括商品详情页下单和购物车下单） APP&WEB  2017/6/27
   *
   * @param source 公众号||APP
   * @param type   下单页面 1=详情页|2=购物车
   * @param carts  示例：商品id_商品规格id_数量,商品id_商品规格id_数量
   */
  @RequestMapping(value = "/sign/create", method = RequestMethod.POST)
  public LejiaResult createCartOrder(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam String source,
                                     @RequestParam Integer type,
                                     @RequestParam String carts) {

    LeJiaUser
        leJiaUser =
        leJiaUserService.findUserById(Long.valueOf("" + request.getAttribute("leJiaUserId")));
    Long count = orderService.getCurrentUserObligationOrdersCount(leJiaUser);
    if (count >= 4) {
      return LejiaResult.build(5001, "未支付订单过多,请支付后再下单");
    }
    Map<String, Object>
        result = orderService.createOrder(carts, leJiaUser, source);
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);
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
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    if ("WEB".equals(source) && type == 2) {
      //购物车cookie
      List<CartDetailDto> list = orderService.stringToList(carts);
      String cardCookie = CookieUtils.getCookieValue(request, "leJiaUnionId") + "-cart";
      String cart = CookieUtils.getCookieValue(request, cardCookie);
      List<CartDetailDto> cartDetailDtoOrigin = null;
      if (cart != null) {
        cartDetailDtoOrigin = JsonUtils.jsonToList(cart, CartDetailDto.class);
        if (cartDetailDtoOrigin != null) {
          cartDetailDtoOrigin.removeAll(list);
        }
      }
      CookieUtils
          .setCookie(request, response, cardCookie,
                     JsonUtils.objectToJson(cartDetailDtoOrigin),
                     Constants.COOKIE_DISABLE_TIME);
    }
    orderDto.setScoreB(scoreC.getScore());
    return LejiaResult.build(200, "ok", orderDto);
  }

  /**
   * 臻品订单提交 填充实际使用金币&微信支付接口  APP&WEB  2017/6/27
   *
   * @param orderId     订单号
   * @param trueScore   实际使用金币
   * @param source      APP||WEB
   * @param transmitWay 线下自提=1
   */
  @RequestMapping(value = "/sign/submit", method = RequestMethod.POST)
  public LejiaResult submit(@RequestParam Long orderId,
                            @RequestParam String trueScore,
                            @RequestParam String source,
                            @RequestParam Integer transmitWay,
                            HttpServletRequest request) {
    if (trueScore == null || trueScore.equals("")) {
      trueScore = "0";
    }
    long payWay = 1; //payWay 5=公众号|1=APP
    if ("WEB".equals(source)) {
      payWay = 5;
    }
    Long newTrueScore = new BigDecimal(trueScore).multiply(new BigDecimal(100)).longValue();

    try {
      Map<String, Object>
          result =
          orderService.submitOrder(orderId, payWay, newTrueScore, transmitWay);

      String status = result.get("status").toString();
      if (!"200".equals(status)) {
        return LejiaResult
            .build((Integer) result.get("status"),
                   messageService.getMsg("" + result.get("status")));
      }
      OnLineOrder order = (OnLineOrder) result.get("data");

      SortedMap<String, Object> params = weiXinPayService
          .returnPayParams(payWay, request, "臻品商城消费", order.getOrderSid(),
                           "" + order.getTruePrice(),
                           AppConstants.ONLINEORDER_NOTIFY_URL);
      if (params != null) {
        return LejiaResult.ok(params);
      }
      return LejiaResult.build(500, "出现未知错误,请尝试重新下单");
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "save error");
    }
  }

  /**
   * 微信回调函数
   */
  @RequestMapping(value = "/afterPay", produces = MediaType.APPLICATION_XML_VALUE)
  public void afterPay(HttpServletRequest request, HttpServletResponse response)
      throws IOException, JDOMException {
    InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), "utf-8");
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    String str = null;
    StringBuilder buffer = new StringBuilder();
    while ((str = bufferedReader.readLine()) != null) {
      buffer.append(str);
    }
    Map<String, Object> map = WeixinPayUtil.doXMLParse(buffer.toString());
    if (map != null) {
      System.out.println("参数为========" + map.toString());
      //验签
      String
          sign =
          weiXinPayService.createSign(map, String.valueOf(map.get("trade_type")));
      if (map.get("sign") != null && String.valueOf(map.get("sign")).equals(sign)) {
        String returnCode = (String) map.get("return_code");
        String resultCode = (String) map.get("result_code");
        //操作订单
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
          try {
            orderService.paySuccess(map);
          } catch (Exception e) {
            log.error(e.getMessage());
            response.setContentType("application/xml");
            response.getWriter().write("<xml><return_code>FAIL</return_code></xml>");
            return;
          }
          //返回微信的信息
          response.setContentType("application/xml");
          response.getWriter().write("<xml><return_code>SUCCESS</return_code></xml>");
        }
      }
    }
  }

  /**
   * 支付成功  2017/6/27
   *
   * @param orderId 订单号
   */
  @RequestMapping(value = "/paySuccess/{orderId}")
  public ModelAndView goPaySuccessPage(@PathVariable Long orderId, Model model,
                                       HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    OnLineOrder order = orderService.findOnLineOrderById(orderId);
    if (order != null) {
      if (order.getType() == 1) {
        model.addAttribute("totalScore",
                           scoreAService.findScoreAByLeJiaUser(leJiaUser)
                               .getTotalScore());
        model.addAttribute("payBackScore", order.getPayBackA());
        model.addAttribute("truePrice", order.getTruePrice());
        //商品分类
        List<ProductType> typeList = productService.findAllProductType();
        model.addAttribute("scoreC", scoreCService.findScoreCByLeJiaUser(leJiaUser));
        model.addAttribute("typeList", typeList);
        return MvUtil.go("/product/productIndex");
      } else {
        model.addAttribute("order", order);
        return MvUtil.go("/gold/order/success");
      }
    }
    return null;
  }

  @ApiOperation("获取用户的订单")
  @RequestMapping(value = "/orderList", method = RequestMethod.POST)
  public LejiaResult orderList(@RequestParam String token,
                               @ApiParam(value = "订单状态(0=待付款|1=待发货|2=待收货|3=已完成|5=全部)")
                               @RequestParam Integer status) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(2002, messageService.getMsg("2002"));
    }
    List<OnLineOrder>
        onLineOrders =
        orderService.getCurrentUserOrders(leJiaUser, status);
    return LejiaResult.build(200, "ok", onLineOrders);
  }

  @ApiOperation("订单列表去支付按钮")
  @RequestMapping(value = "/orderDetail", method = RequestMethod.POST)
  public LejiaResult orderDetail(
      @ApiParam(value = "用户身份标识token") @RequestParam String token,
      @ApiParam(value = "订单id") @RequestParam Long orderId) {
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

  @ApiOperation("支付成功后请求数据")
  @RequestMapping(value = "/paySuccess", method = RequestMethod.POST)
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
  public LejiaResult editAddress(@RequestParam(required = true) Long orderId,
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
}
