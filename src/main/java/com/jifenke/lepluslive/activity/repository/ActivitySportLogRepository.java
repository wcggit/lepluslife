package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivitySportLog;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/8/18.
 */
public interface ActivitySportLogRepository extends JpaRepository<ActivitySportLog, String> {

  List<ActivitySportLog> findByLeJiaUserInfoAndSportTime(LeJiaUserInfo leJiaUserInfo, String sportTime);

}
