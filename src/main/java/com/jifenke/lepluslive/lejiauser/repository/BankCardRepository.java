package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.BankCard;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


/**
 * 用户银行卡号相关接口 Created by zhangwen on 2016/11/28.
 */
public interface BankCardRepository extends JpaRepository<BankCard, Long> {

  /**
   * 根据银行卡号查找记录  16/11/28
   */
  Optional<BankCard> findByNumberAndState(String number, Integer state);

  /**
   * 获取某用户的卡号列表 16/11/28
   */
  List<BankCard> findByLeJiaUserAndState(LeJiaUser leJiaUser, Integer state);

}
