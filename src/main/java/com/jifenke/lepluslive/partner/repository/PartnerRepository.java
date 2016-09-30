package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerRepository extends JpaRepository<Partner, Long> {

  Partner findByPartnerSid(String sid);

  Optional<Partner> findByWeiXinUser(WeiXinUser weiXinUser);
}
