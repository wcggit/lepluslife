package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.controller.dto.OrderDto;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.order.repository.OrderRepository;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.DictionaryRepository;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.JobThread;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;
import com.jifenke.lepluslive.weixin.service.WeixinPayLogService;

import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import javax.inject.Inject;


/**
 * Created by wcg on 16/3/20.
 */
@Service
@Transactional(readOnly = true)
public class OrderService {

  @Inject
  private ProductService productService;

  @Inject
  private AddressService addressService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private OrderRepository orderRepository;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private Scheduler scheduler;

  @Inject
  private DictionaryRepository dictionaryRepository;

  @Inject
  private WeixinPayLogService weixinPayLogService;

  @Inject
  private WeiXinUserService weiXinUserService;

  /**
   * 我的 获取用户不同状态订单数  16/09/05
   *
   * @param leJiaUserId leJiaUserId
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Map getOrdersCount(Long leJiaUserId) {
    List<Object[]> list = orderRepository.getOrdersCount(leJiaUserId);
    Map<String, Object> map = new HashMap<>();
    for (Object[] o : list) {
      map.put("status_" + o[0], o[1]);
    }
    for (int i = 0; i < 3; i++) {
      if (map.get("status_" + i) == null) {
        map.put("status_" + i, 0);
      }
    }
    return map;
  }

  /**
   * 普通商品立即购买创建的待支付订单 16/09/24
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
  public Map createBuyOrder(Long productId, Long specId, Integer buyNumber, LeJiaUser leJiaUser,
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
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    //订单信息
    try {
      Long orderPrice = productSpec.getPrice() * buyNumber;
      Long totalPrice = productSpec.getMinPrice() * buyNumber;    //订单共需付款金额(以后不变)
      Long totalScore = orderPrice - totalPrice;//订单最高可使用积分(以后不变)
      Long truePrice = productSpec.getMinPrice() * buyNumber;  //该订单最低需使用金额>=totalPrice
      Long freightPrice = 0L;
      //免运费最低价格
      Integer FREIGHT_FREE_PRICE = Integer.parseInt(dictionaryRepository.findOne(1L).getValue());
      //判断是否包邮
      if (orderPrice.intValue() < FREIGHT_FREE_PRICE) {
        Long FREIGHT_PRICE = Long.parseLong(dictionaryRepository.findOne(2L).getValue());
        freightPrice = FREIGHT_PRICE;
        totalPrice += FREIGHT_PRICE;
        truePrice += FREIGHT_PRICE;
      }

      onLineOrder.setLeJiaUser(leJiaUser);
      if (address != null) {
        onLineOrder.setAddress(address);
      }
      onLineOrder.setFreightPrice(freightPrice);
      onLineOrder.setTotalPrice(totalPrice);
      onLineOrder.setTotalScore(
          (long) Math.floor(Double.parseDouble(totalScore.toString()) / 100));
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
   * 购物车生成订单基本信息 16/09/24
   *
   * @param cartDetailDtos 订单详情
   * @param leJiaUser      用户
   * @param address        地址
   * @param payWayId       订单来源
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> createCartOrder(List<CartDetailDto> cartDetailDtos,
                                             LeJiaUser leJiaUser,
                                             Address address, Long payWayId) {
    Map<String, Object> result = new HashMap<>();
    OnLineOrder onLineOrder = new OnLineOrder();
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    Long orderPrice = 0L;
    Long freightPrice = 0L;
    Long totalScore = 0L;//订单最高可使用积分(以后不变)
    Long totalPrice = 0L;    //订单共需付款金额(以后不变)
    Long truePrice = 0L;  //该订单最低需使用金额>=totalPrice
    for (CartDetailDto cartDetailDto : cartDetailDtos) {
      OrderDetail orderDetail = new OrderDetail();
      Product product = productService.findOneProduct(cartDetailDto.getProduct().getId());
      ProductSpec productSpec = productService.editProductSpecRepository(
          cartDetailDto.getProductSpec().getId(),
          cartDetailDto.getProductNumber());
      if (productSpec != null) {
        orderPrice += productSpec.getPrice() * cartDetailDto.getProductNumber();
        totalPrice += productSpec.getMinPrice() * cartDetailDto.getProductNumber();
        truePrice += productSpec.getMinPrice() * cartDetailDto.getProductNumber();
        totalScore +=
            (productSpec.getPrice() - productSpec.getMinPrice()) * cartDetailDto.getProductNumber();
      } else {
        result.put("status", 5005); //某件商品的某种规格无库存了
        result.put("data", product.getName() + "_" + cartDetailDto.getProductNumber());
        break;
      }
      orderDetail.setState(1);
      orderDetail.setProduct(product);
      orderDetail.setProductNumber(cartDetailDto.getProductNumber());
      orderDetail.setProductSpec(productSpec);
      orderDetail.setOnLineOrder(onLineOrder);
      orderDetails.add(orderDetail);
    }
    //判断是否某件商品库存不足
    if (result.get("status") != null) {
      return result;
    }
    //免运费最低价格
    Integer FREIGHT_FREE_PRICE = Integer.parseInt(dictionaryRepository.findOne(1L).getValue());
    //判断是否包邮
    if (orderPrice.intValue() < FREIGHT_FREE_PRICE) {
      Long FREIGHT_PRICE = Long.parseLong(dictionaryRepository.findOne(2L).getValue());
      freightPrice = FREIGHT_PRICE;
      totalPrice += FREIGHT_PRICE;
      truePrice += FREIGHT_PRICE;
    }
    onLineOrder.setLeJiaUser(leJiaUser);
    if (address != null) {
      onLineOrder.setAddress(address);
    }
    onLineOrder.setOrderDetails(orderDetails);
    onLineOrder.setState(0);
    onLineOrder.setPayState(0);
    onLineOrder.setOrderPrice(orderPrice);
    onLineOrder.setTotalPrice(totalPrice);
    onLineOrder.setTruePrice(truePrice);
    onLineOrder.setTotalScore((long) Math.floor(Double.parseDouble(totalScore.toString()) / 100));
    onLineOrder.setFreightPrice(freightPrice);
    PayOrigin payOrigin = new PayOrigin(payWayId); //设置支付来源
    onLineOrder.setPayOrigin(payOrigin);
    orderRepository.save(onLineOrder);

    JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
    jobThread.start();

    result.put("status", 200);
    result.put("data", onLineOrder);
    return result;
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String orderConfirm(Long orderId, Long addressId) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    onLineOrder.setAddress(addressService.findOneAddress(addressId));
    orderRepository.save(onLineOrder);
    return onLineOrder.getOrderSid();
  }


  public OnLineOrder findOrderBySid(String orderSid) {
    return orderRepository.findByOrderSid(orderSid);
  }

  /**
   * @param orderSid 支付成功后,a积分加,b积分减,修改订单状态为已支付
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccess(String orderSid) {
    OnLineOrder onLineOrder = orderRepository.findByOrderSid(orderSid);
    //保存微信支付日志
    weixinPayLogService.savePayLog(onLineOrder);
    PayOrigin payOrigin = onLineOrder.getPayOrigin();
    PayOrigin payWay = new PayOrigin();
    System.out.println(onLineOrder.getState() + "之前");
    if (onLineOrder.getState() == 0) {
      Integer PAY_BACK_SCALE = Integer.parseInt(dictionaryRepository.findOne(3L).getValue());
      Long
          payBackScore =
          (long) Math.ceil((double) (onLineOrder.getTruePrice() * PAY_BACK_SCALE) / 100);
      onLineOrder.setState(1);
      onLineOrder.setPayState(1);
      onLineOrder.setPayDate(new Date());
      onLineOrder.setPayBackA(payBackScore);
      LeJiaUser user = onLineOrder.getLeJiaUser();
      scoreAService.paySuccess(user, payBackScore, onLineOrder.getOrderSid());
      if (onLineOrder.getTrueScore() != 0) {
        if (payOrigin.getId() == 1) {
          payWay.setId(4L);
        } else {
          payWay.setId(8L);
        }
        scoreBService.paySuccess(user, onLineOrder.getTrueScore(), onLineOrder.getOrderSid());
      } else {
        if (payOrigin.getId() == 1) {
          payWay.setId(2L);
        } else {
          payWay.setId(6L);
        }
      }
      onLineOrder.setPayOrigin(payWay);
      //订单相关product的销量等数据处理
      try {
        productService.editProductSaleByPayOrder(onLineOrder);
        //如果返还A红包不为0,改变会员状态
        if (payBackScore > 0) {
          WeiXinUser w = user.getWeiXinUser();
          if (w != null) {
            w.setState(1);
            weiXinUserService.saveWeiXinUser(w);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      System.out.println(onLineOrder.getState() + "之后");
      orderRepository.save(onLineOrder);
    }
  }

  /**
   * @param onLineOrder 支付成功后,a积分加,b积分减,修改订单状态为已支付
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccessQuery(OnLineOrder onLineOrder) {
    if (onLineOrder.getState() == 0 || onLineOrder.getState() == 4) {
      Integer PAY_BACK_SCALE = Integer.parseInt(dictionaryRepository.findOne(3L).getValue());
      Long
          payBackScore =
          (long) Math.ceil((double) (onLineOrder.getTruePrice() * PAY_BACK_SCALE) / 100);
      PayOrigin payOrigin = onLineOrder.getPayOrigin();
      PayOrigin payWay = new PayOrigin();
      onLineOrder.setState(1);
      onLineOrder.setPayState(1);
      onLineOrder.setPayDate(new Date());
      onLineOrder.setPayBackA(payBackScore);
      scoreAService.paySuccess(onLineOrder.getLeJiaUser(), payBackScore, onLineOrder.getOrderSid());
      if (onLineOrder.getTrueScore() != 0) {
        if (payOrigin.getId() == 1) {
          payWay.setId(4L);
        } else {
          payWay.setId(8L);
        }
        scoreBService.paySuccess(onLineOrder.getLeJiaUser(), onLineOrder.getTrueScore(),
                                 onLineOrder.getOrderSid());
      } else {
        if (payOrigin.getId() == 1) {
          payWay.setId(2L);
        } else {
          payWay.setId(6L);
        }
      }
      onLineOrder.setPayOrigin(payWay);
      //订单相关product的销量等数据处理
      try {
        productService.editProductSaleByPayOrder(onLineOrder);
      } catch (Exception e) {
        e.printStackTrace();
      }
      orderRepository.save(onLineOrder);
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<OnLineOrder> getCurrentUserOrders(LeJiaUser leJiaUser, Integer status) {
    List<OnLineOrder> list = null;
    //订单状态(0=待付款|1=待发货|2=待收货|3=已完成|5=全部)
    if (5 == status) {
      List<Integer> states = new ArrayList<>();
      states.add(-1);
      states.add(4);
      list = orderRepository
          .findAllByLeJiaUserAndStateNotInOrderByCreateDateDesc(leJiaUser, states);
    } else {
      list = orderRepository.findAllByLeJiaUserAndStateOrderByCreateDateDesc(leJiaUser, status);
    }
    return list;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void orderCancle(Long orderId) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    productService.orderCancle(orderDetails);
    onLineOrder.setState(4);
    orderRepository.save(onLineOrder);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void checkOderState(Long orderId) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    if (onLineOrder != null) {
      if (onLineOrder.getState() == 0) {
        onLineOrder.setState(4);
        orderRepository.save(onLineOrder);
        productService.orderCancle(onLineOrder.getOrderDetails());
      }
      if (onLineOrder.getState() == -1) {
        orderRepository.delete(onLineOrder);
      }
    }
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void confrimOrder(Long orderId) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    onLineOrder.setState(3);
    onLineOrder.setConfirmDate(new Date());
    orderRepository.save(onLineOrder);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void editAddress(OnLineOrder onLineOrder) {
    orderRepository.save(onLineOrder);
  }

  /**
   * 订单页提交,输入实际使用金额和积分 16/09/29
   *
   * @param orderId     订单id
   * @param truePrice   实际使用价格
   * @param trueScore   实际使用积分
   * @param transmitWay 物流方式 1=自提
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<Object, Object> setPriceScoreForOrder(Long orderId, Long truePrice, Long trueScore,
                                                   Integer transmitWay) {
    Map<Object, Object> result = new HashMap<>();
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    if (onLineOrder.getState() == 4) { //订单已经失效
      result.put("status", 5008);
      return result;
    }

    //需判断实际使用积分+红包与totalPrice+totalScore是否一致
    int check = checkOrderMoney(truePrice, trueScore, transmitWay, onLineOrder);
    if (check == 0) {
      result.put("status", 5009);
      return result;
    }

    onLineOrder.setOrderSid(MvUtil.getOrderNumber()); //必须重新设置,否者导致微信订单号重复报错
    onLineOrder.setTruePrice(truePrice);
    onLineOrder.setTransmitWay(transmitWay);
    onLineOrder.setTrueScore(trueScore);

    orderRepository.saveAndFlush(onLineOrder);
    result.put("status", 200);
    result.put("data", onLineOrder);
    return result;
  }

  public Long getCurrentUserObligationOrdersCount(LeJiaUser leJiaUser) {
    return orderRepository.
        getCurrentUserObligationOrdersCount(leJiaUser.getId());
  }

  /**
   * 根据订单id查询线上订单信息
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public OnLineOrder findOnLineOrderById(Long id) {
    return orderRepository.findOne(id);
  }


  /**
   * 查询订单是否支付完成防止掉单 16/09/29
   *
   * @param id        订单id
   * @param orderType 订单来源 1=公众号订单|2=APP订单
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void orderStatusQuery(Long id, Integer orderType) {
    OnLineOrder onLineOrder = orderRepository.findOne(id);
    if (onLineOrder != null) {
      if (onLineOrder.getState() == 4 || onLineOrder.getState() == 0) {
        //调接口查询订单是否支付完成
        SortedMap<Object, Object> map = null;
        String currOrderType;
        if (orderType == 1) {
          map = weiXinPayService.buildOrderQueryParams(onLineOrder);
          currOrderType = "onLineOrder";
        } else {
          map = weiXinPayService.buildAPPOrderQueryParams(onLineOrder);
          currOrderType = "APPOnLineOrder";
        }
        Map orderMap = weiXinPayService.orderStatusQuery(map);
        String returnCode = (String) orderMap.get("return_code");
        String resultCode = (String) orderMap.get("result_code");
        String tradeState = (String) orderMap.get("trade_state");
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode) && "SUCCESS"
            .equals(tradeState)) {
          //保存微信掉单日志
          weixinPayLogService
              .savePayLog(onLineOrder.getOrderSid(), returnCode, resultCode, tradeState,
                          currOrderType);
          //对订单进行处理
          paySuccessQuery(onLineOrder);
        }
      }
    }
  }

  /**
   * 检测订单实付金额和积分是否正确  2016/10/9
   *
   * @param truePrice   实付金额
   * @param trueScore   实付积分
   * @param transmitWay 取货方式 1=自提
   * @param onLineOrder 订单信息
   * @return 1=ok
   */
  public int checkOrderMoney(Long truePrice, Long trueScore, Integer transmitWay,
                             OnLineOrder onLineOrder) {
    if (transmitWay == 1) {
      if ((onLineOrder.getTotalPrice() + onLineOrder.getTotalScore() * 100 - onLineOrder
          .getFreightPrice()) != (truePrice + trueScore * 100)) {
        return 0;
      }
    } else {
      if ((onLineOrder.getTotalPrice() + onLineOrder.getTotalScore() * 100) != (truePrice
                                                                                + trueScore
                                                                                  * 100)) {
        return 0;
      }
    }
    return 1;
  }

