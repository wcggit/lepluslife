package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityRockLog;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.repository.ActivityRockLogRepository;
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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/8/18.
 */
@Service
@Transactional(readOnly = true)
public class ActivityRockLogService {


  @Inject
  private ActivityRockLogRepository activityRockLogRepository;

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

  //添加摇一摇数据
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public HashMap<String, Integer> addRockLogAndScore(String token, LeJiaUserInfo leJiaUserInfo)
      throws Exception {
    Date date = new Date();
    String currDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
    //查询今日是否摇一摇已达三次
    int flag;
    if (currDate.equals(leJiaUserInfo.getRockDate())) {
      flag = 3 - leJiaUserInfo.getRockTime();
      if (flag > 0) {
        leJiaUserInfo.setRockTime(leJiaUserInfo.getRockTime() + 1);
      }
    } else {
      flag = 3;
      leJiaUserInfo.setRockDate(currDate);
      leJiaUserInfo.setRockTime(1);
    }
    if (flag != 0) { //今日没有摇一摇或不足三次,添加摇一摇记录，积分、红包及其记录，LeJiaUserInfo更新rock积分和红包
      //随机获取本次摇一摇的红包和积分
      HashMap<String, Integer> map = getScoreByRock();
      Integer numberA = map.get("a");
      Integer numberB = map.get("b");
      //保存本次摇一摇的红包和积分
      ActivityRockLog rockLog = new ActivityRockLog();
      rockLog.setRockTime(currDate);
      rockLog.setCreateDate(date);
      rockLog.setScoreA(numberA);
      rockLog.setScoreB(numberB);
      rockLog.setToken(token);
      rockLog.setLeJiaUserInfo(leJiaUserInfo);
      activityRockLogRepository.save(rockLog);
      ScoreA scoreA = null;
      ScoreB scoreB = null;
      LeJiaUser leJiaUser = leJiaUserInfo.getLeJiaUser();
      if (leJiaUser != null) {
        scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
        scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
      }
      if (scoreA != null && scoreB != null) {
        //SportUser更新总积分和红包及次数
        leJiaUserInfo.setRockA(leJiaUserInfo.getRockA() + numberA);
        leJiaUserInfo.setRockB(leJiaUserInfo.getRockB() + numberB);
        leJiaUserInfoRepository.save(leJiaUserInfo);
        if (numberA > 0) {
          //添加红包
          scoreA.setTotalScore(scoreA.getTotalScore() + numberA);
          scoreA.setScore(scoreA.getScore() + numberA);
          scoreA.setLastUpdateDate(date);
          scoreARepository.save(scoreA);
          //添加红包记录
          ScoreADetail aDetail = new ScoreADetail();
          aDetail.setNumber(numberA.longValue());
          aDetail.setScoreA(scoreA);
          aDetail.setOrigin(7);
          aDetail.setOperate("摇一摇得鼓励金");
          scoreADetailRepository.save(aDetail);
        }
        if (numberB > 0) {
          //添加积分
          scoreB.setTotalScore(scoreB.getTotalScore() + numberB);
          scoreB.setScore(scoreB.getScore() + numberB);
          scoreB.setLastUpdateDate(date);
          scoreBRepository.save(scoreB);
          //添加积分记录
          ScoreBDetail bDetail = new ScoreBDetail();
          bDetail.setOrigin(7);
          bDetail.setOperate("摇一摇得积分");
          bDetail.setNumber(numberB.longValue());
          bDetail.setScoreB(scoreB);
          scoreBDetailRepository.save(bDetail);
        }

        //返回当前摇一摇获得的积分和红包及总计获得的
        map.put("totalA", leJiaUserInfo.getRockA());
        map.put("totalB", leJiaUserInfo.getRockB());
        map.put("times", flag - 1);
        return map;
      } else {
        throw new RuntimeException();
      }
    } else {
      return null;
    }
  }

  private HashMap<String, Integer> getScoreByRock() {
    HashMap<String, Integer> map = new HashMap<>();
    double random = Math.random();
    if (random < 0.7) {
      int a = (int) (Math.random() * 2) + 1;
      map.put("a", a);
      map.put("b", 0);
    } else if (0.7 <= random && random < 0.96) {
      int a = (int) (Math.random() * 3) + 2;
      map.put("a", a);
      map.put("b", 1);
    } else if (0.96 <= random && random < 0.99) {
      int a = (int) (Math.random() * 3) + 4;
      map.put("a", a);
      map.put("b", 2);
    } else {
      int a = (int) (Math.random() * 5) + 6;
      map.put("a", a);
      map.put("b", 3);
    }
    return map;
  }

}
