package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponRefundCode;
import com.jifenke.lepluslive.groupon.repository.GrouponRefundCodeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 退款单对应的团购码
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponRefundCodeService {

  @Inject
  private GrouponRefundCodeRepository repository;

  @Transactional(propagation = Propagation.REQUIRED)
  public void saveGrouponRefundCode(GrouponRefundCode grouponRefundCode) {
    repository.save(grouponRefundCode);
  }

}
