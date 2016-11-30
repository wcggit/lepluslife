package com.jifenke.lepluslive.coupon.service;

import com.jifenke.lepluslive.coupon.repository.LeJiaUserCouponRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 用户领取的优惠券 Created by zhangwen on 2016/11/29.
 */
@Service
@Transactional(readOnly = true)
public class LeJiaUserCouponService {

  @Inject
  private LeJiaUserCouponRepository leJiaUserCouponRepository;

}
