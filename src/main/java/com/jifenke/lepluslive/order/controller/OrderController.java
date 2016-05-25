package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
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
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
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
  private ScoreAService scoreAService;

  @Inject
  private ExpressInfoService expressInfoService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @ApiOperation("获取用户所有的订单")
  @RequestMapping(value = "/orderList", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult orderList(@RequestParam(required = false) String token) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(206, "未找到用户");
    }
    List<OnLineOrder>
        onLineOrders =
        orderService.getCurrentUserOrders(leJiaUser);
    return LejiaResult.build(200, "ok", onLineOrders);
  }

  @ApiOperation("购物车生成订单信息")
  @RequestMapping(value = "/createCartOrder", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult createCartOrder(
      @ApiParam(value = "用户身份标识token") @RequestParam(required = false) String token,
      HttpServletResponse response,
      @ApiParam(value = "商品id,商品规格id和数量") @RequestBody(required = false) List<CartDetailDto> cartDetailDtos) {
    if (token == null) {
      return LejiaResult.build(207, "请先登录");
    }
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(206, "未找到用户");
    }
    Long count = orderService.getCurrentUserObligationOrdersCount(leJiaUser);
    if (count >= 4) {
      return LejiaResult.build(401, "未支付订单过多,请支付后再下单");
    }
    Address address = addressService.findAddressByLeJiaUserAndState(leJiaUser);
    WeiXinUser weiXinUser = new WeiXinUser();
    weiXinUser.setLeJiaUser(leJiaUser);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(weiXinUser.getLeJiaUser());
    OnLineOrder onLineOrder = orderService.createCartOrder(cartDetailDtos, weiXinUser, address);
    OnLineOrderDto orderDto = new OnLineOrderDto();
    try {
      BeanUtils.copyProperties(orderDto, onLineOrder);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    orderDto.setScoreB(scoreB.getScore());

    return LejiaResult.build(200, "ok", orderDto);
  }

  @ApiOperation("APP微信支付接口")
  @RequestMapping(value = "/weixinpay", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult weixinPay(@RequestParam Long orderId, @RequestParam Long truePrice,
                        @RequestParam Long trueScore, HttpServletRequest request) {
    OnLineOrder onLineOrder = orderService.setPriceScoreForOrder(orderId, truePrice, trueScore);
    if (onLineOrder == null) {
      return LejiaResult.build(403, "库存不足");
    }

    //封装订单参数
    SortedMap<Object, Object> map = weiXinPayService._buildOrderParams(request, onLineOrder);
    //获取预支付id
    Map unifiedOrder = weiXinPayService.createUnifiedOrder(map);
    if (unifiedOrder.get("prepay_id") != null) {
      SortedMap sortedMap = weiXinPayService.buildAppParams(
          unifiedOrder.get("prepay_id").toString());
      return LejiaResult.build(200, "ok", sortedMap);
    } else {
      return LejiaResult.build(404, "支付异常");
    }
  }

  @ApiOperation("支付成功后请求数据")
  @RequestMapping(value = "/paySuccess", method = RequestMethod.POST)
  @ResponseBody
  public LejiaResult paySuccess(@RequestParam Long orderId) {

    OnLineOrder order = orderService.findOnLineOrderById(orderId);

    if (order != null) {
      ScoreADetail aDetail = scoreAService.findScoreADetailByOrderSid(order.getOrderSid());
      if (aDetail != null) {
        ScoreA scoreA = aDetail.getScoreA();
        if (scoreA != null) {
          HashMap<String, Object> map = new HashMap<>();
          map.put("payBackScore", aDetail.getNumber());
          map.put("totalScore", scoreA.getTotalScore());
          return LejiaResult.build(200, "ok", map);
        }
      }
    }
    return LejiaResult.build(405, "未找到订单信息");
  }

  @ApiOperation("修改订单的收货地址")
  @RequestMapping(value = "/editOrderAddr", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult editAddress(@RequestParam(required = false) Long orderId,
                          @RequestParam(required = false) Long addrId) {
    OnLineOrder onLineOrder = orderService.findOrderById(orderId, false);
    Address address = addressService.findOneAddress(addrId);
    if (onLineOrder != null && address != null) {
      addressService.editOrderAddress(address, onLineOrder);
      return LejiaResult.build(200, "ok");
    } else {
      return LejiaResult.build(402, "未找到订单或地址数据");
    }
  }

  @ApiOperation("取消订单")
  @RequestMapping(value = "/orderCancle", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult orderCancle(@RequestParam(required = true) Long orderId) {
    orderService.orderCancle(orderId);
    return LejiaResult.ok();
  }

  @ApiOperation("确认收货")
  @RequestMapping(value = "/orderConfirm", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult orderConfim(@RequestParam(required = true) Long orderId) {
    orderService.confrimOrder(orderId);
    return LejiaResult.ok();
  }

  /**
   * 查看物流信息
   */
  @RequestMapping(value = "/showExpress/{id}", method = RequestMethod.GET)
  public ModelAndView showExpress(@PathVariable Long id, Model model) {

    OnLineOrder order = orderService.findOnLineOrderById(id);

    if (order.getExpressNumber() == null) {
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
    model.addAttribute("wxUser", order.getLeJiaUser().getWeiXinUser());
    model.addAttribute("address", order.getAddress());
    model.addAttribute("payBackScoreA",
                       scoreAService.findScoreADetailByOrderSid(order.getOrderSid()));

    return MvUtil.go("/weixin/orderInfo");
  }
}
