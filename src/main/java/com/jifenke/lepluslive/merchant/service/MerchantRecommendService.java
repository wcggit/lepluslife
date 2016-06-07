package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantRecommend;
import com.jifenke.lepluslive.merchant.repository.MerchantRecommendRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 商家推荐管理 Created by zhangwen on 2016/5/5.
 */
@Service
@Transactional(readOnly = true)
public class MerchantRecommendService {

  @Inject
  private MerchantRecommendRepository recommendRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<MerchantRecommend> findAllMerchantRecommend() {
    return recommendRepository.findAllByStateOrderBySidAsc(1);
  }

}
