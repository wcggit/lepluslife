package com.jifenke.lepluslive.score.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.domain.entities.ScoreCDetail;
import com.jifenke.lepluslive.score.repository.ScoreCDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreCRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

/**
 * 金币账户 Created by zhangwen on 17/2/20.
 */
@Service
@Transactional(readOnly = true)
public class ScoreCService {

  @Inject
  private ScoreCRepository repository;

  @Inject
  private ScoreCDetailRepository scoreCDetailRepository;

  /**
   * 查找金币账户  17/2/20
   *
   * @param leJiaUser 用户
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public ScoreC findScoreCByLeJiaUser(LeJiaUser leJiaUser) {
    if (leJiaUser != null) {
      Optional<ScoreC> optional = repository.findByLeJiaUser(leJiaUser);
      if (optional.isPresent()) {
        return optional.get();
      }
      ScoreC scoreC = new ScoreC();
      scoreC.setLeJiaUser(leJiaUser);
      scoreC.setLastUpdateDate(new Date());
      repository.saveAndFlush(scoreC);
      return scoreC;
    }
    return null;
  }

  /**
   * 保存金币账户  17/2/20
   *
   * @param scoreC 金币账户
   * @param type   1=增加|0=减少
   * @param val    增加或减少的金币
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveScoreC(ScoreC scoreC, int type, Long val) throws Exception {
    Date date = new Date();
    scoreC.setLastUpdateDate(date);
    try {
      if (type == 1) {
        scoreC.setScore(scoreC.getScore() + val);
        scoreC.setTotalScore(scoreC.getTotalScore() + val);
      } else {
        scoreC.setScore(scoreC.getScore() - val);
      }
      repository.save(scoreC);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  /**
   * 添加用户金币变动明细   2017/2/20
   *
   * @param scoreC   金币账户
   * @param state    1=加金币|0=减金币
   * @param number   更改金币的数额
   * @param origin   变动来源
   * @param operate  变动文字描述
   * @param orderSid 对应的订单号(可为空)
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveScoreCDetail(ScoreC scoreC, Integer state, Long number, Integer origin,
                               String operate,
                               String orderSid) throws Exception {
    try {
      ScoreCDetail detail = new ScoreCDetail();
      detail.setOperate(operate);
      detail.setOrigin(origin);
      detail.setOrderSid(orderSid);
      detail.setScoreC(scoreC);
      if (state == 0) {
        detail.setNumber(-number);
      } else {
        detail.setNumber(number);
      }
      scoreCDetailRepository.save(detail);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 根据scoreC查询金币明细列表 2017/03/09
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ScoreCDetail> findAllScoreCDetailByScoreC(ScoreC scoreC) {
    return scoreCDetailRepository.findAllByScoreCOrderByIdDesc(scoreC);
  }
}
