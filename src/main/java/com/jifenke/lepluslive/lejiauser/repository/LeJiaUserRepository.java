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

  List<LeJiaUser> findByPhoneNumber(String phoneNumber);

  /**
   * APP获取用户信息  16/09/07
   *
   * @param token 用户唯一标识
   * @return 个人信息
   */
  @Query(value = "SELECT w.nickname,w.head_image_url,u.user_sid,u.phone_number,a.score AS aScore,b.score AS bScore,u.bind_merchant_id FROM le_jia_user u,wei_xin_user w,scorea a ,scoreb b WHERE u.wei_xin_user_id=w.id AND a.le_jia_user_id=u.id AND b.le_jia_user_id=u.id AND u.user_sid=?1", nativeQuery = true)
  List<Object[]> getUserInfo(String token);

  @Query(value = "select merchant_id from off_line_order where le_jia_user_id = ?1 and complete_date  is  not null  order by complete_date desc limit 1", nativeQuery = true)
  Long checkUserBindMerchant(Long leJiaUserId);

  @Query(value = "select count(*) from le_jia_user where bind_merchant_id = ?1", nativeQuery = true)
  Long countMerchantBindLeJiaUser(Long merchantId);

  @Query(value = "select count(*) from le_jia_user where bind_partner_id = ?1", nativeQuery = true)
  Long countPartnerBindLeJiaUser(Long partnerId);

  // List<LeJiaUser> findAllByMerchant(Merchant merchant);
}
