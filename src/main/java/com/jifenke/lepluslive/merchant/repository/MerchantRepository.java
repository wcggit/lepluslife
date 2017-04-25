package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/17.
 */
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

  Page<Merchant> findAll(Pageable pageable);

  //商家详情
  @Query(value = "SELECT m.id,m.`name` AS mName,m.location,m.phone_number,m.lng,m.lat,t.`name` AS tName,a.`name` AS aName,i.star,i.card,i.park,i.per_sale,i.wifi,i.description,i.discount,i.feature,i.reason,i.vip_picture from merchant m,merchant_info i,merchant_type t,area a WHERE m.merchant_info_id=i.id AND m.merchant_type_id=t.id AND m.area_id=a.id AND m.id=?1", nativeQuery = true)
  List<Object[]> findMerchantById(Long id);

  List<Merchant> findByPartnerAndPartnership(Partner partner, int i);

  Merchant findByMerchantSid(String merchantSid);

  /**
   * 查询某一商户下所有门店的详细信息  2017/01/09
   *
   * @param merchantUser 商户
   */
  List<Merchant> findByMerchantUser(MerchantUser merchantUser);
}
