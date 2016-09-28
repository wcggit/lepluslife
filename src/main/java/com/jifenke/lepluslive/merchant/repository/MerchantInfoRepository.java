package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/5/13.
 */
public interface MerchantInfoRepository extends JpaRepository<MerchantInfo, Long> {

  /**
   * 根据二维码参数获取商家信息  16/09/07
   *
   * @param parameter 二维码参数
   * @return 商家id及是否是虚拟商户
   */
  @Query(value = "SELECT m.id,m.partnership FROM merchant m,merchant_info i WHERE m.merchant_info_id=i.id AND i.parameter=?1", nativeQuery = true)
  List<Object[]> findByParameter(String parameter);

}
