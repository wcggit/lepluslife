package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.global.util.CookieUtils;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 16/3/21.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinPayService {

  private static Logger log = LoggerFactory.getLogger(WeiXinPayService.class);


  @Value("${weixin.appId}")
  private String appId;

  @Value("${weixin.mchId}")
  private String mchId;

  @Value("${app.appId}")
  private String _appId;

  @Value("${app.mchId}")
  private String _mchId;

  @Value("${weixin.weixinRootUrl}")
  private String weixinRootUrl;

  @Value("${weixin.mchKey}")
  private String mchKey;

  @Value("${app.mchKey}")
  private String _mchKey;

  @Value("${weixin.jsapiTicket}")
  private String jsapiTicket;


  /**
   * 封装订单参数
   */
  @Transactional(readOnly = true)
  public SortedMap<Object, Object> buildOrderParams(HttpServletRequest request,
                                                    OnLineOrder onLineOrder) {
    SortedMap<Object, Object> orderParams = new TreeMap<Object, Object>();
    orderParams.put("appid", appId);
    orderParams.put("mch_id", mchId);
    orderParams.put("nonce_str", MvUtil.getRandomStr());
//   String body = "";
//    for (OrderDetail orderDetail : order.getOrderDetails()) {
//      if (body.equals("")) {
//        body = body + orderDetail.getProduct().getName();
//      }
//      body = body + "+" + orderDetail.getProduct().getName();
//    }
    orderParams.put("body", "乐加商城消费");
    orderParams.put("out_trade_no", onLineOrder.getOrderSid());
    orderParams.put("fee_type", "CNY");
    orderParams.put("total_fee", String.valueOf(onLineOrder.getTruePrice()));
    orderParams.put("spbill_create_ip", getIpAddr(request));
    orderParams.put("notify_url", weixinRootUrl + "/weixin/pay/afterPay");
    orderParams.put("trade_type", "JSAPI");
    orderParams.put("input_charset", "UTF-8");
    orderParams.put("openid", CookieUtils.getCookieValue(request, appId + "-user-open-id"));
    String sign = createSign("UTF-8", orderParams, mchKey);
    orderParams.put("sign", sign);
    return orderParams;
  }

  /**
   * APP支付封装订单参数
   */
  @Transactional(readOnly = true)
  public SortedMap<Object, Object> _buildOrderParams(HttpServletRequest request,
                                                     OnLineOrder onLineOrder) {
    SortedMap<Object, Object> orderParams = new TreeMap<Object, Object>();
    orderParams.put("appid", _appId);
    orderParams.put("mch_id", _mchId);
    orderParams.put("nonce_str", MvUtil.getRandomStr());
    orderParams.put("body", "乐加商城消费");
    orderParams.put("out_trade_no", onLineOrder.getOrderSid());
    orderParams.put("fee_type", "CNY");
    // orderParams.put("total_fee", String.valueOf(onLineOrder.getTruePrice()));
    orderParams.put("total_fee", "1");
    orderParams.put("spbill_create_ip", getIpAddr(request));
    orderParams.put("notify_url", weixinRootUrl + "/weixin/pay/afterPay");
    orderParams.put("trade_type", "APP");
    orderParams.put("input_charset", "UTF-8");
    String sign = createSign("UTF-8", orderParams, _mchKey);
    orderParams.put("sign", sign);
    return orderParams;
  }

  /**
   * 封装订单查询参数
   */
  @Transactional(readOnly = true)
  public SortedMap<Object, Object> buildOrderQueryParams(OnLineOrder onLineOrder) {
    SortedMap<Object, Object> orderParams = new TreeMap<Object, Object>();
    orderParams.put("appid", appId);
    orderParams.put("mch_id", mchId);
    orderParams.put("out_trade_no", onLineOrder.getOrderSid());
    orderParams.put("nonce_str", MvUtil.getRandomStr());
    String sign = createSign("UTF-8", orderParams, mchKey);
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
    return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
  }

  /**
   * 生成微信签名
   */
  public String createSign(String characterEncoding, SortedMap<Object, Object> parameters,
                           String mch_key) {
    StringBuffer sb = new StringBuffer();
    Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
    Iterator it = es.iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      String k = (String) entry.getKey();
      Object v = entry.getValue();
      if (null != v && !"".equals(v)
          && !"sign".equals(k) && !"key".equals(k)) {
        sb.append(k + "=" + v + "&");
      }
    }
    sb.append("key=" + mch_key);
    String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    return sign;
  }


  /**
   * 微信统一支付接口，创建微信预支付订单
   */
  public Map<Object, Object> createUnifiedOrder(SortedMap orderParams) {
    return WeixinPayUtil
        .createUnifiedOrder("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",
                            getRequestXml(orderParams));
  }

  /**
   * 查询订单状态
   */
  public Map<Object, Object> orderStatusQuery(SortedMap orderParams) {
    return WeixinPayUtil
        .createUnifiedOrder("https://api.mch.weixin.qq.com/pay/orderquery", "POST",
                            getRequestXml(orderParams));
  }

  /**
   * 获取请求的XML
   */
  public String getRequestXml(SortedMap<Object, Object> parameters) {
    StringBuffer sb = new StringBuffer();
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

  public SortedMap buildJsapiParams(String prepayId) {
    SortedMap<Object, Object> jsapiParams = new TreeMap<Object, Object>();
    jsapiParams.put("appId", appId);
    jsapiParams.put("timeStamp", new Date().getTime() / 1000);
    jsapiParams.put("nonceStr", MvUtil.getRandomStr());
    jsapiParams.put("package", "prepay_id=" + prepayId);
    jsapiParams.put("signType", "MD5");
    String sign = createSign("UTF-8", jsapiParams, mchKey);
    jsapiParams.put("sign", sign);
    return jsapiParams;
  }

  public SortedMap buildAppParams(String prepayId) {
    SortedMap<Object, Object> jsapiParams = new TreeMap<Object, Object>();
    jsapiParams.put("appid", _appId);
    jsapiParams.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
    jsapiParams.put("noncestr", MvUtil.getRandomStr());
    jsapiParams.put("partnerid", _mchId);
    jsapiParams.put("prepayid", prepayId);
    jsapiParams.put("package", "Sign=WXPay");
    String sign = createSign("UTF-8", jsapiParams, _mchKey);
    jsapiParams.put("sign", sign);
    return jsapiParams;
  }

  public String getJsapiSignature(HttpServletRequest request, String noncestr, Long timestamp) {
    StringBuffer sb = new StringBuffer();
    sb.append("jsapi_ticket=");
    sb.append(WeixinPayUtil.getAccessToken().getJsapiTickek());
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

    String s = weixinRootUrl +
               request.getRequestURI() +
               (request.getQueryString() != null ? "?" + request.getQueryString() : "");
    return s;
  }
}
