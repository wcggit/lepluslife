package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.order.controller.dto.OnLineOrderDto;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 金币商品订单相关 Created by zhangwen on 2017/4/5.
 */
@RestController
@RequestMapping("/front/order")
public class GoldOrderController {

  @Inject
  private OrderService orderService;

  @Inject
  private AddressService addressService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private OnlineOrderService onlineOrderService;

  @Inject
  private MessageService messageService;

  /**
   * 金币商品创建订单（包括商品详情页下单和购物车下单）  2017/4/5
   *
   * @param payWay payWay 5=公众号|1=APP
   * @param carts  示例：商品id_商品规格id_数量,商品id_商品规格id_数量
   */
  @RequestMapping(value = "/user/create", method = RequestMethod.POST)
  public LejiaResult createCartOrder(HttpServletRequest request, @RequestParam Long payWay,
                                     @RequestParam(required = false) String carts) {

    LeJiaUser
        leJiaUser =
        leJiaUserService.findUserById(Long.valueOf("" + request.getAttribute("leJiaUserId")));
    Long count = orderService.getCurrentUserObligationOrdersCount(leJiaUser);
    if (count >= 4) {
      return LejiaResult.build(5001, messageService.getMsg("5001"));
    }
    List<CartDetailDto> cartDetailDtoList = stringToList(carts);
    Address address = addressService.findAddressByLeJiaUserAndState(leJiaUser);
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);
    Map<String, Object>
        result =
        onlineOrderService.createGoldOrder(cartDetailDtoList, leJiaUser, address, payWay);
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

    orderDto.setScoreB(scoreC.getScore());
    return LejiaResult.build(200, "ok", orderDto);
  }

  /**
   * 金币类订单提交  2017/04/05
   *
   * @param orderId     订单ID
   * @param trueScore   实际使用金币
   * @param transmitWay 取货方式
   * @param payWay      payWay 5=公众号|1=APP
   */
  @RequestMapping(value = "/submit", method = RequestMethod.POST)
  public LejiaResult goldOrderPay(@RequestParam Long orderId,
                                  @RequestParam String trueScore,
                                  @RequestParam Integer transmitWay,
                                  @RequestParam String message,
                                  @RequestParam Long payWay,
                                  HttpServletRequest request) {
    Long newTrueScore = (long) (Float.parseFloat(trueScore) * 100);
    Map<String, Object>
        result =
        onlineOrderService.completeGoldOrder(orderId, newTrueScore, payWay, transmitWay, message);
    String status = result.get("status").toString();
    if ("2000".equals(status)) {//全金币兑换
      return LejiaResult.build(2000, "success", result.get("data"));
    }
    if (!"200".equals(status)) {
      return LejiaResult
          .build((Integer) result.get("status"), messageService.getMsg("" + result.get("status")));
    }

    OnLineOrder order = (OnLineOrder) result.get("data");
    SortedMap<String, Object> params = weiXinPayService
        .returnPayParams(payWay, request, "金币商城消费", order.getOrderSid(), "" + order.getTruePrice(),
                         Constants.ONLINEORDER_NOTIFY_URL);
    if (params != null) {
      params.put("orderId", order.getId());
      return LejiaResult.ok(params);
    }
    return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
  }

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
