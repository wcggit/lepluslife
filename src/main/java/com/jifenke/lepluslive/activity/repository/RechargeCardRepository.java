package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.RechargeCard;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by tqy on 2017/1/4.
 */
public interface RechargeCardRepository extends JpaRepository<RechargeCard, Long> {

  List<RechargeCard> findByWeiXinUser(WeiXinUser weiXinUser);
  List<RechargeCard> findByExchangeCode(String exchangeCode);

  Page findAll(Specification<RechargeCard> specification, Pageable pageRequest);

}
