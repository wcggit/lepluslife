package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneRule;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangwen on 16/10/26.
 */
public interface ActivityPhoneOrderRepository extends JpaRepository<ActivityPhoneOrder, String> {

  ActivityPhoneOrder findByOrderSid(String orderSid);

  /**
   * 用户本月充值成功使用金币总额  2017/6/7
   *
   * @param userId 用户ID
   */
  @Query(value = "SELECT SUM(true_scoreb) FROM activity_phone_order WHERE le_jia_user_id = ?1 AND pay_state = 1 AND pay_date > DATE_ADD(CURDATE(),interval -day(CURDATE())+1 day)", nativeQuery = true)
  Integer sumTrueScoreByLeJiaUserThisMonth(Long userId);

  /**
   * 某一手机号本月充值成功使用金币总额  2017/6/7
   *
   * @param phoneNumber 充值手机号
   */
  @Query(value = "SELECT SUM(true_scoreb) FROM activity_phone_order WHERE phone = ?1 AND pay_state = 1 AND pay_date > DATE_ADD(CURDATE(),interval -day(CURDATE())+1 day)", nativeQuery = true)
  Integer sumTrueScoreByPhoneThisMonth(String phoneNumber);

  /**
   * 获取某一用户所有的已支付订单  16/11/01
   */
  List<ActivityPhoneOrder> findByLeJiaUserAndPayStateOrderByCreateDateDesc(LeJiaUser leJiaUser,
                                                                           Integer payState);

  /**
   * 今日已使用话费池  16/10/27
   */
  @Query(value = "SELECT SUM(worth) FROM activity_phone_order WHERE pay_state = 1 AND pay_date BETWEEN DATE_FORMAT(NOW(),'%Y-%m-%d') AND DATE_ADD(DATE_FORMAT(NOW(),'%Y-%m-%d'),INTERVAL 1 DAY)", nativeQuery = true)
  Integer todayUsedWorth();

  /**
   * 个人购买更新特惠后的特惠次数  16/10/31
   *
   * @param leJiaUser 用户
   * @param payState  是否已付款
   * @param cheap     是否特惠
   * @param payDate   支付时间
   */
  Integer countByLeJiaUserAndPayStateAndCheapAndPayDateGreaterThan(LeJiaUser leJiaUser,
                                                                   Integer payState, Integer cheap,
                                                                   Date payDate);

  /**
   * 话费产品分类购买限制  16/10/31
   */
  Integer countByPhoneRuleAndLeJiaUserAndPayStateAndPayDateGreaterThan(ActivityPhoneRule rule,
                                                                       LeJiaUser user,
                                                                       Integer payState,
                                                                       Date payDate);

  /**
   * 话费产品累计购买限制  16/10/31
   */
  Integer countByPhoneRuleAndLeJiaUserAndPayState(ActivityPhoneRule rule, LeJiaUser user,
                                                  Integer payState);
}
