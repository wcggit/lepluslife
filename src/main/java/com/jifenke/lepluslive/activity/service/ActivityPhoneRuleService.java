package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneRule;
import com.jifenke.lepluslive.activity.repository.ActivityPhoneRuleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;


/**
 * 手机话费规则相关 Created by zhangwen on 2016/10/26.
 */
@Service
public class ActivityPhoneRuleService {

  @Inject
  private ActivityPhoneRuleRepository repository;

  /**
   * 根据ID查询充值规则 16/10/27
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityPhoneRule findById(Long id) {
    return repository.findOne(id);
  }

  /**
   * 获取所有上线话费产品 16/11/01
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ActivityPhoneRule> findAllByState(int state) {
    return repository.findByState(state);
  }

  /**
   * 特惠产品支付成功减库存 16/10/31
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public synchronized void reduceRepository(ActivityPhoneRule rule) throws Exception {
    try {
      rule.setRepository(rule.getRepository() - 1);
      repository.saveAndFlush(rule);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

}
