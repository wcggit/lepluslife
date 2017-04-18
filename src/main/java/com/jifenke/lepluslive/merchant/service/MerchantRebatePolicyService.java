package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantRebatePolicy;
import com.jifenke.lepluslive.merchant.repository.MerchantRebatePolicyRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by xf on 16-11-9.
 */
@Service
@Transactional(readOnly = true)
public class MerchantRebatePolicyService {

  @Inject
  private MerchantRebatePolicyRepository repository;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public MerchantRebatePolicy findByMerchant(Long merchantId) {
    return repository.findByMerchantId(merchantId);
  }

}
