package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 话费订单 Created by zhangwen on 2017/2/22.
 */
@RestController
@RequestMapping("/front/phone")
public class ActivityPhoneOrderController {

  private static Logger log = LoggerFactory.getLogger(ActivityPhoneOrderController.class);

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private MessageService messageService;

  @Inject
  private ActivityPhoneOrderService phoneOrderService;

  /**
   * 金币话费订单生成 生成支付参数  17/2/22
   *
   * @param worth 话费金额
   * @param phone 充值手机号
   */
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public LejiaResult createGoldOrder(@RequestParam Integer worth, @RequestParam String phone,
                                     HttpServletRequest request) {
    LeJiaUser leJiaUser = weiXinService.getCurrentWeiXinUser(request).getLeJiaUser();
    Map<Object, Object> result = null;
    try {
      result = phoneOrderService.createPhoneOrder(leJiaUser, worth, phone, 5L);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
    }
    if (!"200".equals("" + result.get("status"))) {
      return LejiaResult
          .build((Integer) result.get("status"), messageService.getMsg("" + result.get("status")));
    }

    ActivityPhoneOrder order = (ActivityPhoneOrder) result.get("data");
    //话费产品如果是全金币，这直接调用充话费接口
    if (order.getTruePrice() == 0) {
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
        weiXinPayService.buildOrderParams(request, "金币充值话费", order.getOrderSid(),
                                          "" + order.getTruePrice(),
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

  @RequestMapping(value = "/user/userTest", method = RequestMethod.GET)
  public LejiaResult userTest(HttpServletRequest request) {
    System.out
        .println("用户leJiaUserId=========================" + request.getAttribute("leJiaUserId"));
    return LejiaResult.ok("测试成功");
  }

}
