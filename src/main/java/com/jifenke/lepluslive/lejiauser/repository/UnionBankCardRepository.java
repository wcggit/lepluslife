package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.UnionBankCard;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 用户银行卡号相关接口 Created by zhangwen on 2016/11/28.
 */
public interface UnionBankCardRepository extends JpaRepository<UnionBankCard, Long> {

  UnionBankCard findByNumber(String number);

}
