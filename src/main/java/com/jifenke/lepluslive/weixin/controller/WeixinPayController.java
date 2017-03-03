package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeixinPayLogService;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private OnlineOrderService onlineOrderService;

  @Inject
  private MessageService messageService;

  @Inject
  private ActivityPhoneOrderService phoneOrderService;

  @Inject
  private WeixinPayLogService weixinPayLogService;

  /**
   * 话费订单生成 生成支付参数  16/10/28
   *
   * @param ruleId 话费产品ID
   * @param phone  充值手机号
   */
  @RequestMapping(value = "/phonePay", method = RequestMethod.POST)
  public LejiaResult phoneOrderPay(@RequestParam Long ruleId, @RequestParam String phone,
                                   HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    Map<Object, Object> result = null;
    try {
      result = phoneOrderService.createPhoneOrder(ruleId, weiXinUser.getLeJiaUser(), phone, 5L);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
    }
    if (!"200".equals("" + result.get("status"))) {
      return LejiaResult
          .build((Integer) result.get("status"), messageService.getMsg("" + result.get("status")));
    }

    ActivityPhoneOrder order = (ActivityPhoneOrder) result.get("data");
    //话费产品如果是全积分，这直接调用充话费接口
    if (order.getPhoneRule().getPayType() == 3) {
      try {
        phoneOrderService.paySuccess(order.getOrderSid());
        return LejiaResult.build(2000, "支付成功", order.getId());
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
      }
    }
    //封装订单参数
    SortedMap<String, Object>
        map =
        weiXinPayService
            .buildOrderParams(request, "话费充值", order.getOrderSid(), "" + order.getTruePrice(),
                              Constants.PHONEORDER_NOTIFY_URL);
    //获取预支付id
    Map<String, Object> unifiedOrder = weiXinPayService.createUnifiedOrder(map);
    if (unifiedOrder.get("prepay_id") != null) {
      //返回前端页面
      SortedMap<String, Object>
          params =
          weiXinPayService.buildJsapiParams(unifiedOrder.get("prepay_id").toString());
      params.put("orderId", order.getId());
      return LejiaResult.ok(params);
    }
    return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
  }

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
          weiXinPayService.createSign("UTF-8", map, String.valueOf(map.get("trade_type")));
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
            buffer.append("<xml>");
            buffer.append("<return_code>FAIL</" + "return_code" + ">");
            buffer.append("</xml>");
            String s = buffer.toString();
            response.setContentType("application/xml");
            response.getWriter().write(s);
            return;
          }
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
    }
  }

  @RequestMapping(value = "/phoneSuccess/{orderId}", method = RequestMethod.GET)
  public ModelAndView phoneSuccessPage(@PathVariable String orderId, HttpServletRequest request,
                                       Model model) {

    ActivityPhoneOrder order = phoneOrderService.findByOrderId(orderId);
    model.addAttribute("order", order);
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    if (order.getType() == 2) {
      return MvUtil.go("/gold/recharge/success");
    }

    return MvUtil.go("/activity/phone/success");
  }

  //微信支付接口
  @RequestMapping(value = "/weixinpay")
  public
  @ResponseBody
  Map<String, Object> weixinPay(@RequestParam Long orderId, @RequestParam String truePrice,
                                @RequestParam Long trueScore,
                                @RequestParam Integer transmitWay,
                                HttpServletRequest request) {
//    Long newTruePrice = (long) (Float.parseFloat(truePrice) * 100);
    Long newTruePrice = new BigDecimal(truePrice).multiply(new BigDecimal(100)).longValue();
    if (newTruePrice == 0) {//全积分兑换流程
      try {
        return onlineOrderService.orderPayByScoreB(orderId, trueScore, transmitWay, 10L);
      } catch (Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 500);
        return map;
      }
    }
    Map<String, Object>
        result =
        orderService.setPriceScoreForOrder(orderId, newTruePrice, trueScore, transmitWay);
    if (!"200".equals(result.get("status").toString())) {
      result.put("msg", messageService.getMsg("" + result.get("status")));
      return result;
    }

    OnLineOrder order = (OnLineOrder) result.get("data");
    //封装订单参数
    SortedMap<String, Object>
        map =
        weiXinPayService
            .buildOrderParams(request, "乐加商城消费", order.getOrderSid(), "" + order.getTruePrice(),
                              Constants.ONLINEORDER_NOTIFY_URL);
    //获取预支付id
    Map<String, Object> unifiedOrder = weiXinPayService.createUnifiedOrder(map);
    if (unifiedOrder.get("prepay_id") != null) {
      //返回前端页面
      SortedMap<String, Object>
          jsapiParams =
          weiXinPayService.buildJsapiParams(unifiedOrder.get("prepay_id").toString());
      jsapiParams.put("status", 200);
      return jsapiParams;
    } else {
      log.error(unifiedOrder.get("return_msg").toString());
      unifiedOrder.clear();
      unifiedOrder.put("status", 500);
      unifiedOrder.put("msg", "出现未知错误,请联系管理员或稍后重试");
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
          weiXinPayService.createSign("UTF-8", map, String.valueOf(map.get("trade_type")));
      System.out.println("签名==========" + sign);
      if (map.get("sign") != null && String.valueOf(map.get("sign")).equals(sign)) {
        String returnCode = (String) map.get("return_code");
        String resultCode = (String) map.get("result_code");
        //操作订单
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
          try {
            orderService.paySuccess(map);
          } catch (Exception e) {
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
      }
    }
  }

  @RequestMapping(value = "/paySuccess/{truePrice}")
  public ModelAndView goPaySuccessPage(@PathVariable Long truePrice, Model model,
                                       HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("totalScore", scoreAService.findScoreAByLeJiaUser(weiXinUser.getLeJiaUser())
        .getTotalScore());
    Integer PAY_BACK_SCALE = Integer.parseInt(dictionaryService.findDictionaryById(3L).getValue());
    model.addAttribute("payBackScore",
                       (long) Math.ceil((double) (truePrice * PAY_BACK_SCALE) / 100));
    model.addAttribute("truePrice", truePrice);

    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(weiXinUser.getLeJiaUser());
    //商品分类
    List<ProductType> typeList = productService.findAllProductType();
    //主打爆品
    Map product = productService.findMainHotProduct();
    model.addAttribute("scoreB", scoreB);
    model.addAttribute("product", product);
    model.addAttribute("typeList", typeList);
    return MvUtil.go("/product/productIndex");
  }


  @RequestMapping(value = "/payFail/{orderId}")
  public ModelAndView goPayFailPage(@PathVariable Long orderId, Model model,
                                    HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(weiXinUser.getLeJiaUser());
    //商品分类
    List<ProductType> typeList = productService.findAllProductType();
    //主打爆品
    Map product = productService.findMainHotProduct();
    model.addAttribute("scoreB", scoreB);
    model.addAttribute("product", product);
    model.addAttribute("typeList", typeList);
    model.addAttribute("orderId", orderId);
    return MvUtil.go("/product/productIndex");
  }

  /**
   * 金币类订单提交  2017/02/20
   *
   * @param orderId     订单ID
   * @param truePrice   实际支付金额
   * @param trueScore   实际使用金币
   * @param transmitWay 取货方式
   */
  @RequestMapping(value = "/goldOrder", method = RequestMethod.POST)
  public Map<String, Object> goldOrderPay(@RequestParam Long orderId,
                                          @RequestParam String truePrice,
                                          @RequestParam Long trueScore,
                                          @RequestParam Integer transmitWay,
                                          HttpServletRequest request) {
    Long newTruePrice = (long) (Float.parseFloat(truePrice) * 100);
    if (newTruePrice == 0) {//全金币兑换流程
      try {
        return onlineOrderService.orderPayByScoreC(orderId, trueScore, transmitWay, 14L);
      } catch (Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 500);
        return map;
      }
    }
    Map<String, Object>
        result =
        onlineOrderService.completeGoldOrder(orderId, newTruePrice, trueScore, transmitWay);
    if (!"200".equals(result.get("status").toString())) {
      result.put("msg", messageService.getMsg("" + result.get("status")));
      return result;
    }

    OnLineOrder order = (OnLineOrder) result.get("data");
    //封装订单参数
    SortedMap<String, Object>
        map =
        weiXinPayService
            .buildOrderParams(request, "金币商城消费", order.getOrderSid(), "" + order.getTruePrice(),
                              Constants.ONLINEORDER_NOTIFY_URL);
    //获取预支付id
    Map<String, Object> unifiedOrder = weiXinPayService.createUnifiedOrder(map);
    if (unifiedOrder.get("prepay_id") != null) {
      //返回前端页面
      SortedMap<String, Object>
          jsapiParams =
          weiXinPayService.buildJsapiParams(unifiedOrder.get("prepay_id").toString());
      jsapiParams.put("status", 200);
      return jsapiParams;
    } else {
      log.error(unifiedOrder.get("return_msg").toString());
      unifiedOrder.clear();
      unifiedOrder.put("status", 500);
      unifiedOrder.put("msg", "出现未知错误,请联系管理员或稍后重试");
      return unifiedOrder;
    }
  }


}
