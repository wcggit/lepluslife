package com.jifenke.lepluslive.statistics.repository;

import com.jifenke.lepluslive.statistics.domain.entities.VisitLog;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 操作日志 Created by wcg on 16/3/18.
 */
public interface VisitLogRepository extends JpaRepository<VisitLog, String> {

}
