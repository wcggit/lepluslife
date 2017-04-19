package com.jifenke.lepluslive.lejiauser.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.HttpUtils;
import com.jifenke.lepluslive.lejiauser.domain.entities.BankCard;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.BankCardRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @Inject
  private UnionBankCardService unionBankCardService;

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
   * 调用第三方获取银行卡相关信息 17/4/19
   */
  public Map<String, Object> cardCheck(String cardNum) {

    Map<String, String> headers = new HashMap<>();
    headers.put("accept", "application/json");
    headers.put("content-type", "application/json");
    headers.put("apix-key", Constants.CARD_CHECK_KEY);
    return HttpUtils.get(Constants.CARD_CHECK_URL + cardNum, headers);

  }

  /**
   * 绑定银行卡 16/11/28
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String addBankCard(LeJiaUser leJiaUser, String number, String cardType,
                            String prefixNum, String cardName, String bankName, String phoneNum,
                            Integer registerWay)
      throws Exception {
    try {
      Optional<BankCard> opt = bankCardRepository.findByNumberAndState(number, 1);
      if (opt.isPresent()) { //已被绑定
        return "2010";
      }
      BankCard bankCard = new BankCard();
      bankCard.setBankName(bankName);
      bankCard.setCardLength(number.length());
      bankCard.setCardName(cardName);
      bankCard.setCardType(cardType);
      bankCard.setLeJiaUser(leJiaUser);
      bankCard.setNumber(number);
      bankCard.setPrefixNum(prefixNum);
      bankCardRepository.save(bankCard);
      new Thread(() -> { //银商会员注册
        unionBankCardService.addUnionBankCard(leJiaUser, number, phoneNum, registerWay);
      }).start();
      return "200";
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }


}
