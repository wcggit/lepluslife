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
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
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
 * 商城订单相关 Created by zhangwen on 2016/9/22.
 */
@Service
@Transactional(readOnly = true)
public class OnlineOrderService {

  @Inject
  private OrderService orderService;

  @Inject
  private ProductService productService;

  @Inject
  private OrderRepository orderRepository;

  @Inject
  private Scheduler scheduler;

  @Inject
  private OnLineOrderShareService orderShareService;

  @Inject
  private ScoreCService scoreCService;

  /**
   * 金币商品创建订单（包括商品详情页下单和购物车下单）  2017/4/5
   *
   * @param carts     购买商品详情
   * @param leJiaUser 用户
   * @param address   地址
   * @param payWayId  订单来源 5=公众号|1=APP
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Map<String, Object> createGoldOrder(String carts,
                                             LeJiaUser leJiaUser,
                                             Address address, Long payWayId) {
    List<CartDetailDto> cartList = orderService.stringToList(carts);
    Map<String, Object> result = new HashMap<>();
    OnLineOrder onLineOrder = new OnLineOrder();
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    Long orderPrice = 0L; //市场价（不包括邮费）
    Long freightPrice = 0L;
    Long totalScore = 0L;//订单最高可使用金币(以后不变)
    Long totalPrice = 0L;    //订单共需付款金额(包括邮费，以后不变) = totalScore + freightPrice
    Long payBackA = 0L;  //返鼓励金
    int free = 0; //是否包邮 1=是
    for (CartDetailDto cartDetailDto : cartList) {
      Integer number = cartDetailDto.getProductNumber();
      if (number == null || number < 1) {
        result.put("status", 5011); //购买数量不能小于1
        break;
      }
      OrderDetail orderDetail = new OrderDetail();
      Product product = productService.findOneProduct(cartDetailDto.getProduct().getId());
      ProductSpec productSpec = productService.editProductSpecRepository(
          cartDetailDto.getProductSpec().getId(),
          cartDetailDto.getProductNumber());
      if (productSpec != null) {
        orderPrice += productSpec.getPrice() * cartDetailDto.getProductNumber();
        totalPrice += productSpec.getMinScore() * cartDetailDto.getProductNumber();
        totalScore += productSpec.getMinScore() * cartDetailDto.getProductNumber();
      } else {
        result.put("status", 5005); //某件商品的某种规格无库存了
        String[] o = new String[1];
        o[0] = product.getName();
        result.put("arrays", o); //某件商品的某种规格无库存了
        result.put("data", product.getName() + "_" + cartDetailDto.getProductNumber());
        break;
      }
      //判断是否包邮
      if (free == 0) {
        if (product.getPostage() == 0) { //该产品包邮
          free = 1;
        } else if (product.getPostage() > freightPrice) { //比目前邮费高
          freightPrice = product.getPostage().longValue();
        }
      }
      onLineOrder.setType(2);
      orderDetail.setState(1);
      orderDetail.setProduct(product);
      orderDetail.setProductNumber(cartDetailDto.getProductNumber());
      orderDetail.setProductSpec(productSpec);
      orderDetail.setOnLineOrder(onLineOrder);
      orderDetails.add(orderDetail);

      //返鼓励金
      if (product.getIsBackRed() == 1) {
        if (product.getBackRedType() == 1) {//比例
          payBackA +=
              (long) Math.ceil((double) (product.getBackRatio() * product.getMinScore()) / 100);
        } else {
          payBackA += product.getBackMoney().longValue();
        }
      }
    }
    //判断是否某件商品库存不足
    if (result.get("status") != null) {
      return result;
    }

    //获取邮费
    if (free == 1) {
      freightPrice = 0L;
    }
    totalPrice += freightPrice;

    onLineOrder.setLeJiaUser(leJiaUser);
    if (address != null) {
      onLineOrder.setAddress(address);
    }

    onLineOrder.setOrderDetails(orderDetails);
    onLineOrder.setState(0);
    onLineOrder.setPayState(0);
    onLineOrder.setOrderPrice(orderPrice);
    onLineOrder.setTotalPrice(totalPrice);
    onLineOrder.setTotalScore(totalScore);
    onLineOrder.setFreightPrice(freightPrice);
    onLineOrder.setPayBackA(payBackA);
    PayOrigin payOrigin = new PayOrigin(payWayId); //设置支付来源
    onLineOrder.setPayOrigin(payOrigin);
    orderRepository.save(onLineOrder);

    JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
    jobThread.start();

    result.put("status", 200);
    result.put("data", onLineOrder);
    return result;
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
   * 全金币支付购买金币商品  17/02/20
   *
   * @param transmitWay 线下自提=1
   * @param payOrigin   支付来源 13=APP全金币|14=公众号全金币
   * @param message     消费者留言
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> orderPayByScoreC(OnLineOrder order, Long trueScore,
                                              Integer transmitWay,
                                              Long payOrigin, String message) {

    Map<String, Object> result = new HashMap<>();
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(order.getLeJiaUser());
    Date date = new Date();
    try {
      //订单状态处理
      order.setPayOrigin(new PayOrigin(payOrigin));
      order.setTruePrice(0L);
      order.setTrueScore(order.getTotalScore());
      order.setState(1);
      order.setPayState(1);
      order.setPayDate(date);
      if (transmitWay == 1) {
        order.setDeliveryDate(date);
        order.setConfirmDate(date);
        order.setState(3);
      }
      order.setTransmitWay(transmitWay);
      order.setMessage(message);
      //订单相关product的销量等数据处理
      productService.editProductSaleByPayOrder(order);
      //C金币修改及记录添加
      scoreCService.saveScoreC(scoreC, 0, trueScore);
      scoreCService.saveScoreCDetail(scoreC, 0, trueScore, 2, "金币商城消费", order.getOrderSid());
      orderRepository.save(order);
      result.put("status", 2000);
      result.put("data", order.getId());
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 金币订单页提交,输入实际使用金额和金币 17/02/20
   *
   * @param orderId     订单id
   * @param trueScore   实际使用金币
   * @param transmitWay 物流方式 1=自提
   * @param message     消费者留言
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> completeGoldOrder(Long orderId, Long trueScore, Long payOrigin,
                                               Integer transmitWay, String message) {
    Map<String, Object> result = new HashMap<>();
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    if (onLineOrder.getState() == 1) { //订单已经支付
      result.put("status", 5012);
      return result;
    }
    if (onLineOrder.getState() == 4) { //订单已经失效
      result.put("status", 5008);
      return result;
    }
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(onLineOrder.getLeJiaUser());
    if (scoreC.getScore() < trueScore) {
      result.put("status", 6007);
      return result;
    }

    Long newTruePrice = onLineOrder.getTotalScore() - trueScore;
    if (newTruePrice < 0) {
      result.put("status", 5009);
      return result;
    }
    if (transmitWay == 2) {
      newTruePrice += onLineOrder.getFreightPrice();
    }
    if (newTruePrice == 0) { //全积分兑换
      if (payOrigin == 1) {
        payOrigin = 13L;
      } else {
        payOrigin = 14L;
      }
      return orderPayByScoreC(onLineOrder, trueScore, transmitWay, payOrigin, message);
    }

    onLineOrder.setTruePrice(newTruePrice);
    onLineOrder.setTransmitWay(transmitWay);
    onLineOrder.setTrueScore(trueScore);
    onLineOrder.setMessage(message);

    orderRepository.saveAndFlush(onLineOrder);
    result.put("status", 200);
    result.put("data", onLineOrder);
    return result;
  }


}
