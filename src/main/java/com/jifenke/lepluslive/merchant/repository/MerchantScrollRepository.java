package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/6/8.
 */
public interface MerchantScrollRepository extends JpaRepository<MerchantScroll, Long> {

  List<MerchantScroll> findAllByMerchant(Merchant merchant);

  //APP商家轮播图  06/09/02
  @Query(value = "SELECT sid,picture FROM merchant_scroll WHERE merchant_id=?1", nativeQuery = true)
  List<Object[]> findMerchantScrollsByMerchantId(Long id);
}
