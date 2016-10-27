package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.domain.entities.ActivityJoinLog;
import com.jifenke.lepluslive.activity.repository.ActivityJoinLogRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 参加活动记录 Created by zhangwen 16/8/8.
 */
@Service
@Transactional(readOnly = true)
public class ActivityJoinLogService {

  @Inject
  private ActivityJoinLogRepository activityJoinLogRepository;

  //查找是否参加过永久或分享活动
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityJoinLog findLogBySubActivityAndOpenId(Integer type, WeiXinUser user) {
    List<ActivityJoinLog>
        list =
        activityJoinLogRepository.findByTypeAndUser(type, user);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 查找是否参加过某个临时页面活动 16/10/18
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityJoinLog findLogByTypeAndUser(Integer type, Long activityId, WeiXinUser user) {
    List<ActivityJoinLog>
        list =
        activityJoinLogRepository.findByTypeAndActivityIdAndUser(type, activityId, user);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  //添加永久关注二维码活动记录
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void addCodeBurseLog(ActivityCodeBurse codeBurse, WeiXinUser user) {
    ActivityJoinLog joinLog = new ActivityJoinLog();
    joinLog.setActivityId(codeBurse.getId());
    joinLog.setDetail(codeBurse.getSingleMoney() + "");
    joinLog.setOpenId(user.getOpenId());
    joinLog.setType(codeBurse.getType());
    joinLog.setUser(user);
    activityJoinLogRepository.save(joinLog);
  }

  //添加默认关注送红包记录
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void addCodeBurseLogByDefault(WeiXinUser user, int defaultScoreA) {
    ActivityJoinLog joinLog = new ActivityJoinLog();
    joinLog.setActivityId(0L);
    joinLog.setDetail(defaultScoreA + "");
    joinLog.setOpenId(user.getOpenId());
    joinLog.setType(0);
    joinLog.setUser(user);
    activityJoinLogRepository.save(joinLog);
  }

  /**
   * 添加参与记录  16/10/18
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void addLog(WeiXinUser user, Integer scoreA, Integer scoreB, Long activityId,
                     Integer type) {
    ActivityJoinLog joinLog = new ActivityJoinLog();
    joinLog.setActivityId(activityId);
    String detail = "";
    if (scoreA != null) {
      detail += scoreA;
    } else {
      detail += "0";
    }
    if (scoreB != null) {
      detail += "_" + scoreB;
    }
    joinLog.setDetail(detail);
    joinLog.setOpenId(user.getOpenId());
    joinLog.setType(type);
    joinLog.setUser(user);
    activityJoinLogRepository.save(joinLog);
  }

}
