package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityShareLog;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.repository.ActivityShareLogRepository;
import com.jifenke.lepluslive.activity.repository.LeJiaUserInfoRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * APP分享邀请记录 Created by zhangwen 16/8/8.
 */
@Service
@Transactional(readOnly = true)
public class ActivityShareLogService {


  @Inject
  private ActivityShareLogRepository activityShareLogRepository;

  @Inject
  private WeiXinUserRepository weiXinUserRepository;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private LeJiaUserRepository leJiaUserRepository;


  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private LeJiaUserInfoRepository leJiaUserInfoRepository;

  @Inject
  private LeJiaUserInfoService leJiaUserInfoService;

  /**
   * 判断是否被邀请过 16/09/08
   *
   * @param id 被邀请人id
   * @return 1=已被邀请过|0=未被邀请过
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public int findLogByLeJiaUser(Long id) {
    List<Object[]> list = activityShareLogRepository.findLogByLeJiaUser(id);
    if (list != null && list.size() > 0) {
      return 1;
    }
    return 0;
  }

  /**
   * APP分享活动送金币 16/09/08
   *
   * @param weiXinUser  被邀请人
   * @param token       邀请人token
   * @param phoneNumber 被邀请人手机
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void giveScoreByShare(WeiXinUser weiXinUser, String token, String phoneNumber)
      throws Exception {
    //获取红包积分奖励规则
    String[] rules = dictionaryService.findDictionaryById(27L).getValue().split("_");
    int UA = Integer.valueOf(rules[0]); //邀请人金币
    int BeUA = Integer.valueOf(rules[2]); //被邀请人金币
    //被邀请人
    LeJiaUser beLeJiaUser = weiXinUser.getLeJiaUser();
    ScoreC beScoreC = scoreCService.findScoreCByLeJiaUser(beLeJiaUser);
    //邀请人
    LeJiaUser leJiaUser = leJiaUserRepository.findByUserSid(token);
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);
    Date date = new Date();
    try {
      //邀请人
      scoreCService.saveScoreC(scoreC, 1, (long) UA);
      scoreCService
          .saveScoreCDetail(scoreC, 1, (long) UA, 8, "分享得鼓励金",
                            beLeJiaUser.getUserSid());
      //邀请人邀请总额和人数修改
      LeJiaUserInfo info = leJiaUserInfoService.findByLeJiaUser(leJiaUser);
      if (info != null) {
        info.setInviteA((info.getInviteA() == null ? 0 : info.getInviteA()) + UA);
        info.setTotalInvite(info.getTotalInvite() + 1);
        leJiaUserInfoRepository.save(info);
      }

      //被邀请人
      beLeJiaUser.setPhoneNumber(phoneNumber);
      beLeJiaUser.setPhoneBindDate(date);
      leJiaUserRepository.save(beLeJiaUser);
      scoreCService.saveScoreC(beScoreC, 1, (long) BeUA);
      scoreCService
          .saveScoreCDetail(beScoreC, 1, (long) BeUA, 8, "分享注册得鼓励金",
                            leJiaUser.getUserSid());
      weiXinUser.setState(1);
      weiXinUser.setStateDate(date);
      weiXinUserRepository.save(weiXinUser);

      //添加分享记录
      ActivityShareLog shareLog = new ActivityShareLog();
      shareLog.setBeLeJiaUser(beLeJiaUser);
      shareLog.setBeScoreA(BeUA);
      shareLog.setBeScoreB(0);
      shareLog.setCreateDate(date);
      shareLog.setLeJiaUser(leJiaUser);
      shareLog.setScoreA(UA);
      shareLog.setScoreB(0);
      shareLog.setToken(token);
      activityShareLogRepository.save(shareLog);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
