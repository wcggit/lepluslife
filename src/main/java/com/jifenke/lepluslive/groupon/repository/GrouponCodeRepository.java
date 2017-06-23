package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * 团购码 Created by zhangwen on 2017/6/16.
 */
public interface GrouponCodeRepository extends JpaRepository<GrouponCode, Long> {

  List<GrouponCode> findAllByGrouponOrder(GrouponOrder grouponOrder);

}
