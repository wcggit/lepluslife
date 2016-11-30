package com.jifenke.lepluslive.coupon.repository;

import com.jifenke.lepluslive.coupon.domain.entities.MerchantCoupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商家优惠券 Created by zhangwen on 16/11/29.
 */
public interface MerchantCouponRepository extends JpaRepository<MerchantCoupon, Long> {

  /**
   * 获取某个商户的有效优惠券列表  2016/11/29
   *
   * @param merchantId 商户ID
   */
  @Query(value = "SELECT * FROM merchant_coupon WHERE merchant_id = ?1 AND state = 1 ORDER BY sid DESC", nativeQuery = true)
  List<MerchantCoupon> findByMerchantOrderBySidDesc(Long merchantId);

  /**
   * 分页查询附近商户的有效优惠券列表  2016/11/30
   *
   * @param lat      经度
   * @param lng      纬度
   * @param cityId   城市ID
   * @param startNum 起始记录
   * @param pageSize 每页显示条数
   */
  @Query(value = "SELECT c.* FROM merchant_coupon c INNER JOIN (SELECT id,ROUND(ASIN(SQRT(POW(SIN((?1 * PI() / 180 - m.lat * PI() / 180) / 2),2) + COS(?1 * PI() / 180) * COS(m.lat * PI() / 180) * POW(SIN((?2 * PI() / 180 - m.lng * PI() / 180) / 2),2))) * 1000000) AS distance FROM merchant m WHERE city_id = ?3) mm ON c.merchant_id = mm.id WHERE c.state = 1 ORDER BY mm.distance ASC,c.sid DESC LIMIT ?4,?5", nativeQuery = true)
  List<MerchantCoupon> findByDistance(Double lat, Double lng, Long cityId, Integer startNum,
                                      Integer pageSize);
}
