package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.activity.service.RechargeService;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;

import org.jdom.JDOMException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机充值 Created by zhangwen on 2016/10/26.
 */
@RestController
@RequestMapping("/front/phone")
public class RechargeController {

  @Inject
  private MessageService messageUtil;

  @Inject
  private RechargeService rechargeService;

  @Inject
  private ActivityPhoneOrderService orderService;

  /**
   * 检查当前是否可以下单，以及下单价格  16/10/26
   *
   * @param phone 手机号码
   * @param worth 充值面值
   */
  @RequestMapping(value = "/check", method = RequestMethod.GET)
  public LejiaResult check(@RequestParam String phone, @RequestParam Integer worth) {

    Map result = rechargeService.check(phone, worth);
    if (result != null) {
      if ("success".equals(result.get("status"))) {
        return LejiaResult.ok();
      } else {
        return LejiaResult.build(500, "" + result.get("message"));
      }
    }
    return LejiaResult.build(500, messageUtil.getMsg("500"));
  }

  /**
   * 充值回调函数 16/10/28
   */
  @RequestMapping(value = "/afterPay", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
  public void afterPay(HttpServletRequest request, HttpServletResponse response)
      throws IOException, JDOMException {
    String result = "success";
    Map map = request.getParameterMap();
    String status = (String) map.get("status");
    String orderSid = (String) map.get("sp_order_id");

    if ("success".equalsIgnoreCase(status)) {
      String orderId = (String) map.get("order_id");
      Integer worth = (Integer) map.get("worth");
      Integer price = (Integer) map.get("price");
      String sign = (String) map.get("sign");
      try {//验证签名
        ActivityPhoneOrder
            order =
            orderService.checkSign(orderId, status, price, worth, orderSid, sign);
        if (order != null && order.getState() != 2) {//处理订单
          orderService.paySuccess(order,orderId,price,worth);
        } else {
          result = "failed";
        }
      } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("failed");
      }
    } else {
      //处理失败，对订单进行标识
      String message = (String) map.get("message");
      try {
        orderService.payFail(orderSid, message);
      } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("failed");
      }
    }
    //返回信息
    response.getWriter().write(result);
  }


}
