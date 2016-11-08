package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnline;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangwen on 16/11/05.
 */
public interface MerchantWalletOnlineRepository extends JpaRepository<MerchantWalletOnline, Long> {

  MerchantWalletOnline findByMerchant(Merchant merchant);

}
