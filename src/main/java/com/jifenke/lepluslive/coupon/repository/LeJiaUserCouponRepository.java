package com.jifenke.lepluslive.coupon.repository;

import com.jifenke.lepluslive.coupon.domain.entities.LeJiaUserCoupon;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户领取的优惠券 Created by zhangwen on 16/11/29.
 */
public interface LeJiaUserCouponRepository extends JpaRepository<LeJiaUserCoupon, Long> {

}
