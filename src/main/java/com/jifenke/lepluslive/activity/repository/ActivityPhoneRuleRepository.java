package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneRule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/10/26.
 */
public interface ActivityPhoneRuleRepository extends JpaRepository<ActivityPhoneRule, Long> {

  /**
   * 获取所有上线话费产品  16/11/01
   *
   * @param state 产品状态
   */
  List<ActivityPhoneRule> findByState(Integer state);

}
