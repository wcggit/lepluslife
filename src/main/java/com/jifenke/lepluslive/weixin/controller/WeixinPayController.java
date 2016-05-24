package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

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
  private OrderService orderService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private ProductService productService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ScoreAService scoreAService;

  //微信支付接口
  @RequestMapping(value = "/weixinpay")
  public
  @ResponseBody
  Map<Object, Object> weixinPay(@RequestParam Long  orderId, @RequestParam String truePrice,
                                @RequestParam Long trueScore, HttpServletRequest request) {
    Long newTruePrice = (long) (Float.parseFloat(truePrice) * 100);
    OnLineOrder onLineOrder = orderService.setPriceScoreForOrder(orderId,newTruePrice,trueScore);
    if(onLineOrder ==null){
      HashMap<Object, Object> map = new HashMap<>();
      map.put("err_msg","库存不足~");
      return map;
    }

    //封装订单参数
    SortedMap<Object, Object> map = weiXinPayService.buildOrderParams(request, onLineOrder);
    //获取预支付id
    Map unifiedOrder = weiXinPayService.createUnifiedOrder(map);
    if(unifiedOrder.get("prepay_id")!=null) {
      //返回前端页面
      return weiXinPayService.buildJsapiParams(unifiedOrder.get("prepay_id").toString());
    }else{
      log.error(unifiedOrder.get("return_msg").toString());
       unifiedOrder.clear();
      unifiedOrder.put("err_msg","出现未知错误,请联系管理员或稍后重试");
      return unifiedOrder;
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
    StringBuffer buffer = new StringBuffer();
    while ((str = bufferedReader.readLine()) != null) {
      buffer.append(str);
    }
    Map map = WeixinPayUtil.doXMLParse(buffer.toString());
    String orderSid = (String) map.get("out_trade_no");
    String returnCode = (String) map.get("return_code");
    //操作订单
    try {
      orderService.paySuccess(orderSid);
    }catch (Exception e) {
      log.error(e.getMessage());
      buffer.delete(0, buffer.length());
      buffer.append("<xml>");
      buffer.append("<return_code>FAIL</" + "return_code" + ">");
      buffer.append("</xml>");
      String s = buffer.toString();
      response.setContentType("application/xml");
      response.getWriter().write(s);
      return;
    }
    //返回微信的信息
    buffer.delete(0, buffer.length());
    buffer.append("<xml>");
    buffer.append("<return_code>" + returnCode + "</" + "return_code" + ">");
    buffer.append("</xml>");
    String s = buffer.toString();
    response.setContentType("application/xml");
    response.getWriter().write(s);


  }

  @RequestMapping(value = "/paySuccess/{truePrice}")
  public ModelAndView goPaySuccessPage(@PathVariable Long truePrice, Model model,HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("totalScore", scoreAService.findScoreAByLeJiaUser(weiXinUser.getLeJiaUser()).getTotalScore());
    model.addAttribute("payBackScore", (long) Math.ceil((double) (truePrice * 12) / 100));
    model.addAttribute("productTypes",productService.findAllProductType());
    model.addAttribute("truePrice",truePrice);
    return MvUtil.go("/weixin/product");
  }


  @RequestMapping(value = "/payFail/{orderId}")
  public ModelAndView goPayFailPage(@PathVariable Long orderId, Model model) {
    model.addAttribute("productTypes",productService.findAllProductType());
    model.addAttribute("orderId",orderId);
    return MvUtil.go("/weixin/product");
  }

}
