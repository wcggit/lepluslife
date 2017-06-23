package com.jifenke.lepluslive.global.config;

/**
 * APP 相关常量池
 *
 * @author zhangwen【zhangwenit@126.com】 2017/6/9 14:07
 **/
public final class AppConstants {

  public static int REQUEST_TIMESTAMP_ALLOW_RANGE = 900000; //请求时间戳允许误差/毫秒

  //线上域名  ↓
  private static final String NOTIFY_ROOT_URL = "http://www.lepluslife.com"; //线上
//  private static final String NOTIFY_ROOT_URL = "http://www.tiegancrm.com";  //测试

  //团购订单微信支付回调地址  APP&WEB  ↓
  public static final String GROUPON_ORDER_NOTIFY_URL = NOTIFY_ROOT_URL + "/groupon/afterPay";


}
