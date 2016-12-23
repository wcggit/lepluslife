package com.jifenke.lepluslive.score.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 积分账户相关 Created by zhangwen on 16/12/01.
 */
@Service
@Transactional(readOnly = true)
public class ScoreBService {

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private ScoreBDetailRepository scoreBDetailRepository;

  /**
   * 添加或减少用户积分   2016/12/01
   *
   * @param scoreB 积分账户
   * @param state  1=加积分|0=减积分
   * @param number 更改积分的数额
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void editScoreB(ScoreB scoreB, Integer state, Integer number) throws Exception {
    try {
      if (state == 0) {
        if (scoreB.getScore() - number >= 0) {
          scoreB.setScore(scoreB.getScore() - number);
        } else {
          throw new RuntimeException();
        }
      } else {
        scoreB.setScore(scoreB.getScore() + number);
        scoreB.setTotalScore(scoreB.getTotalScore() + number);
      }
      scoreBRepository.save(scoreB);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 添加用户积分变动明细   2016/12/01
   *
   * @param scoreB   积分账户
   * @param state    1=加积分|0=减积分
   * @param number   更改积分的数额
   * @param origin   变动来源
   * @param operate  变动文字描述
   * @param orderSid 对应的订单号(可为空)
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void addScoreBDetail(ScoreB scoreB, Integer state, Long number, Integer origin,
                              String operate,
                              String orderSid) throws Exception {
    try {
      ScoreBDetail scoreBDetail = new ScoreBDetail();
      scoreBDetail.setOperate(operate);
      scoreBDetail.setOrigin(origin);
      scoreBDetail.setOrderSid(orderSid);
      scoreBDetail.setScoreB(scoreB);
      if (state == 0) {
        scoreBDetail.setNumber(-number);
      } else {
        scoreBDetail.setNumber(number);
      }
      scoreBDetailRepository.save(scoreBDetail);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ScoreB findScoreBByWeiXinUser(LeJiaUser leJiaUser) {
    return scoreBRepository.findByLeJiaUser(leJiaUser);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccess(LeJiaUser leJiaUser, Long totalScore, Integer origin, String operate,
                         String orderSid) {
    ScoreB scoreB = findScoreBByWeiXinUser(leJiaUser);

    if (scoreB.getScore() - totalScore >= 0) {
      scoreB.setScore(scoreB.getScore() - totalScore);
      ScoreBDetail scoreBDetail = new ScoreBDetail();
      scoreBDetail.setOperate(operate);
      scoreBDetail.setOrigin(origin);
      scoreBDetail.setOrderSid(orderSid);
      scoreBDetail.setScoreB(scoreB);
      scoreBDetail.setNumber(-totalScore);
      scoreBDetailRepository.save(scoreBDetail);
      scoreBRepository.save(scoreB);
    } else {
      throw new RuntimeException("积分不足");
    }

  }

  /**
   * 根据scoreB查询积分明细列表
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ScoreBDetail> findAllScoreBDetailByScoreB(ScoreB scoreB) {
    return scoreBDetailRepository.findAllByScoreBOrderByIdDesc(scoreB);
  }
}
