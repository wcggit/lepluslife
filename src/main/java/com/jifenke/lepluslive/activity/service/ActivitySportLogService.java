package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivitySportLog;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.repository.ActivitySportLogRepository;
import com.jifenke.lepluslive.activity.repository.LeJiaUserInfoRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.repository.DictionaryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/8/18.
 */
@Service
@Transactional(readOnly = true)
public class ActivitySportLogService {

  @Inject
  private ActivitySportLogRepository activitySportLogRepository;

  @Inject
  private LeJiaUserInfoRepository leJiaUserInfoRepository;

  @Inject
  private ScoreADetailRepository scoreADetailRepository;

  @Inject
  private ScoreBDetailRepository scoreBDetailRepository;

  @Inject
  private ScoreARepository scoreARepository;

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private DictionaryRepository dictionaryRepository;

  //判断今天运动数据是否已添加
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public int findBySportUserAndCurrDate(LeJiaUserInfo leJiaUserInfo, String currDate) {
    List<ActivitySportLog> list = activitySportLogRepository.findByLeJiaUserInfoAndSportTime(
        leJiaUserInfo, currDate);
    if (list != null && list.size() > 0) {
      return 1;
    } else {
      return 0;
    }
  }

  //添加运动数据
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void addSportLogAndScore(String token, LeJiaUserInfo leJiaUserInfo, Integer distance)
      throws Exception {
    Date date = new Date();
    String currDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
    //查询今日是否已添加运动数据
    int flag = findBySportUserAndCurrDate(leJiaUserInfo, currDate);
    if (flag == 0) { //没有,添加跑步记录，积分、红包及其记录，SportUser更新总积分和红包
      ActivitySportLog activitySportLog = new ActivitySportLog();
      activitySportLog.setCreateDate(date);
      activitySportLog.setToken(token);
      activitySportLog.setDistance(distance);
      activitySportLog.setSportTime(currDate);
      activitySportLog.setLeJiaUserInfo(leJiaUserInfo);

      //判断运动距离应获取多少红包和积分
      String currRule = getScoreByDistance(distance);
      if (!"0".equals(currRule)) {
        activitySportLog.setGetScore(1);
        ScoreA scoreA = null;
        ScoreB scoreB = null;
        LeJiaUser leJiaUser = leJiaUserInfo.getLeJiaUser();
        if (leJiaUser != null) {
          scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
          scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
        }
        if (scoreA != null && scoreB != null) {
          String[] rules = currRule.split("_");
          Integer numberA = Integer.valueOf(rules[0]);
          Integer numberB = Integer.valueOf(rules[1]);
          //添加跑步记录
          activitySportLog.setScoreA(numberA);
          activitySportLog.setScoreB(numberB);
          activitySportLogRepository.save(activitySportLog);
          //SportUser更新总积分和红包及总距离
          leJiaUserInfo.setTotalSport(leJiaUserInfo.getTotalSport() + distance);
          leJiaUserInfo.setSportA(leJiaUserInfo.getSportA() + numberA);
          leJiaUserInfo.setSportB(leJiaUserInfo.getSportB() + numberB);
          leJiaUserInfoRepository.save(leJiaUserInfo);
          //添加红包
          scoreA.setTotalScore(scoreA.getTotalScore() + numberA);
          scoreA.setScore(scoreA.getScore() + numberA);
          scoreA.setLastUpdateDate(date);
          scoreARepository.save(scoreA);
          //添加红包记录
          ScoreADetail aDetail = new ScoreADetail();
          aDetail.setNumber(numberA.longValue());
          aDetail.setScoreA(scoreA);
          aDetail.setOrigin(6);
          aDetail.setOperate("运动得红包");
          scoreADetailRepository.save(aDetail);
          //添加积分
          scoreB.setTotalScore(scoreB.getTotalScore() + numberB);
          scoreB.setLastUpdateDate(date);
          scoreB.setScore(scoreB.getScore() + numberB);
          scoreBRepository.save(scoreB);
          //添加积分记录
          ScoreBDetail bDetail = new ScoreBDetail();
          bDetail.setOrigin(6);
          bDetail.setOperate("运动得积分");
          bDetail.setNumber(numberB.longValue());
          bDetail.setScoreB(scoreB);
          scoreBDetailRepository.save(bDetail);
        } else {
          throw new RuntimeException();
        }
      } else {
        activitySportLogRepository.save(activitySportLog);
      }
    }
  }

  private String getScoreByDistance(Integer distance) {
    String rule = dictionaryRepository.findOne(19L).getValue();
    String[] str00 = rule.split("=");
    String[] distanceRule = str00[0].split("_");
    String[] scoreRule = str00[1].split("-");
    int flag = 0;
    String currRule = "";
    for (int i = 0; i < distanceRule.length; i++) {
      if (((int) distance) < Integer.valueOf(distanceRule[i])) {
        flag = 1;
        currRule = scoreRule[i];
        break;
      }
    }
    if (flag == 0) {
      currRule = scoreRule[scoreRule.length - 1];
    }
    return currRule;
  }

}
