package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnline;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnlineLog;

import com.jifenke.lepluslive.merchant.repository.MerchantWalletOnlineLogRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletOnlineRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 商户线上钱包操作 Created by zhangwen on 16/11/05.
 */
@Service
@Transactional(readOnly = true)
public class MerchantWalletOnlineService {

  @Inject
  private MerchantWalletOnlineRepository onlineRepository;

  @Inject
  private MerchantWalletOnlineLogRepository logRepository;


  /**
   * 获取商户线上钱包  16/11/05
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public MerchantWalletOnline findByMercahnt(Merchant merchant) {

    return onlineRepository.findByMerchant(merchant);
  }

  /**
   * 给绑定商户分润    2016/11/05
   *
   * @param shareMoney     分润金额
   * @param merchant       商户
   * @param merchantWallet 商户线上钱包
   * @param orderSid       线上订单号
   * @param type           1代表app线上订单分润  2代表公众号线上订单分润
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void shareToMerchant(long shareMoney, Merchant merchant,
                              MerchantWalletOnline merchantWallet, String orderSid, Long type) {
    MerchantWalletOnlineLog log = new MerchantWalletOnlineLog();

    Long availableBalance = merchantWallet.getAvailableBalance();

    log.setBeforeChangeMoney(availableBalance);

    long afterShareMoney = availableBalance + shareMoney;

    log.setAfterChangeMoney(afterShareMoney);

    log.setChangeMoney(shareMoney);

    log.setMerchantId(merchant.getId());

    log.setOrderSid(orderSid);

    log.setType(type);

    merchantWallet.setTotalMoney(merchantWallet.getTotalMoney() + shareMoney);

    merchantWallet.setAvailableBalance(afterShareMoney);

    logRepository.save(log);

    onlineRepository.save(merchantWallet);
  }

}
