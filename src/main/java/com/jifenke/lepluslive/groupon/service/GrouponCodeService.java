package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.global.util.DateUtils;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.repository.GrouponCodeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * 团购码
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponCodeService {

  @Inject
  private GrouponCodeRepository repository;

  @Transactional(propagation = Propagation.REQUIRED)
  public void saveGrouponCode(GrouponCode grouponCode) {
    repository.save(grouponCode);
  }

  /**
   * 订单确认页生成团购码  APP&WEB  2017/6/21
   *
   * @param grouponOrder 订单
   * @param buyNum       购买数量
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Date createGrouponCode(GrouponOrder grouponOrder, Integer buyNum) throws Exception {
    //如果已存在团购码，删除
    List<GrouponCode> list = grouponOrder.getGrouponCodes();
    if (list != null && list.size() > 0) {
      repository.deleteInBatch(list);
    }
    GrouponProduct product = grouponOrder.getGrouponProduct();
    //确定团购码使用起始时间
    Date startDate = null;
    Date expiredDate = null;
    Long commission = 0L; //单个券码的佣金
    Long totalPrice = 0L; //单个券码的金额
    try {
      if (product.getValidityType() == 0) { //相对日期
        startDate = new Date();
        expiredDate = DateUtils.dayChange(startDate, Integer.valueOf(product.getValidity()));
      } else { //绝对时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] s = product.getValidity().split("~");
        startDate = sdf.parse(s[0]);
        expiredDate = sdf.parse(s[1]);
      }
      if (grouponOrder.getOrderType() == 1) { //乐加订单
        commission = product.getLjCommission();
        totalPrice = product.getLjPrice();
      } else { //普通订单
        commission = product.getCharge();
        totalPrice = product.getNormalPrice();
      }
      //保存团购码
      for (int i = 0; i < buyNum; i++) {
        GrouponCode code = new GrouponCode();
        code.setGrouponOrder(grouponOrder);
        code.setGrouponProduct(product);
        code.setLeJiaUser(grouponOrder.getLeJiaUser());
        code.setStartDate(startDate);
        code.setExpiredDate(expiredDate);
        code.setTotalPrice(totalPrice);
        code.setCommission(commission);
        code.setTrasnferMoney(totalPrice - commission);
        if (grouponOrder.getOrderType() == 1) {
          code.setCodeType(1);
          code.setShareToLockMerchant(product.getShareToLockMerchant());
          code.setShareToLockPartner(product.getShareToLockPartner());
          code.setShareToLockPartnerManager(product.getShareToLockPartnerManager());
          code.setShareToTradePartner(product.getShareToTradePartner());
          code.setShareToTradePartnerManager(product.getShareToTradePartnerManager());
        }
        repository.save(code);
      }
      return expiredDate;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 支付成功券码处理  2017/6/22
   *
   * @param order 订单
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void paySuccess(GrouponOrder order) {
    List<GrouponCode> codeList = repository.findAllByGrouponOrder(order);
    for (GrouponCode code : codeList) {
      code.setState(0);
      repository.save(code);
    }
  }
}
