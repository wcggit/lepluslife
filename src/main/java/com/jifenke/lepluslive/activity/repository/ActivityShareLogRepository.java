package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityShareLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zhangwen on 16/9/2.
 */
public interface ActivityShareLogRepository extends JpaRepository<ActivityShareLog, String> {

  /**
   * 判断是否被邀请过 16/09/08
   *
   * @param id    被邀请人ID
   * @param token 邀请人token
   * @return 1=已被邀请过|0=未被邀请过
   */
  @Query(value = "SELECT id FROM activity_share_log WHERE token=?1 AND be_le_jia_user_id=?2", nativeQuery = true)
  List<Object[]> findLogByLeJiaUserAndToken(String token, Long id);

}
