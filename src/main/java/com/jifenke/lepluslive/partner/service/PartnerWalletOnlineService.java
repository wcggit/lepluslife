package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnlineLog;
import com.jifenke.lepluslive.partner.repository.PartnerWalletOnlineLogRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWalletOnlineRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 合伙人线上钱包操作 Created by zhangwen on 16/11/05.
 */
@Service
@Transactional(readOnly = true)
public class PartnerWalletOnlineService {

  @Inject
  private PartnerWalletOnlineRepository onlineRepository;

  @Inject
  private PartnerWalletOnlineLogRepository logRepository;


  /**
   * 获取合伙人线上钱包  16/11/05
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public PartnerWalletOnline findByPartner(Partner partner) {

    return onlineRepository.findByPartner(partner);
  }

  /**
   * 给绑定合伙人分润    2016/11/05
   *
   * @param shareMoney    分润金额
   * @param partner       合伙人
   * @param partnerWallet 合伙人线上钱包
   * @param orderSid      线上订单号
   * @param type          1代表app线上订单分润  2代表公众号线上订单分润
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void shareToPartner(Long shareMoney, Partner partner, PartnerWalletOnline partnerWallet,
                             String orderSid, Long type) {
      PartnerWalletOnlineLog log = new PartnerWalletOnlineLog();

      Long availableBalance = partnerWallet.getAvailableBalance();
      log.setBeforeChangeMoney(availableBalance);
      long afterShareMoney = availableBalance + shareMoney;

      log.setAfterChangeMoney(afterShareMoney);
      log.setChangeMoney(shareMoney);

      log.setPartnerId(partner.getId());

      log.setOrderSid(orderSid);

      log.setType(type);

      partnerWallet.setTotalMoney(partnerWallet.getTotalMoney() + shareMoney);

      partnerWallet.setAvailableBalance(afterShareMoney);

      logRepository.save(log);

      onlineRepository.save(partnerWallet);

  }

}
