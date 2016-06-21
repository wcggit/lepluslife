package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/24.
 */
public interface LeJiaUserRepository extends JpaRepository<LeJiaUser, Long> {
    LeJiaUser findByUserSid(String userSid);

    LeJiaUser findOneByPhoneNumber(String phoneNumber);

    @Query(value = "select merchant_id from off_line_order where le_jia_user_id = ?1 and complete_date  is  not null  order by complete_date desc limit 1",nativeQuery = true)
    Long checkUserBindMerchant(Long leJiaUserId);

    @Query(value = "select count(*) from le_jia_user where bind_merchant_id = ?1",nativeQuery = true)
    Long countMerchantBindLeJiaUser(Long merchantId);

    @Query(value = "select count(*) from le_jia_user where bind_partner_id = ?1",nativeQuery = true)
    Long countPartnerBindLeJiaUser(Long partnerId);

    // List<LeJiaUser> findAllByMerchant(Merchant merchant);
}
