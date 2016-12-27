package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityShareLog;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.repository.ActivityShareLogRepository;
import com.jifenke.lepluslive.activity.repository.LeJiaUserInfoRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
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
  private ScoreARepository scoreARepository;

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private ScoreADetailRepository scoreADetailRepository;

  @Inject
  private LeJiaUserRepository leJiaUserRepository;

  @Inject
  private ScoreBDetailRepository scoreBDetailRepository;

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
   * APP分享活动送红包和积分 16/09/08
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
    int UA = Integer.valueOf(rules[0]); //邀请人红包
    int UB = Integer.valueOf(rules[1]); //邀请人积分
    int BeUA = Integer.valueOf(rules[2]); //被邀请人红包
    int BeUB = Integer.valueOf(rules[3]); //被邀请人积分
    //被邀请人
    LeJiaUser beLeJiaUser = weiXinUser.getLeJiaUser();
    ScoreA beScoreA = scoreARepository.findByLeJiaUser(beLeJiaUser).get(0);
    ScoreB beScoreB = scoreBRepository.findByLeJiaUser(beLeJiaUser);
    //邀请人
    LeJiaUser leJiaUser = leJiaUserRepository.findByUserSid(token);
    ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser).get(0);
    ScoreB scoreB = scoreBRepository.findByLeJiaUser(leJiaUser);
    Date date = new Date();
    try {
      //邀请人
      scoreA.setScore(scoreA.getScore() + UA);
      scoreA.setTotalScore(scoreA.getTotalScore() + UA);
      scoreA.setLastUpdateDate(date);
      scoreARepository.save(scoreA);
      scoreB.setScore(scoreB.getScore() + UB);
      scoreB.setTotalScore(scoreB.getTotalScore() + UB);
      scoreB.setLastUpdateDate(date);
      scoreBRepository.save(scoreB);
      ScoreADetail scoreADetail = new ScoreADetail();
      scoreADetail.setNumber(Long.valueOf(String.valueOf(UA)));
      scoreADetail.setScoreA(scoreA);
      scoreADetail.setOperate("分享得红包");
      scoreADetail.setOrigin(8);
      scoreADetail.setOrderSid(beLeJiaUser.getUserSid());
      scoreADetailRepository.save(scoreADetail);
      ScoreBDetail scoreBDetail = new ScoreBDetail();
      scoreBDetail.setNumber(Long.valueOf(String.valueOf(UB)));
      scoreBDetail.setScoreB(scoreB);
      scoreBDetail.setOperate("分享得积分");
      scoreBDetail.setOrigin(8);
      scoreBDetail.setOrderSid(beLeJiaUser.getUserSid());
      scoreBDetailRepository.save(scoreBDetail);
      //邀请人邀请总额和人数修改
      LeJiaUserInfo info = leJiaUserInfoService.findByLeJiaUser(leJiaUser);
      if (info != null) {
        info.setInviteA(info.getInviteA() + UA);
        info.setInviteB(info.getInviteB() + UB);
        info.setTotalInvite(info.getTotalInvite() + 1);
        leJiaUserInfoRepository.save(info);
      }

      //被邀请人
      beLeJiaUser.setPhoneNumber(phoneNumber);
      leJiaUserRepository.save(beLeJiaUser);
      beScoreA.setScore(beScoreA.getScore() + BeUA);
      beScoreA.setTotalScore(beScoreA.getTotalScore() + BeUA);
      beScoreA.setLastUpdateDate(date);
      scoreARepository.save(beScoreA);
      beScoreB.setScore(beScoreB.getScore() + BeUB);
      beScoreB.setTotalScore(beScoreB.getTotalScore() + BeUB);
      beScoreB.setLastUpdateDate(date);
      scoreBRepository.save(beScoreB);
      ScoreADetail beScoreADetail = new ScoreADetail();
      beScoreADetail.setNumber(Long.valueOf(String.valueOf(BeUA)));
      beScoreADetail.setScoreA(beScoreA);
      beScoreADetail.setOperate("分享注册得红包");
      beScoreADetail.setOrigin(8);
      beScoreADetail.setOrderSid(leJiaUser.getUserSid());
      scoreADetailRepository.save(beScoreADetail);
      ScoreBDetail beScoreBDetail = new ScoreBDetail();
      beScoreBDetail.setNumber(Long.valueOf(String.valueOf(BeUB)));
      beScoreBDetail.setScoreB(beScoreB);
      beScoreBDetail.setOperate("分享注册得积分");
      beScoreBDetail.setOrigin(8);
      beScoreBDetail.setOrderSid(leJiaUser.getUserSid());
      scoreBDetailRepository.save(beScoreBDetail);
      weiXinUser.setHongBaoState(1);
      weiXinUser.setState(1);
      weiXinUser.setStateDate(date);
      weiXinUserRepository.save(weiXinUser);

      //添加分享记录
      ActivityShareLog shareLog = new ActivityShareLog();
      shareLog.setBeLeJiaUser(beLeJiaUser);
      shareLog.setBeScoreA(BeUA);
      shareLog.setBeScoreB(BeUB);
      shareLog.setCreateDate(date);
      shareLog.setLeJiaUser(leJiaUser);
      shareLog.setScoreA(UA);
      shareLog.setScoreB(UB);
      shareLog.setToken(token);
      activityShareLogRepository.save(shareLog);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
