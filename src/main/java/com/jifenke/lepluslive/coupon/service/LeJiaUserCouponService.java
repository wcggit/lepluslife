package com.jifenke.lepluslive.coupon.service;

import com.jifenke.lepluslive.coupon.domain.entities.LeJiaUserCoupon;
import com.jifenke.lepluslive.coupon.domain.entities.MerchantCoupon;
import com.jifenke.lepluslive.coupon.repository.LeJiaUserCouponRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * 用户领取的优惠券 Created by zhangwen on 2016/11/29.
 */
@Service
@Transactional(readOnly = true)
public class LeJiaUserCouponService {

  @Inject
  private LeJiaUserCouponRepository leJiaUserCouponRepository;

  /**
   * 查看某个用户是否领取了某张时效内的券  16/11/30
   *
   * @param leJiaUserId 用户
   * @param couponId    优惠券
   */
  public Long countByUserAndCoupon(Long leJiaUserId, Long couponId) {
    return leJiaUserCouponRepository
        .countByLeJiaUserAndMerchantCoupon(leJiaUserId, couponId);
  }

  /**
   * 查看用户某一状态的优惠券列表  16/12/01
   *
   * @param leJiaUserId 用户
   * @param state       状态 1=待使用|2=已使用|3=已过期
   * @param currPage    当前页码
   */
  public List<Map<String, Object>> findByLeJiaUserAndState(Long leJiaUserId, Integer state,
                                                           Integer currPage) {
    List<Object[]> list = leJiaUserCouponRepository.findByLeJiaUserAndState(leJiaUserId, state,
                                                                            (currPage - 1) * 10,
                                                                            10);
    List<Map<String, Object>> mapList = new ArrayList<>();
    if (list != null) {
      for (Object[] o : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", o[0]);
        map.put("beginDate", o[1]);
        map.put("endDate", o[2]);
        map.put("payType", o[3]);
        map.put("useScore", o[4]);
        map.put("createDate", o[5]);
        map.put("useDate", o[6]);
        map.put("describe1", o[7]);
        map.put("describe2", o[8]);
        map.put("title", o[9]);
        map.put("merchantName", o[10]);
        map.put("picture", o[11]);
        mapList.add(map);
      }
    }
    return mapList;
  }

  /**
   * 领取优惠券时创建优惠券  16/12/01
   *
   * @param leJiaUser 用户
   * @param coupon    优惠券
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void getCoupon(LeJiaUser leJiaUser, MerchantCoupon coupon) throws Exception {
    try {
      LeJiaUserCoupon userCoupon = new LeJiaUserCoupon();
      userCoupon.setLeJiaUser(leJiaUser);
      userCoupon.setBeginDate(coupon.getBeginDate());
      userCoupon.setEndDate(coupon.getEndDate());
      userCoupon.setMerchantName(coupon.getMerchantName());
      userCoupon.setPayType(coupon.getPayType());
      userCoupon.setUseScore(coupon.getScoreB());
      userCoupon.setMerchantCoupon(coupon);
      leJiaUserCouponRepository.save(userCoupon);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 核销优惠券  2016/11/30
   *
   * @param token    用户token
   * @param couponId 用户优惠券ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String useCoupon(String token, Long couponId) throws Exception {
    LeJiaUserCoupon coupon = leJiaUserCouponRepository.findOne(couponId);
    if (coupon == null) {
      return "10006";
    }
    if (!token.equals(coupon.getLeJiaUser().getUserSid())) {
      return "500";
    }
    if (coupon.getState() != 1) {
      return "10007";
    }
    Date date = new Date();
    if (coupon.getEndDate().getTime() < date.getTime()) {
      return "10004";
    }
    if (coupon.getBeginDate().getTime() > date.getTime()) {
      return "10008";
    }
    try {
      coupon.setState(2);
      coupon.setUseDate(date);
      leJiaUserCouponRepository.save(coupon);
      return "200";
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

}
