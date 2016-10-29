package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneRule;
import com.jifenke.lepluslive.activity.repository.ActivityPhoneOrderRepository;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;


/**
 * 手机话费订单相关 Created by zhangwen on 2016/10/26.
 */
@Service
public class ActivityPhoneOrderService {

  @Value("${telephone.appkey}")
  private String phoneKey;

  @Value("${telephone.secret}")
  private String phoneSecret;

  @Inject
  private ActivityPhoneOrderRepository repository;

  @Inject
  private ActivityPhoneRuleService ruleService;

  /**
   * 微信支付成功后，充值成功操作 16/10/28
   *
   * @param order   充值成功订单
   * @param orderId 第三方订单号
   * @param price   成本价
   * @param worth   面值
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccess(ActivityPhoneOrder order, String orderId, Integer price, Integer worth)
      throws Exception {
    Date date = new Date();
    try {
      order.setState(2);
      order.setWorth(worth);
      order.setUsePrice(price);
      order.setOrderId(orderId);
      order.setCompleteDate(date);
      repository.save(order);
      //给用户发红包
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }


  /**
   * 微信支付成功后，充值失败操作 16/10/28
   *
   * @param orderSid 自己的订单号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void payFail(String orderSid, String msg) throws Exception {
    try {
      ActivityPhoneOrder order = repository.findByOrderSid(orderSid);
      order.setMessage(msg);
      order.setErrorDate(new Date());
      order.setState(3);
      repository.save(order);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  /**
   * 验证回调签名  16/10/28
   *
   * @param orderId  第三方订单号
   * @param status   订单状态
   * @param price    成本价
   * @param worth    面额
   * @param orderSid 自己的订单号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityPhoneOrder checkSign(String orderId, String status, Integer price, Integer worth,
                                      String orderSid, String sign) throws Exception {
    try {
      ActivityPhoneOrder order = repository.findByOrderSid(orderSid);
      //验证签名 order_id + status + worth + price + phone + sp_order_id + secret_key得到字符串A,将A字符串进行md5[结果字符串为小写]
      String
          mySign =
          MD5Util.MD5Encode(
              orderId + status + worth + price + order.getPhone() + orderSid + phoneSecret, "UTF-8")
              .toLowerCase();
      if (sign.equals(mySign)) {
        return order;
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  /**
   * 生成话费订单   16/10/28
   *
   * @param ruleId     话费产品ID
   * @param weiXinUser 购买用户
   * @param phone      充值手机号码
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<Object, Object> createPhoneOrder(Long ruleId, WeiXinUser weiXinUser, String phone)
      throws Exception {
    Map<Object, Object> result = new HashMap<>();
    ActivityPhoneRule rule = ruleService.findById(ruleId);
    if (rule == null || weiXinUser == null) {
      throw new RuntimeException();
    }
    try {
      ActivityPhoneOrder order = new ActivityPhoneOrder();
      order.setPhoneRule(rule);
      order.setPhone(phone);
      order.setLeJiaUser(weiXinUser.getLeJiaUser());
      order.setTruePrice(rule.getPrice());
      order.setTrueScoreB(rule.getScore());
      //订单返红包
      if (rule.getRebateType() == 0) {
        order.setPayBackScore(rule.getRebate());
      } else {
        int maxA = rule.getRebate();
        int minA = rule.getMinRebate();
        order.setPayBackScore(new Random().nextInt((maxA - minA) / 10) * 10 + minA);
      }
      repository.save(order);
      result.put("status", 200);
      result.put("data", order);
      return result;
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }
}
