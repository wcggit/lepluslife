package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityJoinLog;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwen on 16/8/4.
 */
public interface ActivityJoinLogRepository extends JpaRepository<ActivityJoinLog, String> {

  Page findAll(Specification<ActivityJoinLog> whereClause, Pageable pageRequest);

  //查找是否参加过永久或分享裂变活动
  List<ActivityJoinLog> findByTypeAndOpenId(Integer type, String openId);

  /**
   * 查找是否参加过某个临时页面活动 16/10/18
   */
  List<ActivityJoinLog> findByTypeAndActivityIdAndUser(Integer type, Long activityId,
                                                       WeiXinUser user);
}
