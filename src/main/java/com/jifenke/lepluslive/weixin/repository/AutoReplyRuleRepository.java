package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.AutoReplyRule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wcg on 16/3/21.
 */
public interface AutoReplyRuleRepository extends JpaRepository<AutoReplyRule,Long>{

  List<AutoReplyRule> findOneByReplyType(String replyType);

  List<AutoReplyRule> findOneByKeyword(String keyword);

}
