package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityRockLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangwen on 16/8/18.
 */
public interface ActivityRockLogRepository extends JpaRepository<ActivityRockLog, String> {
}
