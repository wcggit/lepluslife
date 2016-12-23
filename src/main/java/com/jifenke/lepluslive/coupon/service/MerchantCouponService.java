package com.jifenke.lepluslive.coupon.service;

import com.jifenke.lepluslive.coupon.domain.entities.MerchantCoupon;
import com.jifenke.lepluslive.coupon.repository.MerchantCouponRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreBService;

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
 * 商户优惠券 Created by zhangwen on 2016/11/29.
 */
@Service
@Transactional(readOnly = true)
public class MerchantCouponService {

  @Inject
  private MerchantCouponRepository merchantCouponRepository;

  @Inject
  private LeJiaUserCouponService leJiaUserCouponService;

  @Inject
  private ScoreBService scoreBService;

  /**
   * 获取某个商家的优惠券列表 16/11/29
   *
   * @param merchantId 商家ID
   */
  public List<Map<String, Object>> findByMerchant(Long merchantId) {
    List<Object[]> list = merchantCouponRepository.findByMerchantOrderBySidDesc(merchantId);
    return result(list);
  }

  /**
   * 分页查询附近商户的有效优惠券列表  2016/11/30
   *
   * @param lat      经度
   * @param lng      纬度
   * @param cityId   城市ID
   * @param startNum 起始记录
   */
  public List<Map<String, Object>> findByDistance(Double lat, Double lng, Long cityId,
                                                  Integer startNum) {
    List<Object[]> list = merchantCouponRepository.findByDistance(lat, lng, cityId, startNum, 10);
    return result(list);
  }

  /**
   * 领取优惠券  2016/11/30
   *
   * @param leJiaUser 用户
   * @param couponId  优惠券ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String getCoupon(LeJiaUser leJiaUser, Long couponId) throws Exception {
    MerchantCoupon coupon = merchantCouponRepository.findOne(couponId);
    if (coupon == null) {
      return "10001";
    }
    if (coupon.getState() == 0) {
      return "10002";
    }
    if (coupon.getRepository() == 0) {
      return "10003";
    }
    Date date = new Date();
    if (coupon.getEndDate().getTime() <= date.getTime()) {
      return "10004";
    }
    Long count = leJiaUserCouponService.countByUserAndCoupon(leJiaUser.getId(), couponId);
    if (count != null && count > 0) {
      return "10005";
    }
    try {
      //判断领取方式
      if (coupon.getPayType() == 1) { //花积分领取
        ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
        if (scoreB.getScore() < coupon.getScoreB()) {//积分是否足够
          return "6002";
        }
        //减用户的积分及添加积分记录
        scoreBService.editScoreB(scoreB, 0, coupon.getScoreB());
        scoreBService
            .addScoreBDetail(scoreB, 0, coupon.getScoreB().longValue(), 15001, "优惠券兑换", null);
      }
      //领取优惠券
      leJiaUserCouponService.getCoupon(leJiaUser, coupon);
      //修改优惠券库存和销量
      coupon.setRepository(coupon.getRepository() - 1);
      coupon.setGetNum(coupon.getGetNum() + 1);
      merchantCouponRepository.save(coupon);
      return "200";
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 返回数据封装  2016/12/02
   */
  private List<Map<String, Object>> result(List<Object[]> list) {
    List<Map<String, Object>> mapList = new ArrayList<>();
    if (list != null) {
      for (Object[] o : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", o[0]);
        map.put("beginDate", o[1]);
        map.put("endDate", o[2]);
        map.put("describe1", o[3]);
        map.put("describe2", o[4]);
        map.put("title", o[5]);
        map.put("payType", o[6]);
        map.put("scoreB", o[7]);
        map.put("merchantName", o[8]);
        map.put("picture", o[9]);
        mapList.add(map);
      }
    }
    return mapList;
  }

}
