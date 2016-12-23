package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneRule;
import com.jifenke.lepluslive.activity.repository.ActivityPhoneOrderRepository;
import com.jifenke.lepluslive.global.util.DateUtils;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;
import com.jifenke.lepluslive.weixin.service.WeixinPayLogService;
import com.jifenke.lepluslive.weixin.service.WxTemMsgService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeixinPayLogService payLogService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private RechargeService rechargeService;

  @Inject
  private WxTemMsgService wxTemMsgService;

  @Inject
  private ScoreBService scoreBService;


  /**
   * 获取某一订单  16/10/31
   *
   * @param orderSid 订单号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityPhoneOrder findByOrderSid(String orderSid) {
    return repository.findByOrderSid(orderSid);
  }

  /**
   * 获取某一订单  16/10/31
   *
   * @param orderId 订单ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityPhoneOrder findByOrderId(String orderId) {
    return repository.findOne(orderId);
  }


  /**
   * 获取某一用户所有的已支付订单  16/11/01
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ActivityPhoneOrder> findAllByLeJiaUser(LeJiaUser leJiaUser) {
    return repository.findByLeJiaUserAndPayStateOrderByIdDesc(leJiaUser, 1);
  }

  /**
   * 今日已使用话费池  16/11/01
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Integer todayUsedWorth() {
    return repository.todayUsedWorth();
  }

  /**
   * 微信支付成功后，充值成功回调 16/10/28
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
      //给用户发充值模板通知
      String[] keys = new String[4];
      keys[0] = order.getPhone();
      keys[1] = order.getWorth() + ".00元";
      keys[2] = order.getTruePrice() / 100.0 + "元+" + order.getTrueScoreB() + "积分";
      keys[3] = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(order.getPayDate());
      wxTemMsgService.sendTemMessage(
          weiXinUserService.findWeiXinUserByLeJiaUser(order.getLeJiaUser()).getOpenId(), 6L, keys);

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
  public ActivityPhoneOrder checkSign(String orderId, String status, String price, Integer worth,
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
   * @param ruleId   话费产品ID
   * @param user     购买用户
   * @param phone    充值手机号码
   * @param payWayId 5=公众号|1=APP
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<Object, Object> createPhoneOrder(Long ruleId, LeJiaUser user, String phone,
                                              Long payWayId)
      throws Exception {
    Map<Object, Object> result = new HashMap<>();
    ActivityPhoneRule rule = ruleService.findById(ruleId);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(user);
    if (rule == null || user == null || scoreB == null) {
      throw new RuntimeException();
    }
    //检测当前用户是否可以下单
    Map<Object, Object> check = checkCreate(rule, user);
    if (check.get("status") != null) {
      result.put("status", check.get("status"));
      return result;
    }

    try {
      ActivityPhoneOrder order = new ActivityPhoneOrder();
      order.setPhoneRule(rule);
      order.setPhone(phone);
      order.setLeJiaUser(user);
      //积分不足时，以1积分=1元钱兑换
      if (scoreB.getScore() < rule.getScore().longValue()) {
        order.setTrueScoreB(scoreB.getScore().intValue());
        Long extra = (rule.getScore().longValue() - scoreB.getScore()) * 100;
        order.setTruePrice(rule.getPrice() + extra.intValue());
      } else {
        order.setTruePrice(rule.getPrice());
        order.setTrueScoreB(rule.getScore());
      }
      order.setPayOrigin(new PayOrigin(payWayId));
      if (rule.getCheap() == 1) {
        order.setCheap(1);
      }
      order.setWorth(rule.getWorth());
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

  /**
   * 生成话费订单前检测可否下单   16/10/28
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Map<Object, Object> checkCreate(ActivityPhoneRule rule, LeJiaUser user) throws Exception {
    Map<Object, Object> result = new HashMap<>();
    if (rule.getState() == 0) { //是否已下线
      result.put("status", 1006);
      return result;
    }
    String[] o = dictionaryService.findDictionaryById(48L).getValue().split("_");
    //以下是判断当日话费池，暂不使用
//    Integer worth = Integer.valueOf(o[0]);
//    Integer todayUsedWorth = repository.todayUsedWorth();
//    if (worth < (todayUsedWorth == null ? 0 : todayUsedWorth) + rule.getWorth()) {//话费池已满
//      result.put("status", 1001);
//      return result;
//    }

    if (rule.getCheap() == 1) {//特惠
      if (rule.getRepository() < 1) {//库存不足
        result.put("status", 1002);
        return result;
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date update = sdf.parse(o[2]);
      Integer limit = Integer.valueOf(o[1]);
      Integer
          cheapSize =
          repository.countByLeJiaUserAndPayStateAndCheapAndPayDateGreaterThan(user, 1, 1, update);
      if (cheapSize >= limit) { //特惠活动参与次数已达上限
        result.put("status", 1003);
        return result;
      }
    } else {//非特惠
      if (rule.getRepositoryLimit() == 1) {//有库存限制
        if (rule.getRepository() < 1) {//库存不足
          result.put("status", 1002);
          return result;
        }
      }
      if (rule.getTotalLimit() != 0) { //有累计购买限制
        Integer totalLimit = repository.countByPhoneRuleAndLeJiaUserAndPayState(rule, user, 1);
        if (totalLimit >= rule.getTotalLimit()) { //累计购买超限
          result.put("status", 1004);
          return result;
        }
      }
      if (rule.getLimitType() != 0) { //有分类购买限制
        Date date = null;
        if (rule.getLimitType() == 1) {//每日限购
          date = DateUtils.getCurrDayBeginDate();
        } else if (rule.getLimitType() == 2) { //每周限购
          date = DateUtils.getCurrWeekBeginDate();
        } else {
          date = DateUtils.getCurrMonthBeginDate();
        }
        Integer
            typeLimit =
            repository.countByPhoneRuleAndLeJiaUserAndPayStateAndPayDateGreaterThan(rule, user, 1,
                                                                                    date);
        if (typeLimit >= rule.getBuyLimit()) {//分类购买超限
          result.put("status", 1005);
          return result;
        }
      }
    }
    return result;
  }

  /**
   * 话费订单微信支付成功后微信的处理  16/10/31
   *
   * @param orderSid 订单ID
   * @param way      1=微信支付|2=全积分
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccess(String orderSid, Integer way) throws Exception {
    ActivityPhoneOrder order = repository.findByOrderSid(orderSid);
    try {
      if (order.getPayState() == 0) {
        //保存微信支付日志
        if (way == 1) {
          payLogService.savePayLog(orderSid, "PhoneOrder");
        }
        //如果是限购商品，或不是限购,但又库存限制，减库存
        if (order.getCheap() == 1 || order.getPhoneRule().getRepositoryLimit() == 1) {
          ruleService.reduceRepository(order.getPhoneRule());
        }

        order.setState(1);
        order.setPayState(1);
        order.setPayDate(new Date());
        LeJiaUser user = order.getLeJiaUser();
        Integer payBackScore = order.getPayBackScore();
        scoreAService.giveScoreAByDefault(user, payBackScore, "充话费返红包", 13, orderSid);
        //减积分
        if (order.getTrueScoreB() != null && order.getTrueScoreB() > 0) {
          scoreBService.paySuccess(user, (long) order.getTrueScoreB(), 13, "充话费使用积分", orderSid);
        }
        PayOrigin payOrigin = order.getPayOrigin();
        PayOrigin payWay = new PayOrigin();
        if (order.getTrueScoreB() != 0) {
          if (payOrigin.getId() == 1) {
            payWay.setId(4L);
          } else {
            payWay.setId(8L);
          }
        } else if (order.getTruePrice() == 0) {
          if (payOrigin.getId() == 1) {
            payWay.setId(9L);
          } else {
            payWay.setId(10L);
          }
        } else {
          if (payOrigin.getId() == 1) {
            payWay.setId(2L);
          } else {
            payWay.setId(6L);
          }
        }
        order.setPayOrigin(payWay);

        //如果返还A红包不为0,改变会员状态
        if (payBackScore > 0) {
          WeiXinUser w = weiXinUserService.findWeiXinUserByLeJiaUser(user);
          if (w != null) {
            if (w.getState() == 0) {
              w.setState(1);
              weiXinUserService.saveWeiXinUser(w);
            }
          }
        }
        repository.save(order);

        //支付回调成功调用第三方充值接口充值
        Map<Object, Object>
            result =
            rechargeService.submit(order.getPhone(), order.getWorth(), orderSid);
        if (result.get("status") == null || "failure".equalsIgnoreCase("" + result.get("status"))) {
          //充值失败
          order.setState(3);
          order.setErrorDate(new Date());
          order.setMessage("" + result.get("message"));
          repository.save(order);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
