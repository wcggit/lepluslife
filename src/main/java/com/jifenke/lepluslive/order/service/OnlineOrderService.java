package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.order.repository.OrderRepository;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.service.JobThread;

import org.quartz.Scheduler;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/9/22.
 */
@Service
@Transactional(readOnly = true)
public class OnlineOrderService {


  @Inject
  private ProductService productService;

  @Inject
  private OrderRepository orderRepository;

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private ScoreBDetailRepository scoreBDetailRepository;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private Scheduler scheduler;

  /**
   * 创建爆品的待支付订单 16/09/22
   *
   * @param productId 爆品Id
   * @param specId    爆品规格Id
   * @param buyNumber 该规格购买数量
   * @param leJiaUser 购买用户
   * @param address   用户地址
   * @param payWayId  购买来源Id
   * @return 返回状态码和data
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map createHotOrder(Long productId, Long specId, Integer buyNumber, LeJiaUser leJiaUser,
                            Address address,
                            Long payWayId) throws Exception {
    Map<String, Object> result = new HashMap<>();
    Product product = productService.findOneProduct(productId);
    if (product == null) {
      result.put("status", 500);  //异常
      return result;
    }
    ProductSpec productSpec = productService.editProductSpecRepository(specId, buyNumber);
    if (productSpec == null) {
      result.put("status", 5003); //无库存了
      return result;
    }
    OnLineOrder onLineOrder = new OnLineOrder();
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    //订单信息
    try {
      Long totalPrice = productSpec.getMinPrice() * buyNumber;    //订单共需付款金额(以后不变)
      Long truePrice = productSpec.getMinPrice() * buyNumber;  //最低使用金额
      if (product.getPostage() != 0) {
        totalPrice += product.getPostage();
        truePrice += product.getPostage();
      }
      Long freightPrice = (long) product.getPostage();
      Long totalScore = (long) productSpec.getMinScore() * buyNumber;//订单最高可使用积分(以后不变)

      onLineOrder.setLeJiaUser(leJiaUser);
      if (address != null) {
        onLineOrder.setAddress(address);
      }
      onLineOrder.setFreightPrice(freightPrice);
      onLineOrder.setTotalPrice(totalPrice);
      onLineOrder.setTotalScore(totalScore);
      onLineOrder.setTruePrice(truePrice);  //最少需要money

      onLineOrder.setState(0);
      onLineOrder.setPayState(0);
      onLineOrder.setPayOrigin(new PayOrigin(payWayId));
      //订单详情
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setState(1);
      orderDetail.setProduct(product);
      orderDetail.setProductNumber(buyNumber);
      orderDetail.setProductSpec(productSpec);
      orderDetail.setOnLineOrder(onLineOrder);
      orderDetails.add(orderDetail);

      onLineOrder.setOrderDetails(orderDetails);

      orderRepository.save(onLineOrder);
      JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
      jobThread.start();

      result.put("status", 200);
      result.put("data", onLineOrder);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception();
    }
  }

  /**
   * 分页查询订单 16/09/23
   *
   * @param leJiaUser 用户
   * @param status    订单状态
   * @param currPage  当前页码
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<OnLineOrder> getCurrentUserOrderListByPage(LeJiaUser leJiaUser, Integer status,
                                                         Integer currPage) {
    List<OnLineOrder> list = null;
    //订单状态(0=待付款|1=待发货|2=待收货|3=已完成|5=全部)
    if (5 == status) {
      List<Integer> states = new ArrayList<>();
      states.add(-1);
      states.add(4);
      list = orderRepository
          .findAllByLeJiaUserAndStateNotInOrderByCreateDateDesc(new PageRequest(currPage - 1, 10),
                                                                leJiaUser, states).getContent();
    } else {
      list =
          orderRepository
              .findAllByLeJiaUserAndStateOrderByCreateDateDesc(new PageRequest(currPage - 1, 10),
                                                               leJiaUser, status).getContent();
    }
    return list;
  }

  /**
   * 全积分支付购买商品  16/09/27
   *
   * @param orderId     订单id
   * @param trueScore   实际使用积分
   * @param transmitWay 线下自提=1
   * @param payOrigin   支付来源 9=APP全积分|10=公众号全积分
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<Object, Object> orderPayByScoreB(Long orderId, Long trueScore, Integer transmitWay,
                                              Long payOrigin) throws Exception {
    Map<Object, Object> result = new HashMap<>();
    OnLineOrder order = orderRepository.findOne(orderId);
    if (order == null) {
      result.put("status", 5006);
      return result;
    }
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(order.getLeJiaUser());
    if (scoreB == null) {
      result.put("status", 6001);
      return result;
    }
    if (scoreB.getScore() < order.getTotalScore()) {
      result.put("status", 6002);
      return result;
    }
    if ((transmitWay == 2 && order.getFreightPrice() != 0)
        || order.getTotalScore().longValue() != trueScore) {
      result.put("status", 5007);
      return result;
    }
    PayOrigin payWay = new PayOrigin(payOrigin);
    //暂定不反A积分
    if (order.getState() == 4) {
      result.put("status", 5008);
      return result;
    }
    Date date = new Date();
    try {
      //订单状态处理
      order.setPayOrigin(payWay);
      order.setState(3);
      order.setPayState(1);
      order.setTrueScore(trueScore);
      order.setTruePrice(0L);
      order.setPayDate(date);
      if (transmitWay == 1) {
        order.setDeliveryDate(date);
        order.setConfirmDate(date);
      }
      order.setTransmitWay(transmitWay);
      //订单相关product的销量等数据处理
      productService.editProductSaleByPayOrder(order);
      //B积分修改
      scoreB.setScore(scoreB.getScore() - trueScore);
      ScoreBDetail scoreBDetail = new ScoreBDetail();
      scoreBDetail.setOperate("乐+商城消费");
      scoreBDetail.setOrigin(2);
      scoreBDetail.setOrderSid(order.getOrderSid());
      scoreBDetail.setScoreB(scoreB);
      scoreBDetail.setNumber(-trueScore);
      scoreBDetailRepository.save(scoreBDetail);
      scoreBRepository.save(scoreB);
      orderRepository.save(order);
      result.put("status", 200);
      result.put("data", order.getId());
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

}
