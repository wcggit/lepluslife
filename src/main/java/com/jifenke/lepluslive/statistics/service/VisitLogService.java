package com.jifenke.lepluslive.statistics.service;

import com.jifenke.lepluslive.statistics.domain.entities.VisitLog;
import com.jifenke.lepluslive.statistics.repository.VisitLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 页面访问记录/按钮点击记录等操作日志 Created by zhangwen on 2017/3/10.
 */
@Service
@Transactional(readOnly = true)
public class VisitLogService {

  @Inject
  private VisitLogRepository repository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveLog(VisitLog visitLog) {
    repository.saveAndFlush(visitLog);
  }

}
