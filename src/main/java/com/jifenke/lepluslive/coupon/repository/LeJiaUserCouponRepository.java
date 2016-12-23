package com.jifenke.lepluslive.coupon.repository;

import com.jifenke.lepluslive.coupon.domain.entities.LeJiaUserCoupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户领取的优惠券 Created by zhangwen on 16/11/29.
 */
public interface LeJiaUserCouponRepository extends JpaRepository<LeJiaUserCoupon, Long> {

  /**
   * 查看某个用户是否领取了某张时效内的券  16/11/30
   *
   * @param leJiaUserId 用户
   * @param couponId    优惠券
   */
  @Query(value = "SELECT COUNT(*) FROM le_jia_user_coupon WHERE le_jia_user_id = ?1 AND merchant_coupon_id = ?2 AND begin_date < NOW() AND end_date > NOW()", nativeQuery = true)
  Long countByLeJiaUserAndMerchantCoupon(Long leJiaUserId, Long couponId);

  /**
   * 查看用户某一状态的优惠券列表  16/12/01
   *
   * @param leJiaUserId 用户
   * @param state       状态 1=待使用|2=已使用|3=已过期
   * @param startNum    起始记录
   * @param pageSize    每页显示条数
   */
  @Query(value = "SELECT u.id,u.begin_date,u.end_date,u.pay_type,u.use_score,u.create_date,u.use_date,c.describe1,c.describe2,c.title,c.merchant_name,c.picture FROM le_jia_user_coupon u INNER JOIN merchant_coupon c ON u.merchant_coupon_id = c.id WHERE u.le_jia_user_id = ?1 AND u.state = ?2 ORDER BY u.id DESC LIMIT ?3,?4", nativeQuery = true)
  List<Object[]> findByLeJiaUserAndState(Long leJiaUserId, Integer state, Integer startNum,
                                         Integer pageSize);
}
