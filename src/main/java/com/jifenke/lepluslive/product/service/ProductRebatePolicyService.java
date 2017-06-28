package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.order.controller.dto.OrderShare;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductRebatePolicy;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.repository.ProductRebatePolicyRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 规格分润、返鼓励金规则
 * Created by zhangwen on 2017/6/26.
 */
@Service
@Transactional(readOnly = true)
public class ProductRebatePolicyService {

  @Inject
  private ProductRebatePolicyRepository repository;

  /**
   * 臻品订单支付成功后获取分润等数据  2017/6/27
   *
   * @param order onLineOrder
   */
  public OrderShare getByPaySuccess(OnLineOrder order) {

    OrderShare share = new OrderShare();

    long rebateScore = 0L;
    long commission = 0L;
    long toMerchant = 0L;
    long toPartner = 0L;
    long toPartnerManager = 0L;
    long toLePlusLife = 0L;

    List<OrderDetail> detailList = order.getOrderDetails();
    for (OrderDetail detail : detailList) {
      ProductRebatePolicy policy = repository.findByProductSpec(detail.getProductSpec());
      Integer number = detail.getProductNumber();
      rebateScore += policy.getRebateScore() * number;
      commission += policy.getCommission() * number;
      toMerchant += policy.getToMerchant() * number;
      toPartner += policy.getToPartner() * number;
      toPartnerManager += policy.getToPartnerManager() * number;
      toLePlusLife += policy.getToLePlusLife() * number;
    }

    share.setRebateScore(rebateScore);
    share.setShareMoney(commission);
    share.setShareToLockMerchant(toMerchant);
    share.setShareToLockPartner(toPartner);
    share.setShareToLockPartnerManager(toPartnerManager);
    share.setToLePlusLife(toLePlusLife);

    return share;
  }

  public ProductRebatePolicy findByProductSpec(ProductSpec productSpec) {

    return repository.findByProductSpec(productSpec);
  }

}
