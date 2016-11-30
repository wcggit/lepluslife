package com.jifenke.lepluslive.coupon.service;

import com.jifenke.lepluslive.coupon.domain.entities.MerchantCoupon;
import com.jifenke.lepluslive.coupon.repository.MerchantCouponRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 商户优惠券 Created by zhangwen on 2016/11/29.
 */
@Service
@Transactional(readOnly = true)
public class MerchantCouponService {

  @Inject
  private MerchantCouponRepository merchantCouponRepository;

  /**
   * 获取某个商家的优惠券列表 16/11/29
   *
   * @param merchantId 商家ID
   */
  public List<MerchantCoupon> findByMerchant(Long merchantId) {
    return merchantCouponRepository.findByMerchantOrderBySidDesc(merchantId);
  }

  /**
   * 分页查询附近商户的有效优惠券列表  2016/11/30
   *
   * @param lat      经度
   * @param lng      纬度
   * @param cityId   城市ID
   * @param startNum 起始记录
   */
  public List<MerchantCoupon> findByDistance(Double lat, Double lng, Long cityId,
                                             Integer startNum) {
    return merchantCouponRepository.findByDistance(lat, lng, cityId, startNum, 10);
  }

}
