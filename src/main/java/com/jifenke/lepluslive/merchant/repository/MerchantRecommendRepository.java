package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantRecommend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by wcg on 16/3/25.
 */
public interface MerchantRecommendRepository extends JpaRepository<MerchantRecommend, Integer> {

  List<MerchantRecommend> findAllByStateOrderBySidAsc(Integer state);
}
