package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneRule;
import com.jifenke.lepluslive.activity.repository.ActivityPhoneOrderRepository;
import com.jifenke.lepluslive.global.util.DateUtils;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.statistics.service.RedisCacheService;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;
import com.jifenke.lepluslive.weixin.service.WxTemMsgService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

  @Value("${telephone.secret}")
  private String phoneSecret;

  @Inject
  private ActivityPhoneOrderRepository repository;

  @Inject
  private ActivityPhoneRuleService ruleService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private RechargeService rechargeService;

  @Inject
  private WxTemMsgService wxTemMsgService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private RedisCacheService redisCacheService;

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
    return repository.findByLeJiaUserAndPayStateOrderByCreateDateDesc(leJiaUser, 1);
  }

  /**
   * 获取某一用户所有的已支付订单  16/11/01
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map<String, Object>> findListByLeJiaUser(Long userId) {
    String
        sql =
        "SELECT worth,true_price AS truePrice,true_scoreb AS trueScore,phone,pay_date AS date,state FROM activity_phone_order WHERE le_jia_user_id = "
        + userId + " AND type = 2 AND pay_state = 1 ORDER BY create_date DESC";
    return redisCacheService.findBySql(sql);
  }

  /**
   * 今日已使用话费池  16/11/01
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Integer todayUsedWorth() {
    return repository.todayUsedWorth();
  }

  /**
   * 手机号或用户每月使用金币充话费是否超限  2017/6/7
   *
   * @param leJiaUser 用户
   * @param phone     手机号
   * @param worth     本次充值面额
   */
  public Map<String, Object> checkUseScore(LeJiaUser leJiaUser, String phone, Integer worth) {
    String s = dictionaryService.findDictionaryById(64L).getValue();
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);
    Map<String, Object> result = new HashMap<>();
    int userLimit = Integer.valueOf(s.split("_")[0]);
    int phoneLimit = Integer.valueOf(s.split("_")[1]);
    int trueScore = 0;
    if (scoreC.getScore().intValue() < worth * 100) {
      trueScore = scoreC.getScore().intValue();
    } else {
      trueScore = worth * 100;
    }
    Integer userThisMonth = sumTrueScoreByLeJiaUserThisMonth(leJiaUser.getId());
    if (userLimit <= userThisMonth + trueScore) {
      result.put("status", 1008);
      String[] o = new String[2];
      o[0] = String.valueOf(userLimit / 100);
      o[1] = String.valueOf(userThisMonth.doubleValue() / 100);
      result.put("arrays", o);
      return result;
    }
    Integer phoneThisMonth = sumTrueScoreByPhoneThisMonth(phone);
    if (phoneLimit <= phoneThisMonth + trueScore) {
      result.put("status", 1009);
      String[] o = new String[2];
      o[0] = String.valueOf(phoneLimit / 100);
      o[1] = String.valueOf(phoneThisMonth.doubleValue() / 100);
      result.put("arrays", o);
      return result;
    }
    result.put("status", 200);
    return result;
  }

  /**
   * 用户本月充值成功使用金币总额  2017/6/7
   *
   * @param userId 用户ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  private Integer sumTrueScoreByLeJiaUserThisMonth(Long userId) {
    Integer i = repository.sumTrueScoreByLeJiaUserThisMonth(userId);
    return i == null ? 0 : i;
  }

  /**
   * 某一手机号本月充值成功使用金币总额  2017/6/7
   *
   * @param phoneNumber 充值手机号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  private Integer sumTrueScoreByPhoneThisMonth(String phoneNumber) {
    Integer i = repository.sumTrueScoreByPhoneThisMonth(phoneNumber);
    return i == null ? 0 : i;
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
      keys[2] = order.getTruePrice() / 100.0 + "元+" + order.getTrueScoreB() / 100 + "金币";
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
   * 生成积分话费订单   16/10/28
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
        order.setPayBackScore(new Random().nextInt(maxA - minA) + minA);
      }
      repository.save(order);
      result.put("status", 200);
      result.put("data", order);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 生成金币话费订单   17/2/23 zhangwen
   *
   * @param worth    充值面额  单位/元
   * @param user     购买用户
   * @param phone    充值手机号码
   * @param payWayId 5=公众号|1=APP
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> createPhoneOrder(LeJiaUser user, Integer worth, String phone,
                                              Long payWayId)
      throws Exception {
    Map<String, Object> result = new HashMap<>();
    //校验充值面额  1,2,5,10,20,50,100,300
    if (!(worth != null && (worth == 20 || worth == 50 || worth == 100))) {
      result.put("status", 1007);
      return result;
    }

    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(user);
    try {
      ActivityPhoneOrder order = new ActivityPhoneOrder();
      order.setPhoneRule(null);
      order.setType(2);
      order.setPhone(phone);
      order.setLeJiaUser(user);
      order.setWorth(worth);
      //金币不足时，以1金币=1元钱兑换
      if (scoreC.getScore().intValue() < worth * 100) {
        order.setTrueScoreB(scoreC.getScore().intValue());
        order.setTruePrice(worth * 100 - scoreC.getScore().intValue());
      } else {
        order.setTruePrice(0);
        order.setTrueScoreB(worth * 100);
      }
      order.setPayOrigin(new PayOrigin(payWayId));
      Integer backScore = 2;
      BigDecimal backA = new BigDecimal(order.getTruePrice()).multiply(
          new BigDecimal(dictionaryService.findDictionaryById(54L).getValue()));
      if (backA.compareTo(new BigDecimal(2)) == 1) {
        backScore = backA.intValue();
      }
      order.setPayBackScore(backScore);
      repository.save(order);
      result.put("status", 200);
      result.put("data", order);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 生成话费订单前检测可否下单   16/10/28
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  private Map<Object, Object> checkCreate(ActivityPhoneRule rule, LeJiaUser user) throws Exception {
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
   * 话费订单微信支付成功后处理  17/2/23
   *
   * @param orderSid 订单Sid
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public synchronized void paySuccess(String orderSid) throws Exception {
    ActivityPhoneOrder order = repository.findByOrderSid(orderSid);
    if (order != null) {
      if (order.getType() == 1) {
        paySuccessByScore(order);
        return;
      }
      paySuccessByGold(order);
    }
  }

  /**
   * 积分类话费订单微信支付成功后处理  16/10/31
   *
   * @param order 积分话费订单
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void paySuccessByScore(ActivityPhoneOrder order) throws Exception {
    try {
      if (order.getPayState() == 0) {
        //如果是限购商品，或不是限购,但又库存限制，减库存
        if (order.getCheap() == 1 || order.getPhoneRule().getRepositoryLimit() == 1) {
          ruleService.reduceRepository(order.getPhoneRule());
        }

        order.setState(1);
        order.setPayState(1);
        order.setPayDate(new Date());
        LeJiaUser user = order.getLeJiaUser();
        Integer payBackScore = order.getPayBackScore();
        scoreAService.giveScoreAByDefault(user, payBackScore, "充话费返鼓励金", 13, order.getOrderSid());
        //减积分
        if (order.getTrueScoreB() != null && order.getTrueScoreB() > 0) {
          scoreBService
              .paySuccess(user, (long) order.getTrueScoreB(), 13, "充话费使用积分", order.getOrderSid());
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
          weiXinUserService.setWeiXinState(user);
        }
        repository.save(order);

        //支付回调成功调用第三方充值接口充值
        Map<Object, Object>
            result =
            rechargeService.submit(order.getPhone(), order.getWorth(), order.getOrderSid());
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

  /**
   * 金币类话费订单微信支付成功后处理  17/2/23
   *
   * @param order 金币话费订单
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void paySuccessByGold(ActivityPhoneOrder order) throws Exception {
    try {
      if (order.getPayState() == 0) {
        order.setState(1);
        order.setPayState(1);
        order.setPayDate(new Date());
        LeJiaUser user = order.getLeJiaUser();
        ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(user);
        //减金币
        if (order.getTrueScoreB() != null && order.getTrueScoreB() > 0) {
          Long score = scoreCService.findScoreByLeJiaUserId(user.getId());
          if (order.getTrueScoreB() > score) {
            System.out.println("scoreC pay fail");
            throw new RuntimeException();
          }
          scoreCService.saveScoreC(scoreC, 0, order.getTrueScoreB().longValue());
          scoreCService
              .saveScoreCDetail(scoreC, 0, order.getTrueScoreB().longValue(), 15003, "充话费消耗金币",
                                order.getOrderSid());

          PayOrigin payOrigin = order.getPayOrigin();
          PayOrigin payWay = new PayOrigin();
          if (order.getTruePrice() == 0) {
            if (payOrigin.getId() == 1) {
              payWay.setId(13L);
            } else {
              payWay.setId(14L);
            }
          } else if (order.getTrueScoreB() == 0) {
            if (payOrigin.getId() == 1) {
              payWay.setId(2L);
            } else {
              payWay.setId(6L);
            }
          } else {
            if (payOrigin.getId() == 1) {
              payWay.setId(11L);
            } else {
              payWay.setId(12L);
            }
          }
          order.setPayOrigin(payWay);

          repository.save(order);

          //支付回调成功调用第三方充值接口充值
          Map<Object, Object>
              result =
              rechargeService.submit(order.getPhone(), order.getWorth(), order.getOrderSid());
          if (result.get("status") == null || "failure"
              .equalsIgnoreCase("" + result.get("status"))) {
            //充值失败
            order.setState(3);
            order.setErrorDate(new Date());
            order.setMessage("" + result.get("message"));
            repository.save(order);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 将订单重新充值  16/12/09
   *
   * @param orderSid 自有订单号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> recharge(String orderSid) throws Exception {
    Map<String, Object> result = new HashMap<>();
    //查询该笔订单是否已经充值
    try {
      Map map = rechargeService.status(orderSid);
      System.out.println(map);
      ActivityPhoneOrder order = repository.findByOrderSid(orderSid);
      String status = String.valueOf(((Map<String, Object>) map.get("data")).get("status"));
      Date date = new Date();
      if ("success".equals(status)) {
        //掉单，将订单设为已支付，并发送模板消息
        Map map2 = (Map) map.get("data");
        order.setState(2);
        order.setWorth(Integer.valueOf(map2.get("card_worth").toString()));
        order.setUsePrice(
            new BigDecimal(map2.get("price").toString()).multiply(new BigDecimal(100)).intValue());
        order.setOrderId(map2.get("order_id").toString());
        order.setCompleteDate(date);
        order.setPlatform(2);
        repository.save(order);
        //给用户发充值模板通知
        String[] keys = new String[4];
        keys[0] = order.getPhone();
        keys[1] = order.getWorth() + ".00元";
        keys[2] = order.getTruePrice() / 100.0 + "元+" + order.getTrueScoreB() / 100.0 + "金币";
        keys[3] =
            new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(order.getPayDate() == null ? date : order.getPayDate());
        wxTemMsgService.sendTemMessage(
            weiXinUserService.findWeiXinUserByLeJiaUser(order.getLeJiaUser()).getOpenId(), 6L,
            keys);
        result.put("status", 101);
        result.put("msg", "充值回调丢失,该订单已经充值成功");
      } else if ("recharging".equals(status) || "init".equals(status)) {
        result.put("status", 102);
        result.put("msg", "该订单正在充值中,请稍后查询");
      } else if ("failure".equals(status)) {
        //调充值接口充值
        orderSid = MvUtil.getOrderNumber();
        Map<Object, Object>
            result2 =
            rechargeService.submit(order.getPhone(), order.getWorth(), orderSid);
        if (result2.get("status") == null || "failure"
            .equalsIgnoreCase("" + result2.get("status"))) {
          //充值失败
          order.setState(3);
          order.setErrorDate(new Date());
          order.setMessage(result.get("message") == null ? "未知错误" : ("" + result.get("message")));
          repository.save(order);
          result.put("status", 103);
          result.put("msg", result.get("message"));
        } else {
          order.setOrderSid(orderSid);
          order.setPlatform(2);
          repository.saveAndFlush(order);
          result.put("status", 100);
          result.put("msg", "重新充值成功，请稍后查询");
        }
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
