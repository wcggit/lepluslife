package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 团购商品 Created by zhangwen on 2017/6/16.
 */
public interface GrouponProductRepository extends JpaRepository<GrouponProduct, Long> {


}
