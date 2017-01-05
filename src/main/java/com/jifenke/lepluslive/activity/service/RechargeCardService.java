package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.RechargeCard;
import com.jifenke.lepluslive.activity.repository.RechargeCardRepository;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
