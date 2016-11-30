package com.jifenke.lepluslive.lejiauser.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.BankCard;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.BankCardRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

/**
 * 用户银行卡号相关接口 Created by zhangwen on 2016/11/28.
 */
@Service
@Transactional(readOnly = true)
public class BankCardService {

  @Inject
  private BankCardRepository bankCardRepository;

  /**
   * 获取某用户的卡号列表 16/11/28
   */
  public List<BankCard> findByLeJiaUser(LeJiaUser leJiaUser) {
    return bankCardRepository.findByLeJiaUserAndState(leJiaUser, 1);
  }

  /**
   * 删除绑定的银行卡 16/11/28
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void deleteBankCard(Long id, String token) throws Exception {
    try {
      BankCard bankCard = bankCardRepository.findOne(id);
      if (token.equals(bankCard.getLeJiaUser().getUserSid())) {
        bankCard.setState(0);
        bankCardRepository.save(bankCard);
      } else {
        throw new RuntimeException();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }

  }

  /**
   * 绑定银行卡 16/11/28
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String addBankCard(LeJiaUser leJiaUser, String number, Integer cardLength, String cardType,
                            String prefixNum, String cardName, String bankName) throws Exception {
    try {
      Optional<BankCard> opt = bankCardRepository.findByNumberAndState(number, 1);
      if (opt.isPresent()) { //已被绑定
        return "2010";
      }
      BankCard bankCard = new BankCard();
      bankCard.setBankName(bankName);
      bankCard.setCardLength(cardLength);
      bankCard.setCardName(cardName);
      bankCard.setCardType(cardType);
      bankCard.setLeJiaUser(leJiaUser);
      bankCard.setNumber(number);
      bankCard.setPrefixNum(prefixNum);
      bankCardRepository.save(bankCard);
      return "200";
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
