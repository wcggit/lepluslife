package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerInfo;
import com.jifenke.lepluslive.partner.domain.entities.PartnerScoreLog;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.repository.PartnerInfoRepository;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerScoreLogRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWalletRepository;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

/**
 * Created by wcg on 16/6/3.
 */
@Service
@Transactional(readOnly = true)
public class PartnerService {

  @Inject
  private PartnerRepository partnerRepository;

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
  private PartnerInfoRepository partnerInfoRepository;

  @Inject
  private PartnerWalletRepository partnerWalletRepository;

  @Inject
  private PartnerScoreLogRepository partnerScoreLogRepository;

  private static ReentrantLock lock = new ReentrantLock();


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public List<Partner> findAllParter() {
    return partnerRepository.findAll();
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Integer> giveScoreToUser(WeiXinUser weiXinUser, String phoneNumber,
                                              Merchant merchant) {
    Map<String, Integer> map = new HashMap<>();
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    Partner partner = merchant.getPartner();
    PartnerWallet partnerWallet = partnerWalletRepository.findByPartner(partner);
    PartnerInfo partnerInfo = partnerInfoRepository.findByPartner(partner);
    int
        valueA =
        partnerInfo.getScoreAType() == 0 ? partnerInfo.getMaxScoreA()
                                         :
        new Random().nextInt(partnerInfo.getMaxScoreA() - partnerInfo.getMinScoreA() + 1)
        + partnerInfo.getMinScoreA();
    int
        valueB =
        partnerInfo.getScoreBType() == 0 ? partnerInfo.getMaxScoreB() :
        new Random().nextInt(partnerInfo.getMaxScoreB() - partnerInfo.getMinScoreB() + 1)
        + partnerInfo.getMinScoreB();
    Date date = new Date();
    if (partnerWallet.getAvailableScoreA() >= valueA
        && partnerWallet.getAvailableScoreB() >= valueB) {
      leJiaUser.setPhoneNumber(phoneNumber);
      leJiaUserRepository.save(leJiaUser);
      //合伙人减,会员加
      String sid = "0_" + partner.getPartnerSid();
      if (valueA > 0) {
        partnerWallet.setAvailableScoreA(partnerWallet.getAvailableScoreA() - valueA);
        PartnerScoreLog scoreALog = new PartnerScoreLog();
        scoreALog.setScoreAOrigin(1);
        scoreALog.setDescription("邀请会员送红包");
        scoreALog.setSid(leJiaUser.getUserSid());
        scoreALog.setNumber(-(long) valueA);
        scoreALog.setType(1);
        scoreALog.setCreateDate(date);
        partnerScoreLogRepository.save(scoreALog);
        ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser).get(0);
        scoreA.setScore(scoreA.getScore() + valueA);
        scoreA.setTotalScore(scoreA.getTotalScore() + valueA);
        scoreA.setLastUpdateDate(date);
        scoreARepository.save(scoreA);
        ScoreADetail scoreADetail = new ScoreADetail();
        scoreADetail.setNumber(Long.valueOf(String.valueOf(valueA)));
        scoreADetail.setScoreA(scoreA);
        scoreADetail.setOperate("注册送礼");
        scoreADetail.setOrigin(0);
        scoreADetail.setOrderSid(sid);
        scoreADetailRepository.save(scoreADetail);
      }
      if (valueB > 0) {
        partnerWallet.setAvailableScoreB(partnerWallet.getTotalScoreB() - valueB);
        PartnerScoreLog scoreBLog = new PartnerScoreLog();
        scoreBLog.setType(0);
        scoreBLog.setNumber(-(long) valueB);
        scoreBLog.setDescription("邀请会员送积分");
        scoreBLog.setSid(leJiaUser.getUserSid());
        scoreBLog.setScoreBOrigin(1);
        scoreBLog.setCreateDate(date);
        partnerScoreLogRepository.save(scoreBLog);
        ScoreB scoreB = scoreBRepository.findByLeJiaUser(leJiaUser);
        scoreB.setLastUpdateDate(date);
        scoreB.setScore(scoreB.getScore() + valueB);
        scoreB.setTotalScore(scoreB.getTotalScore() + valueB);
        scoreBRepository.save(scoreB);
        ScoreBDetail scoreBDetail = new ScoreBDetail();
        scoreBDetail.setNumber((long) valueB);
        scoreBDetail.setScoreB(scoreB);
        scoreBDetail.setOperate("注册送礼");
        scoreBDetail.setOrigin(0);
        scoreBDetail.setOrderSid(sid);
        scoreBDetailRepository.save(scoreBDetail);
      }
      partnerWalletRepository.save(partnerWallet);
      map.put("return", 1);
      map.put("scoreA", valueA);
      map.put("scoreB", valueB);
    } else {
      map.put("return", 0);
    }
    return map;
  }

  public Map<String, Integer> lockGiveScoreToUser(WeiXinUser weiXinUser, String phoneNumber,
                                                  Merchant merchant) {
    lock.lock();
    Map map = giveScoreToUser(weiXinUser, phoneNumber, merchant);
    lock.unlock();
    return map;
  }
}
