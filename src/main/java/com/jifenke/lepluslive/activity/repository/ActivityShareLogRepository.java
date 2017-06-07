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
   * @return 1=已被邀请过|0=未被邀请过
   */
  @Query(value = "SELECT id FROM activity_share_log WHERE  be_le_jia_user_id=?1", nativeQuery = true)
  List<Object[]> findLogByLeJiaUser(Long id);

  /**
   * 判断今日邀请成功人数是否已达上限  17/6/7
   */
  @Query(value = "SELECT SUM(1) FROM activity_share_log WHERE le_jia_user_id = ?1  AND create_date BETWEEN DATE_FORMAT(NOW(),'%Y-%m-%d') AND DATE_ADD(DATE_FORMAT(NOW(),'%Y-%m-%d'),INTERVAL 1 DAY)", nativeQuery = true)
  Integer countShareTimesToday(Long userId);

}
