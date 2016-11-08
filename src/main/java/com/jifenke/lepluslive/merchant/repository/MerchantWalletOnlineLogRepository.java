package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnlineLog;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangwen on 16/11/05.
 */
public interface MerchantWalletOnlineLogRepository
    extends JpaRepository<MerchantWalletOnlineLog, Long> {

}
