package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

import java.util.List;

/**
 * Created by zw on 16/04/27.
 */
public interface MerchantDetailRepository extends JpaRepository<MerchantDetail, Long> {

  /**
   * 商家轮播图
   */
  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  List<MerchantDetail> findAllByMerchant(Merchant merchant);

  //APP商家详情图  06/09/02
  @Query(value = "SELECT sid,picture FROM merchant_detail WHERE merchant_id=?1", nativeQuery = true)
  List<Object[]> findMerchantDetailsByMerchantId(Long id);

}
