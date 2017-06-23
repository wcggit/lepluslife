package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponRefundCode;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 退款单对应的团购码 Created by zhangwen on 2017/6/16.
 */
public interface GrouponRefundCodeRepository extends JpaRepository<GrouponRefundCode, Long> {


}
