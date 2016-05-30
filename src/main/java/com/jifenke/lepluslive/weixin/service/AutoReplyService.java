package com.jifenke.lepluslive.weixin.service;


import com.jifenke.lepluslive.weixin.domain.entities.AutoReplyRule;
import com.jifenke.lepluslive.weixin.repository.AutoReplyRuleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/5/25.
 */
@Service
@Transactional(readOnly = true)
public class AutoReplyService {

  @Inject
  private AutoReplyRuleRepository autoReplyRuleRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<AutoReplyRule> findAllReplyRule() {
    return autoReplyRuleRepository.findAll();
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public AutoReplyRule findTextReplyById(Long id) {
    return autoReplyRuleRepository.findOne(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public AutoReplyRule findImageReplyById(Long id) {
    return autoReplyRuleRepository.findOne(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public AutoReplyRule findByReplyType(String replyType) {
    List<AutoReplyRule> replyRuleList = autoReplyRuleRepository.findOneByReplyType(replyType);
    if (replyRuleList.size() > 0) {
      return replyRuleList.get(0);
    } else {
      return null;
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public AutoReplyRule findByKeyword(String keyword) {
    List<AutoReplyRule> replyRuleList = autoReplyRuleRepository.findOneByKeyword(keyword);
    if (replyRuleList.size() > 0) {
      return replyRuleList.get(0);
    } else {
      return null;
    }
  }


}
