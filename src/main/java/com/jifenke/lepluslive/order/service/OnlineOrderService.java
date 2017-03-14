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
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.score.service.ScoreCService;
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
  private OrderService orderService;

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

  @Inject
  private OnLineOrderShareService orderShareService;

  @Inject
  private ScoreCService scoreCService;

  /**
   * 金币商品立即购买创建的待支付订单 17/02/20
   *
   * @param productId 商品Id
   * @param specId    商品规格Id
   * @param buyNumber 该规格购买数量
   * @param leJiaUser 购买用户
   * @param address   用户地址
   * @param payWayId  购买来源Id
   * @return 返回状态码和data
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map createGoldOrder(Long productId, Long specId, Integer buyNumber, LeJiaUser leJiaUser,
                             Address address,
                             Long payWayId) throws Exception {
    Map<String, Object> result = new HashMap<>();
    Product product = productService.findOneProduct(productId);
    if (product == null) {
      result.put("status", 500);  //异常
      return result;
    }
    if (buyNumber == null || buyNumber < 1) {
      buyNumber = 1;
    }
    ProductSpec productSpec = productService.editProductSpecRepository(specId, buyNumber);
    if (productSpec == null) {
      result.put("status", 5003); //无库存了
      return result;
    }
    OnLineOrder onLineOrder = new OnLineOrder();
    onLineOrder.setType(2);
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    //订单信息
    try {
      Long orderPrice = productSpec.getPrice() * buyNumber;
      Long totalPrice = 0L;
      Long totalScore = productSpec.getMinScore() * buyNumber.longValue();//订单最高可使用金币(以后不变)
      Long truePrice = 0L;  //该订单最低需使用金额
      Long freightPrice = 0L;
      Long payBackA = 0L;

      if (product.getPostage() != 0) { //该产品不包邮
        Long FREIGHT_PRICE = product.getPostage().longValue(); //使用该产品的邮费
        freightPrice = FREIGHT_PRICE;
        totalPrice += FREIGHT_PRICE;
        truePrice += FREIGHT_PRICE;
      }

      onLineOrder.setLeJiaUser(leJiaUser);
      if (address != null) {
        onLineOrder.setAddress(address);
      }
      onLineOrder.setOrderPrice(orderPrice);
      onLineOrder.setFreightPrice(freightPrice);
      onLineOrder.setTotalPrice(totalPrice);
      onLineOrder.setTotalScore(totalScore);
      onLineOrder.setTruePrice(truePrice);  //最少需要money

      onLineOrder.setState(0);
      onLineOrder.setPayState(0);
      onLineOrder.setPayOrigin(new PayOrigin(payWayId));
      //返红包金额
      if (product.getIsBackRed() == 1) {
        if (product.getBackRedType() == 1) {//比例
          payBackA =
              (long) Math.ceil((double) (product.getBackRatio() * product.getMinScore()) / 100);
        } else {
          payBackA = product.getBackMoney().longValue();
        }
      }
      onLineOrder.setPayBackA(payBackA);
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
  public Map<String, Object> orderPayByScoreB(Long orderId, Long trueScore, Integer transmitWay,
                                              Long payOrigin) throws Exception {
    Map<String, Object> result = new HashMap<>();
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
    if (payOrigin == 9) {
      payWay.setPayFrom(1);
    } else {
      payWay.setPayFrom(2);
    }
    //暂定不返A积分
    if (order.getState() == 4) {
      result.put("status", 5008);
      return result;
    }
    int check = orderService.checkOrderMoney(0L, trueScore, transmitWay, order);
    if (check == 0) {
      result.put("status", 5009);
      result.put("msg", "订单金额或积分计算有误");
      return result;
    }
    Date date = new Date();
    try {
      //订单状态处理
      order.setPayOrigin(payWay);
      order.setState(1);
      order.setPayState(1);
      order.setTrueScore(trueScore);
      order.setTruePrice(0L);
      order.setPayDate(date);
      if (transmitWay == 1) {
        order.setDeliveryDate(date);
        order.setConfirmDate(date);
        order.setState(3);
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
      new Thread(() -> { //合伙人和商家分润
        orderShareService.onLineOrderShare(order);
      }).start();
      result.put("status", 200);
      result.put("data", order.getId());
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 全金币支付购买金币商品  17/02/20
   *
   * @param orderId     订单id
   * @param trueScore   实际使用金币
   * @param transmitWay 线下自提=1
   * @param payOrigin   支付来源 13=APP全金币|14=公众号全金币
   * @param message     消费者留言
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> orderPayByScoreC(Long orderId, Long trueScore, Integer transmitWay,
                                              Long payOrigin, String message) throws Exception {
    Map<String, Object> result = new HashMap<>();
    OnLineOrder order = orderRepository.findOne(orderId);
    if (order == null) {
      result.put("status", 5006);
      return result;
    }
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(order.getLeJiaUser());
    if (scoreC == null) {
      result.put("status", 6006);
      return result;
    }
    if (scoreC.getScore() < order.getTotalScore()) {
      result.put("status", 6007);
      return result;
    }
    if ((transmitWay == 2 && order.getFreightPrice() != 0)
        || order.getTotalScore().longValue() != trueScore) {
      result.put("status", 5007);
      return result;
    }
    if (order.getState() == 4) {
      result.put("status", 5008);
      return result;
    }
    if (transmitWay != 1 && order.getFreightPrice() != 0) {
      result.put("status", 5009);
      result.put("msg", "订单金额或积分计算有误");
      return result;
    }
    Date date = new Date();
    try {
      //订单状态处理
      order.setPayOrigin(new PayOrigin(payOrigin));
      order.setState(1);
      order.setPayState(1);
      order.setTrueScore(trueScore);
      order.setTruePrice(0L);
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
      result.put("status", 200);
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
   * @param truePrice   实际使用价格
   * @param trueScore   实际使用金币
   * @param transmitWay 物流方式 1=自提
   * @param message     消费者留言
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> completeGoldOrder(Long orderId, Long truePrice, Long trueScore,
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

    //需判断实际使用积分+红包与totalPrice+totalScore是否一致
    if ((truePrice + trueScore) != (onLineOrder.getTotalScore() + onLineOrder.getFreightPrice())) {
      result.put("status", 5009);
      return result;
    }

    onLineOrder.setTruePrice(truePrice);
    onLineOrder.setTransmitWay(transmitWay);
    onLineOrder.setTrueScore(trueScore);
    onLineOrder.setMessage(message);

    orderRepository.saveAndFlush(onLineOrder);
    result.put("status", 200);
    result.put("data", onLineOrder);
    return result;
  }

}
