package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 微信支付相关 Created by zhangwen on 17/2/21.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinPayService {

  private static Logger log = LoggerFactory.getLogger(WeiXinPayService.class);


  @Value("${weixin.appId}")
  private String appId_JS;

  @Value("${weixin.mchId}")
  private String mchId_JS;

  @Value("${weixin.mchKey}")
  private String mchKey_JS;

  @Value("${app.appId}")
  private String appId_APP;

  @Value("${app.mchId}")
  private String mchId_APP;

  @Value("${app.mchKey}")
  private String mchKey_APP;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeiXinService weiXinService;

  /**
   * 获取公众号支付页面的配置参数wxconfig
   */
  public Map getWeiXinPayConfig(HttpServletRequest request) {
    Long timestamp = new Date().getTime() / 1000;
    String noncestr = MvUtil.getRandomStr();
    Map<String, Object> map = new HashMap<>();
    map.put("appId", appId_JS);
    map.put("timestamp", timestamp);
    map.put("noncestr", noncestr);
    map.put("signature", getJsapiSignature(request, noncestr, timestamp));
    return map;
  }

  /**
   * 返回微信APP支付或公众号支付所需要的参数  2017/4/5
   *
   * @param payWay    payWay 5=公众号|1=APP
   * @param body      body
   * @param orderSid  订单号
   * @param truePrice 实付金额
   * @param notifyUrl 异步回调地址
   */
  public SortedMap<String, Object> returnPayParams(Long payWay, HttpServletRequest request,
                                                   String body,
                                                   String orderSid, String truePrice,
                                                   String notifyUrl) {
    SortedMap<String, Object> map = null;
    SortedMap<String, Object> params = null;
    if (payWay == 5) {
      map = buildOrderParams(request, body, orderSid, truePrice, notifyUrl);
    } else {
      map = buildAPPOrderParams(request, body, orderSid, truePrice, notifyUrl);
    }
    //获取预支付id
    Map<String, Object> unifiedOrder = createUnifiedOrder(map);
    if (unifiedOrder.get("prepay_id") != null) {
      //返回
      if (payWay == 5) {
        params = buildJsapiParams(unifiedOrder.get("prepay_id").toString());
      } else {
        params = buildAppParams(unifiedOrder.get("prepay_id").toString());
      }
      return params;
    }
    return null;
  }


  /**
   * 公众号支付封装预支付参数
   */
  @Transactional(readOnly = true)
  public SortedMap<String, Object> buildOrderParams(HttpServletRequest request, String body,
                                                    String orderSid, String truePrice,
                                                    String notifyUrl) {
    SortedMap<String, Object> orderParams = new TreeMap<>();
    orderParams.put("appid", appId_JS);
    orderParams.put("mch_id", mchId_JS);
    orderParams.put("nonce_str", MvUtil.getRandomStr());
    orderParams.put("body", body);
    orderParams.put("out_trade_no", orderSid);
    orderParams.put("fee_type", "CNY");
    orderParams.put("total_fee", truePrice);
    orderParams.put("spbill_create_ip", getIpAddr(request));
    orderParams.put("notify_url", notifyUrl);
    orderParams.put("trade_type", "JSAPI");
    orderParams.put("input_charset", "UTF-8");
    orderParams.put("openid", weiXinService.getCurrentWeiXinUser(request).getOpenId());
    String sign = createSign(orderParams, "JSAPI");
    orderParams.put("sign", sign);
    return orderParams;
  }

  /**
   * APP支付封装预支付参数  2016/12/13
   */
  @Transactional(readOnly = true)
  public SortedMap<String, Object> buildAPPOrderParams(HttpServletRequest request, String body,
                                                       String orderSid, String truePrice,
                                                       String notifyUrl) {
    SortedMap<String, Object> orderParams = new TreeMap<>();
    orderParams.put("appid", appId_APP);
    orderParams.put("mch_id", mchId_APP);
    orderParams.put("nonce_str", MvUtil.getRandomStr());
    orderParams.put("body", body);
    orderParams.put("out_trade_no", orderSid);
    orderParams.put("fee_type", "CNY");
    orderParams.put("total_fee", truePrice);
    orderParams.put("spbill_create_ip", getIpAddr(request));
    orderParams.put("notify_url", notifyUrl);
    orderParams.put("trade_type", "APP");
    orderParams.put("input_charset", "UTF-8");
    String sign = createSign(orderParams, "APP");
    orderParams.put("sign", sign);
    return orderParams;
  }

  /**
   * 公众号支付封装吊起支付参数
   *
   * @param prepayId 预支付交易会话标识
   */
  public SortedMap<String, Object> buildJsapiParams(String prepayId) {
    SortedMap<String, Object> jsapiParams = new TreeMap<>();
    jsapiParams.put("appId", appId_JS);
    jsapiParams.put("timeStamp", new Date().getTime() / 1000);
    jsapiParams.put("nonceStr", MvUtil.getRandomStr());
    jsapiParams.put("package", "prepay_id=" + prepayId);
    jsapiParams.put("signType", "MD5");
    String sign = createSign(jsapiParams, "JSAPI");
    jsapiParams.put("sign", sign);
    return jsapiParams;
  }

  /**
   * APP支付封装吊起支付参数
   *
   * @param prepayId 预支付交易会话标识
   */
  public SortedMap<String, Object> buildAppParams(String prepayId) {
    SortedMap<String, Object> jsapiParams = new TreeMap<>();
    jsapiParams.put("appid", appId_APP);
    jsapiParams.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
    jsapiParams.put("noncestr", MvUtil.getRandomStr());
    jsapiParams.put("partnerid", mchId_APP);
    jsapiParams.put("prepayid", prepayId);
    jsapiParams.put("package", "Sign=WXPay");
    String sign = createSign(jsapiParams, "APP");
    jsapiParams.put("sign", sign);
    return jsapiParams;
  }

  /**
   * 公众号封装订单查询参数
   */
  @Transactional(readOnly = true)
  public SortedMap<String, Object> buildOrderQueryParams(OnLineOrder onLineOrder) {
    SortedMap<String, Object> orderParams = new TreeMap<>();
    orderParams.put("appid", appId_JS);
    orderParams.put("mch_id", mchId_JS);
    orderParams.put("out_trade_no", onLineOrder.getOrderSid());
    orderParams.put("nonce_str", MvUtil.getRandomStr());
    String sign = createSign(orderParams, "JSAPI");
    orderParams.put("sign", sign);
    return orderParams;
  }

  /**
   * APP封装订单查询参数   16/09/29
   */
  @Transactional(readOnly = true)
  public SortedMap<String, Object> buildAPPOrderQueryParams(OnLineOrder onLineOrder) {
    SortedMap<String, Object> orderParams = new TreeMap<>();
    orderParams.put("appid", appId_APP);
    orderParams.put("mch_id", mchId_APP);
    orderParams.put("out_trade_no", onLineOrder.getOrderSid());
    orderParams.put("nonce_str", MvUtil.getRandomStr());
    String sign = createSign(orderParams, "APP");
    orderParams.put("sign", sign);
    return orderParams;
  }


  /**
   * @return ip地址
   */
  public String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip != null && ip.length() > 15) {
      ip = ip.split(",")[0];
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
  }

  /**
   * 生成微信签名
   */
  public String createSign(Map<String, Object> parameters,
                           String type) {
    StringBuilder sb = new StringBuilder();
    parameters.forEach((k, v) -> {
      if (null != v && !"".equals(v)
          && !"sign".equals(k) && !"key".equals(k)) {
        sb.append(k).append("=").append(v).append("&");
      }
    });
    if ("JSAPI".equals(type)) {
      sb.append("key=").append(mchKey_JS);
    } else {
      sb.append("key=").append(mchKey_APP);
    }
    return MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
  }


  /**
   * 微信统一支付接口，创建微信预支付订单
   */
  public Map<String, Object> createUnifiedOrder(SortedMap<String, Object> orderParams) {
    return WeixinPayUtil
        .createUnifiedOrder("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",
                            getRequestXml(orderParams));
  }

  /**
   * 查询订单状态
   */
  public Map<String, Object> orderStatusQuery(SortedMap<String, Object> orderParams) {
    return WeixinPayUtil
        .createUnifiedOrder("https://api.mch.weixin.qq.com/pay/orderquery", "POST",
                            getRequestXml(orderParams));
  }

  /**
   * 获取请求的XML
   */
  public String getRequestXml(SortedMap<String, Object> parameters) {
    StringBuilder sb = new StringBuilder();
    sb.append("<xml>");
    Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
    Iterator it = es.iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      String k = (String) entry.getKey();
      String v = (String) entry.getValue();
      sb.append("<" + k + ">" + v + "</" + k + ">");
    }
    sb.append("</xml>");
    log.debug(sb.toString());
    return sb.toString();
  }

  public String getJsapiSignature(HttpServletRequest request, String noncestr, Long timestamp) {
    StringBuilder sb = new StringBuilder();
    sb.append("jsapi_ticket=");
    sb.append(dictionaryService.findDictionaryById(8L).getValue());
    sb.append("&noncestr=");
    sb.append(noncestr);
    sb.append("&timestamp=");
    sb.append(timestamp);
    sb.append("&url=");
    sb.append(getCompleteRequestUrl(request));
    log.debug("JsapiSignature" + sb.toString());
    log.debug("sha1Signature" + DigestUtils.sha1Hex(sb.toString()));
    return DigestUtils.sha1Hex(sb.toString());
  }

  public String getCompleteRequestUrl(HttpServletRequest request) {

    return Constants.WEI_XIN_ROOT_URL +
           request.getRequestURI() +
           (request.getQueryString() != null ? "?" + request.getQueryString() : "");
  }
}
