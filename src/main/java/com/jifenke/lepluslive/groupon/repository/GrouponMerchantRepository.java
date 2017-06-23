package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponMerchant;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * 团购产品对应门店 Created by zhangwen on 2017/6/16.
 */
public interface GrouponMerchantRepository extends JpaRepository<GrouponMerchant, Long> {

  /**
   * 团购产品对应门店列表 2017/6/16
   *
   * @param grouponProduct 团购产品
   */
  List<GrouponMerchant> findAllByGrouponProduct(GrouponProduct grouponProduct);


}