  //APP立即购买操作  即将改版
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  public OnLineOrder createOrder(OrderDto orderDto, LeJiaUser leJiaUser, Address address,
//                                 Long payWayId, Integer FREIGHT_FREE_PRICE) {
//    Product product = productService.findOneProduct(orderDto.getProductId());
////    productService.editProductSpecRepository(orderDto.getProductSpec(), orderDto.getProductNum());
//    OnLineOrder onLineOrder = new OnLineOrder();
//    ProductSpec productSpec = productService.findOneProductSpec(orderDto.getProductSpec());
//    Long totalPrice = productSpec.getPrice() * orderDto.getProductNum();
//    Long totalMinPrice = productSpec.getMinPrice() * orderDto.getProductNum();
//    Long totalScore = totalPrice - totalMinPrice;
//    //判断是否包邮
//    if (totalPrice.intValue() >= FREIGHT_FREE_PRICE) {
//      onLineOrder.setFreightPrice(0L);
//      onLineOrder.setTotalPrice(totalPrice);
//    } else {
//      Long FREIGHT_PRICE = Long.parseLong(dictionaryRepository.findOne(2L).getValue());
//      onLineOrder.setFreightPrice(FREIGHT_PRICE);
//      onLineOrder.setTotalPrice(totalPrice + FREIGHT_PRICE);
//    }
//    onLineOrder.setTruePrice(totalPrice);
////    onLineOrder.setFreightPrice(0L);
////    onLineOrder.setTotalPrice(1L);
////    onLineOrder.setTruePrice(1L);
//
//    onLineOrder.setTotalScore(
//        (long) Math.floor(Double.parseDouble(totalScore.toString()) / 100));
//    onLineOrder.setLeJiaUser(leJiaUser);
//    onLineOrder.setState(-1);
//    onLineOrder.setAddress(address);
//    PayOrigin payOrigin = new PayOrigin(payWayId);  //设置支付来源
//    onLineOrder.setPayOrigin(payOrigin);
//    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
//    OrderDetail orderDetail = new OrderDetail();
//
//    orderDetail.setProduct(product);
//    orderDetail.setState(1);
//    orderDetail.setProductNumber(orderDto.getProductNum());
//    orderDetail.setProductSpec(productSpec);
//    orderDetail.setOnLineOrder(onLineOrder);
//    orderDetails.add(orderDetail);
//
//    orderRepository.save(onLineOrder);
//
//    //====创建订单后,生成quartz任务
////    createOrderScheduler(order.getId());
//    JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
//    jobThread.start();
//
//    return onLineOrder;
//  }

  // 2016/10/9注释
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  public OnLineOrder findOrderById(Long orderId, Boolean flag) {
//    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
//    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails().stream().filter((orderDetail) ->
//                                                                                       orderDetail
//                                                                                           .getState()
//                                                                                       == 0
//    ).collect(Collectors.toList());
//
//    if (orderDetails.size() == onLineOrder.getOrderDetails().size()) {
//      orderRepository.delete(onLineOrder);
//    }
//
//    if (flag == null) {
//      flag = false;
//    }
//    if (flag) {
//      onLineOrder.getOrderDetails().removeAll(orderDetails);
//    }
//
//    return onLineOrder;
//  }

//  /**
//   * 5分钟后查询订单是否支付完成防止掉单
//   */
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  public void startOrderStatusQueryJob(Long orderId) {
//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//          Date time = sdf.parse(sdf.format(new Date().getTime() + Constants.ORDER_QUERY));
//          JobDetail orderStatusQuery = JobBuilder.newJob(OrderStatusQueryJob.class)
//              .withIdentity("orderStatusQuery" + orderId, jobGroupName)
//              .usingJobData("orderId", orderId)
//              .build();
//          Trigger orderStatusQueryTrigger = TriggerBuilder.newTrigger()
//              .withIdentity(
//                  TriggerKey.triggerKey("orderStatusQueryJobTrigger"
//                                        + orderId, triggerGroupName))
//              .startAt(time)
//              .build();
//          scheduler.scheduleJob(orderStatusQuery, orderStatusQueryTrigger);
//          scheduler.start();
//
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//    }).start();
//  }

  //  /**
//   * 购物车生成订单基本信息 原来的，待删除
//   */
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  public OnLineOrder createCartOrder(List<CartDetailDto> cartDetailDtos, LeJiaUser leJiaUser,
//                                     Address address, Integer FREIGHT_FREE_PRICE) {
//    OnLineOrder onLineOrder = new OnLineOrder();
//    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
//    Long totalPrice = 0L;
//    Long totalScore = 0L;
//    for (CartDetailDto cartDetailDto : cartDetailDtos) {
//      OrderDetail orderDetail = new OrderDetail();
//      Product product = productService.findOneProduct(cartDetailDto.getProduct().getId());
//      ProductSpec productSpec = productService.editProductSpecRepository(
//          cartDetailDto.getProductSpec().getId(),
//          cartDetailDto.getProductNumber());
//      if (productSpec != null) {
//        totalPrice += productSpec.getPrice() * cartDetailDto.getProductNumber();
//        totalScore +=
//            (productSpec.getPrice() - productSpec.getMinPrice()) * cartDetailDto.getProductNumber();
//        orderDetail.setState(1);
//      } else {
//        orderDetail.setState(0);
//      }
//      orderDetail.setProduct(product);
//      orderDetail.setProductNumber(cartDetailDto.getProductNumber());
//      orderDetail.setProductSpec(productSpec);
//      orderDetail.setOnLineOrder(onLineOrder);
//      orderDetails.add(orderDetail);
//    }
//    //判断是否包邮
//    if (totalPrice.intValue() >= FREIGHT_FREE_PRICE) {
//      onLineOrder.setFreightPrice(0L);
//      onLineOrder.setTotalPrice(totalPrice);
//    } else {
//      Long FREIGHT_PRICE = Long.parseLong(dictionaryRepository.findOne(2L).getValue());
//      onLineOrder.setFreightPrice(FREIGHT_PRICE);
//      onLineOrder.setTotalPrice(totalPrice + FREIGHT_PRICE);
//    }
//    onLineOrder.setLeJiaUser(leJiaUser);
//    if (address != null) {
//      onLineOrder.setAddress(address);
//    }
//    onLineOrder.setOrderDetails(orderDetails);
//    onLineOrder.setState(0);
//    onLineOrder.setTruePrice(totalPrice);
//    PayOrigin payOrigin = new PayOrigin(5L); //设置支付来源
//    onLineOrder.setPayOrigin(payOrigin);
//
//    onLineOrder.setTotalScore((long) Math.floor(Double.parseDouble(totalScore.toString()) / 100));
//    orderRepository.save(onLineOrder);
//    JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
//    jobThread.start();
//    return onLineOrder;
//  }
}
