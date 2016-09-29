package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerInfo;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.repository.PartnerInfoRepository;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWalletRepository;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

  private static ReentrantLock lock = new ReentrantLock();


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public List<Partner> findAllParter() {
    return partnerRepository.findAll();
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Integer> giveScoreToUser(WeiXinUser weiXinUser, String phoneNumber,
                                              Merchant merchant) {
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
    int valueB = 0;

    ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser).get(0);
    ScoreB scoreB = scoreBRepository.findByLeJiaUser(leJiaUser);
    leJiaUser.setPhoneNumber(phoneNumber);
    leJiaUserRepository.save(leJiaUser);
    return null;
  }

  public Map<String, Integer> lockGiveScoreToUser(WeiXinUser weiXinUser, String phoneNumber,
                                                  Merchant merchant) {
    lock.lock();
    Map map = giveScoreToUser(weiXinUser, phoneNumber, merchant);
    lock.unlock();
    return map;
  }
}
