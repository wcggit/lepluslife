package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityInviteLog;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangwen on 16/9/2.
 */
public interface ActivityInviteLogRepository extends JpaRepository<ActivityInviteLog, String> {

}
