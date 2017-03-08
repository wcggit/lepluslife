package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.RechargeCard;
import com.jifenke.lepluslive.activity.repository.RechargeCardRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by xf on 2017-1-4.
 */
@Service
@Transactional(readOnly = true)
public class RechargeCardService {

    @Inject
    private RechargeCardRepository rechargeCardRepository;

    /**
     * 保存 充值卡兑换记录
     * @param rechargeCard
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveRechargeCard(RechargeCard rechargeCard) {
      rechargeCardRepository.save(rechargeCard);
    }

    /**
     * 查询用户 充值卡兑换记录
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<RechargeCard> findRechargeCardByWeiXinUser(WeiXinUser weiXinUser) {
      return rechargeCardRepository.findByWeiXinUser(weiXinUser);
    }
    /**
     * 根据兑换码 查询兑换记录
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<RechargeCard> findRechargeCardByExchangeCode(String exchangeCode) {
      return rechargeCardRepository.findByExchangeCode(exchangeCode);
    }

}
