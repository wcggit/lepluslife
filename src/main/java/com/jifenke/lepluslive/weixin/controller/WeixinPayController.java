package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeixinPayLogService;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/3/21.
 */
@RestController
@RequestMapping("/weixin/pay")
public class WeixinPayController {

  private static Logger log = LoggerFactory.getLogger(WeixinPayController.class);


  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private ActivityPhoneOrderService phoneOrderService;

  @Inject
  private WeixinPayLogService weixinPayLogService;

  /**
   * 话费订单 微信回调函数 16/10/31
   */
  @RequestMapping(value = "/afterPhonePay", produces = MediaType.APPLICATION_XML_VALUE)
  public void afterPhonePay(HttpServletRequest request, HttpServletResponse response)
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
      //验签
      String
          sign =
          weiXinPayService.createSign(map, String.valueOf(map.get("trade_type")));
      if (map.get("sign") != null && String.valueOf(map.get("sign")).equals(sign)) {
        //保存微信支付日志
        weixinPayLogService.savePayLog(map, "PhoneOrder", 1);

        String orderSid = (String) map.get("out_trade_no");
        String returnCode = (String) map.get("return_code");
        String resultCode = (String) map.get("result_code");
        //操作订单
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
          try {
            phoneOrderService.paySuccess(orderSid);
          } catch (Exception e) {
            log.error(e.getMessage());
            buffer.delete(0, buffer.length());
            buffer.append("<xml><return_code>FAIL</return_code></xml>");
            String s = buffer.toString();
            response.setContentType("application/xml");
            response.getWriter().write(s);
            return;
          }
        }
        //返回微信的信息
        buffer.delete(0, buffer.length());
        buffer.append("<xml><return_code>").append(returnCode).append("</return_code></xml>");
        String s = buffer.toString();
        response.setContentType("application/xml");
        response.getWriter().write(s);
      }
    }
  }

  /**
   * 金币充话费成功  2017/4/5
   */
  @RequestMapping(value = "/phoneSuccess/{orderId}", method = RequestMethod.GET)
  public ModelAndView phoneSuccessPage(@PathVariable String orderId, HttpServletRequest request,
                                       Model model) {

    ActivityPhoneOrder order = phoneOrderService.findByOrderId(orderId);
    model.addAttribute("order", order);
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    return MvUtil.go("/gold/recharge/success");
  }

}
