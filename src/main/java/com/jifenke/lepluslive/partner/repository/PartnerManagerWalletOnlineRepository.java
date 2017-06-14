package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWalletOnline;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xf on 2017/4/28.
 */
public interface PartnerManagerWalletOnlineRepository extends JpaRepository<PartnerManagerWalletOnline, Long> {
    PartnerManagerWalletOnline findByPartnerManager(PartnerManager partnerManager);
}
