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

  //臻品订单微信支付回调地址  APP&WEB  ↓
  public static final String ONLINEORDER_NOTIFY_URL = NOTIFY_ROOT_URL + "/order/afterPay";

  //话费订单微信支付回调地址  APP&WEB  ↓
  public static final String PHONEORDER_NOTIFY_URL = NOTIFY_ROOT_URL + "/weixin/pay/afterPhonePay";

  //话费订单 第三方平台回调接口 APP&WEB  ↓
  public static final String PHONE_NOTIFY_URL = NOTIFY_ROOT_URL + "/front/phone/afterPay";  //充值回调

  //电影票订单微信支付回调地址  WEB  ↓
  public static final String MOVIE_NOTIFY_URL = NOTIFY_ROOT_URL + "/front/movie/afterPay";

}
