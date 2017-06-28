package com.jifenke.lepluslive.score.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.repository.ActivityCodeBurseRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/18.
 */
@Service
@Transactional(readOnly = true)
public class ScoreAService {

  @Inject
  private ScoreARepository scoreARepository;

  @Inject
  private ScoreADetailRepository scoreADetailRepository;

  @Inject
  private ActivityCodeBurseRepository activityCodeBurseRepository;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ScoreA findScoreAByLeJiaUser(LeJiaUser leJiaUser) {
    List<ScoreA> aList = scoreARepository.findByLeJiaUser(leJiaUser);
    if (aList != null && aList.size() > 0) {
      return aList.get(0);
    }
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void paySuccess(LeJiaUser leJiaUser, Long payBackScore, String orderSid) {
    ScoreA scoreA = findScoreAByLeJiaUser(leJiaUser);
    scoreA.setScore(scoreA.getScore() + payBackScore);
    scoreA.setTotalScore(scoreA.getTotalScore() + payBackScore);
    ScoreADetail scoreADetail = new ScoreADetail();
    scoreADetail.setOperate("乐+商城返鼓励金");
    scoreADetail.setOrigin(1);
    scoreADetail.setOrderSid(orderSid);
    scoreADetail.setScoreA(scoreA);
    scoreADetail.setNumber(payBackScore);
    scoreADetailRepository.save(scoreADetail);
    scoreARepository.save(scoreA);
  }

  /**
   * 根据scoreA查询红包明细列表
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ScoreADetail> findAllScoreADetailByScoreA(ScoreA scoreA) {
    return scoreADetailRepository.findAllByScoreAOrderByIdDesc(scoreA);
  }

  //活动派发红包
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public int giveScoreAByActivity(ActivityCodeBurse codeBurse, LeJiaUser leJiaUser) {
    try {
      ScoreA scoreA = findScoreAByLeJiaUser(leJiaUser);
      Long payBackScore = Long.valueOf(codeBurse.getSingleMoney() + "");
      scoreA.setScore(scoreA.getScore() + payBackScore);
      scoreA.setTotalScore(scoreA.getTotalScore() + payBackScore);
      ScoreADetail scoreADetail = new ScoreADetail();
      scoreADetail.setOperate("关注送鼓励金");
      scoreADetail.setOrigin(5);
      scoreADetail.setOrderSid(codeBurse.getType() + "_" + codeBurse.getId());
      scoreADetail.setScoreA(scoreA);
      scoreADetail.setNumber(payBackScore);
      scoreADetailRepository.save(scoreADetail);
      scoreARepository.save(scoreA);

      //修改红包领取次数，金额
      codeBurse.setTotalNumber(codeBurse.getTotalNumber() + 1);
      codeBurse.setTotalMoney(codeBurse.getTotalMoney() + codeBurse.getSingleMoney());
      activityCodeBurseRepository.save(codeBurse);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
    return 1;
  }

  /**
   * 添加红包 16/10/18
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public int giveScoreAByDefault(LeJiaUser leJiaUser, int defaultScoreA, String operate,
                                 Integer origin, String orderSid) {
    ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser).get(0);
    try {
      scoreA.setScore(scoreA.getScore() + defaultScoreA);
      scoreA.setTotalScore(scoreA.getTotalScore() + defaultScoreA);
      scoreA.setLastUpdateDate(new Date());

      scoreARepository.save(scoreA);

      ScoreADetail scoreADetail = new ScoreADetail();
      scoreADetail.setNumber(Long.valueOf(String.valueOf(defaultScoreA)));
      scoreADetail.setScoreA(scoreA);
      scoreADetail.setOperate(operate);
      scoreADetail.setOrigin(origin);
      scoreADetail.setOrderSid(orderSid);
      scoreADetailRepository.save(scoreADetail);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
    return 1;
  }

  /**
   * 保存鼓励金账户  17/6/20
   *
   * @param scoreA 鼓励金账户
   * @param type   1=增加|0=减少
   * @param val    增加或减少的鼓励金
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void saveScore(ScoreA scoreA, int type, Long val) throws Exception {
    Date date = new Date();
    scoreA.setLastUpdateDate(date);
    try {
      if (type == 0) {
        scoreA.setScore(scoreA.getScore() - val);
      } else {
        scoreA.setScore(scoreA.getScore() + val);
        scoreA.setTotalScore(scoreA.getTotalScore() + val);
      }
      scoreARepository.save(scoreA);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 添加用户鼓励金变动明细   2017/2/20
   *
   * @param scoreA   鼓励金账户
   * @param state    1=加鼓励金|0=减鼓励金
   * @param number   更改鼓励金的数额
   * @param origin   变动来源
   * @param operate  变动文字描述
   * @param orderSid 对应的订单号(可为空)
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void saveScoreDetail(ScoreA scoreA, Integer state, Long number, Integer origin,
                              String operate,
                              String orderSid) throws Exception {
    try {
      ScoreADetail detail = new ScoreADetail();
      detail.setOperate(operate);
      detail.setOrigin(origin);
      detail.setOrderSid(orderSid);
      detail.setScoreA(scoreA);
      if (state == 0) {
        detail.setNumber(-number);
      } else {
        detail.setNumber(number);
      }
      scoreADetailRepository.save(detail);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
