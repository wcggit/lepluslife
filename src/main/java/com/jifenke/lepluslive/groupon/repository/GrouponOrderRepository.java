package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 团购订单 Created by zhangwen on 2017/6/16.
 */
public interface GrouponOrderRepository extends JpaRepository<GrouponOrder, Long> {


  GrouponOrder findByOrderSid(String orderSid);


}
