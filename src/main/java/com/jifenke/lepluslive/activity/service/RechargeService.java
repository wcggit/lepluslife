package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.HttpUtils;
import com.jifenke.lepluslive.global.util.MD5Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 手机话费充值 Created by zhangwen on 2016/10/26.
 */
@Service
public class RechargeService {

  @Value("${telephone.appkey}")
  private String phoneKey;

  @Value("${telephone.secret}")
  private String phoneSecret;

  /**
   * 检查当前是否可以下单，以及下单价格  16/10/26
   *
   * @param phone 手机号码
   * @param worth 充值卡面值
   */
  public Map check(String phone, Integer worth) {
    SortedMap<Object, Object> orderParams = new TreeMap<>();
    orderParams.put("card_worth", worth);
    orderParams.put("phone_number", phone);
    orderParams.put("api_key", phoneKey);
    String url = Constants.PHONE_CHECK + buildParams(orderParams);

    return HttpUtils.get(url);
  }

  /**
   * 充值  16/10/28
   *
   * @param phone    要充值的手机号码
   * @param worth    充值卡面值 	面前开放的面值有:1,2,5,10,20,50,100,300[元]
   * @param orderSid 自己系统订单号
   */
  public Map<Object, Object> submit(String phone, Integer worth, String orderSid) {
    SortedMap<Object, Object> orderParams = new TreeMap<>();
    orderParams.put("card_worth", worth);
    orderParams.put("phone_number", phone);
    orderParams.put("notify_url", Constants.PHONE_NOTIFY_URL);
    orderParams.put("sp_order_id", orderSid);
    orderParams.put("api_key", phoneKey);
    String url = Constants.PHONE_SUBMIT + buildParams(orderParams);
    return HttpUtils.get(url);
  }


  /**
   * 生成签名 封装Get查询参数字符串   16/10/26
   *
   * @return GET请求字符串
   */
  private String buildParams(SortedMap<Object, Object> orderParams) {
    StringBuilder sb = new StringBuilder();
    StringBuilder result = new StringBuilder();
    for (Map.Entry entry : orderParams.entrySet()) {
      Object k = entry.getKey();
      Object v = entry.getValue();
      if (null != v) {
        sb.append(k).append(v);
        result.append(k).append("=").append(v).append("&");
      }
    }
    sb.append(phoneSecret);
    result.append("sign=").append(MD5Util.MD5Encode(sb.toString(), "UTF-8").toLowerCase());
    return result.toString();
  }

  /**
   * 查询该订单是否已经充值  16/12/09
   *
   * @param orderSid 自有订单号
   */
  public Map status(String orderSid) {
    SortedMap<Object, Object> orderParams = new TreeMap<>();
    orderParams.put("sp_order_id", orderSid);
    orderParams.put("api_key", phoneKey);
    String url = Constants.PHONE_STATUS + buildParams(orderParams);

    return HttpUtils.get(url);
  }

//  /**
//   * 获得当前account余额  16/10/26
//   */
//  public Map balance() {
//    SortedMap<Object, Object> orderParams = new TreeMap<>();
//    orderParams.put("api_key", phoneKey);
//    String url = Constants.PHONE_BALANCE + buildParams(orderParams);
//    return HttpUtils.get(url);
//  }
}
