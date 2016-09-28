package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.order.repository.OrderDetailRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 订单详情 Created by zhangwen on 2016/9/22.
 */
@Service
@Transactional(readOnly = true)
public class OrderDetailService {

  @Inject
  private OrderDetailRepository orderDetailRepository;

  /**
   * 查询某个用户已购买某个商品的数量 16/09/22
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Integer getCurrentUserBuyProductCount(Long leJiaUserId, Long productId) {
    Integer count = orderDetailRepository.getCurrentUserBuyProductCount(leJiaUserId, productId);
    return count == null ? 0 : count;
  }

  /**
   * 查询某个用户待付款的某个商品的数量 16/09/22
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Integer getCurrentUserOrderProductCount(Long leJiaUserId, Long productId) {
    Integer count = orderDetailRepository.getCurrentUserOrderProductCount(leJiaUserId, productId);
    return count == null ? 0 : count;
  }

}
