package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.order.repository.OrderRepository;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.repository.DictionaryRepository;
import com.jifenke.lepluslive.weixin.service.JobThread;
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

import javax.inject.Inject;


/**
 * 线上订单相关 Created by zhangwen on 16/3/20.
 */
@Service
@Transactional(readOnly = true)
public class OrderService {

  @Inject
  private ProductService productService;

  @Inject
  private OrderRepository orderRepository;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private Scheduler scheduler;

  @Inject
  private DictionaryRepository dictionaryRepository;

  @Inject
  private WeixinPayLogService weixinPayLogService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private OnLineOrderShareService orderShareService;

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
   * 普通商品立即购买创建的待支付订单 16/09/24  todo:待删除
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
      Long totalScore = productSpec.getMinScore() * buyNumber.longValue();//订单最高可使用积分(以后不变)
      Long truePrice = productSpec.getMinPrice() * buyNumber;  //该订单最低需使用金额>=totalPrice
      Long freightPrice = 0L;
      //免运费最低价格
      Integer FREIGHT_FREE_PRICE = Integer.parseInt(dictionaryRepository.findOne(1L).getValue());
      //判断是否包邮
      if (orderPrice.intValue() < FREIGHT_FREE_PRICE) { //不满足全场包邮条件
        if (product.getPostage() != 0) { //该产品不包邮
          if (orderPrice.intValue() < product.getFreePrice()) {//不满足此商品包邮最低价
            Long FREIGHT_PRICE = product.getPostage().longValue(); //使用该产品的邮费
            freightPrice = FREIGHT_PRICE;
            totalPrice += FREIGHT_PRICE;
            truePrice += FREIGHT_PRICE;
          }
        }
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
   * 臻品商城 公众号 详情页&购物车 生成订单基本信息 16/09/24
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
    Long totalScore = 0L;//订单最高可使用金币(以后不变)
    Long totalPrice = 0L;    //订单共需付款金额(以后不变)
    Long truePrice = 0L;  //该订单最低需使用金额>=totalPrice
    //免运费最低价格
    Integer FREIGHT_FREE_PRICE = Integer.parseInt(dictionaryRepository.findOne(1L).getValue());
    Long FREIGHT_PRICE = 0L;//邮费
    int free = 0; //是否包邮 1=是
    Long singleSpecTotalPrice = 0L; //某种产品的邮费
    Map<Object, Long[]> postageList = new HashMap<>();
    for (CartDetailDto cartDetailDto : cartDetailDtos) {
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
        totalPrice += productSpec.getMinPrice() * cartDetailDto.getProductNumber();
        truePrice += productSpec.getMinPrice() * cartDetailDto.getProductNumber();
        totalScore += productSpec.getMinScore() * cartDetailDto.getProductNumber();
        singleSpecTotalPrice = productSpec.getPrice() * cartDetailDto.getProductNumber();
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
        if (orderPrice.intValue() < FREIGHT_FREE_PRICE) { //不满足全场包邮条件
          if (product.getPostage() != 0) { //该产品不包邮
            //判断Map中是否有该产品的总价
            if (postageList.get(product.getId()) == null) {
              if (singleSpecTotalPrice < product.getFreePrice()) {//不满足此商品包邮最低价
                Long[] longs = new Long[2];
                longs[0] = singleSpecTotalPrice;
                longs[1] = product.getPostage().longValue();
                postageList.put(product.getId(), longs);
              } else {
                free = 1;
              }
            } else {
              Long[] longs = postageList.get(product.getId());
              longs[0] += singleSpecTotalPrice;
              if (longs[0] >= product.getFreePrice()) {
                free = 1;
              }
            }
          } else {
            free = 1;
          }
        } else {
          free = 1;
        }
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

    //获取邮费
    if (free == 0) {
      for (Map.Entry<Object, Long[]> entry : postageList.entrySet()) {
        Long[] longs = entry.getValue();
        if (longs[1] > FREIGHT_PRICE) {
          FREIGHT_PRICE = longs[1];
        }
      }
    }
    freightPrice = FREIGHT_PRICE;
    totalPrice += FREIGHT_PRICE;
    truePrice += FREIGHT_PRICE;

    onLineOrder.setLeJiaUser(leJiaUser);
    if (address != null) {
      onLineOrder.setAddress(address);
    }
    onLineOrder.setOrderDetails(orderDetails);
    onLineOrder.setState(0);
    onLineOrder.setPayState(0);
    onLineOrder.setType(1);
    onLineOrder.setOrderPrice(orderPrice);
    onLineOrder.setTotalPrice(totalPrice);
    onLineOrder.setTruePrice(truePrice);
    onLineOrder.setTotalScore(totalScore);
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

  /**
   * 商品订单支付成功回调 2017/02/22 zhangwen
   *
   * @param map 支付回调参数
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccess(Map<String, Object> map) {
    String orderSid = (String) map.get("out_trade_no");
    OnLineOrder onLineOrder = orderRepository.findByOrderSid(orderSid);
    if (onLineOrder != null) {
      //保存微信支付日志
      PayOrigin payOrigin = onLineOrder.getPayOrigin();
      String orderType = "onLineOrder";
      if (payOrigin.getPayFrom() == 1) {
        orderType = "APPOnLineOrder";
      }
      weixinPayLogService.savePayLog(map, orderType, 1);
      if (onLineOrder.getType() != null && onLineOrder.getType() == 1) { //普通订单处理
        paySuccessByScore(onLineOrder);
        return;
      }
      //金币订单处理
      paySuccessByGold(onLineOrder);
    }
  }

  /**
   * 普通订单支付成功执行 2017/02/22 zhangwen
   *
   * @param onLineOrder 支付成功后,a积分加,C积分减,修改订单状态为已支付  并分润  16/11/05
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccessByScore(OnLineOrder onLineOrder) {
    PayOrigin payOrigin = onLineOrder.getPayOrigin();
    PayOrigin payWay = new PayOrigin();
    System.out.println(onLineOrder.getState() + "之前");
    if (onLineOrder.getState() == 0 || onLineOrder.getState() == 4) {
      Integer PAY_BACK_SCALE = Integer.parseInt(dictionaryRepository.findOne(3L).getValue());
      Long
          payBackScore =
          (long) Math.ceil((double) (onLineOrder.getTruePrice() * PAY_BACK_SCALE) / 100);
      onLineOrder.setState(1);
      onLineOrder.setPayState(1);
      onLineOrder.setSource(1);
      onLineOrder.setPayDate(new Date());
      onLineOrder.setPayBackA(payBackScore);
      LeJiaUser user = onLineOrder.getLeJiaUser();
      scoreAService.paySuccess(user, payBackScore, onLineOrder.getOrderSid());
      try {
        if (onLineOrder.getTrueScore() != 0) {
          if (payOrigin.getId() == 1) {
            payWay.setId(11L);
          } else {
            payWay.setId(12L);
          }

          //减金币及添加金币记录
          ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(user);
          scoreCService.saveScoreC(scoreC, 0, onLineOrder.getTrueScore());
          scoreCService.saveScoreCDetail(scoreC, 0, onLineOrder.getTrueScore(), 2, "臻品商城消费",
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
        productService.editProductSaleByPayOrder(onLineOrder);
        //如果返还A红包不为0,改变会员状态
        if (payBackScore > 0) {
          weiXinUserService.setWeiXinState(user);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      final long id = onLineOrder.getId();
      new Thread(() -> { //合伙人和商家分润
        orderShareService.onLineOrderShare(id);
      }).start();

      System.out.println(onLineOrder.getState() + "之后");
      orderRepository.save(onLineOrder);
    }
  }

  /**
   * 金币类订单支付成功执行 2017/02/22 zhangwen
   *
   * @param onLineOrder 支付成功后,a积分加,C积分减,修改订单状态为已支付   17/02/22
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccessByGold(OnLineOrder onLineOrder) {
    PayOrigin payOrigin = onLineOrder.getPayOrigin();
    PayOrigin payWay = new PayOrigin();
    System.out.println(onLineOrder.getState() + "之前");
    if (onLineOrder.getState() == 0) {
      onLineOrder.setState(1);
      onLineOrder.setPayState(1);
      onLineOrder.setSource(1);
      onLineOrder.setPayDate(new Date());
      LeJiaUser user = onLineOrder.getLeJiaUser();
      try {
        if (onLineOrder.getTrueScore() != 0) {
          if (payOrigin.getId() == 1) {
            payWay.setId(11L);
          } else {
            payWay.setId(12L);
          }
          //减金币及添加金币记录
          ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(user);
          scoreCService.saveScoreC(scoreC, 0, onLineOrder.getTrueScore());
          scoreCService.saveScoreCDetail(scoreC, 0, onLineOrder.getTrueScore(), 2, "金币商城消费",
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
        productService.editProductSaleByPayOrder(onLineOrder);
        Long payBackScore = onLineOrder.getPayBackA();
        //如果返还A红包不为0,发红包并改变会员状态
        if (payBackScore != null && payBackScore > 0) {
          scoreAService.paySuccess(user, payBackScore, onLineOrder.getOrderSid());
          weiXinUserService.setWeiXinState(user);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println(onLineOrder.getState() + "之后");
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
   * 臻品商城 公众号&APP 订单确认页 输入实际使用金额和金币 16/09/29
   *
   * @param orderId     订单id
   * @param truePrice   实际使用价格
   * @param trueScore   实际使用金币
   * @param transmitWay 物流方式 1=自提
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Object> setPriceScoreForOrder(Long orderId, Long truePrice, Long trueScore,
                                                   Integer transmitWay) {
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
    if (scoreC == null) {
      result.put("status", 6001);
      return result;
    }
    if (scoreC.getScore() < trueScore) {
      result.put("status", 6002);
      return result;
    }

    //需判断实际使用积分+红包与totalPrice+totalScore是否一致
    int check = checkOrderMoney(truePrice, trueScore, transmitWay, onLineOrder);
    if (check == 0) {
      result.put("status", 5009);
      return result;
    }

    if (onLineOrder.getTrueScore() != 0 && !onLineOrder.getTrueScore().equals(trueScore)) {
      onLineOrder.setOrderSid(MvUtil.getOrderNumber()); //当订单参数变化时，必须重新设置,否者导致微信订单号重复报错
    }

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
      if ((onLineOrder.getTotalPrice() + onLineOrder.getTotalScore() - onLineOrder
          .getFreightPrice()) != (truePrice + trueScore)) {
        return 0;
      }
    } else {
      if ((onLineOrder.getTotalPrice() + onLineOrder.getTotalScore()) != (truePrice + trueScore)) {
        return 0;
      }
    }
    return 1;
  }

//  /**
//   * 查询订单是否支付完成防止掉单 16/09/29
//   *
//   * @param id        订单id
//   * @param orderType 订单来源 1=公众号订单|2=APP订单
//   */
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  public void orderStatusQuery(Long id, Integer orderType) {
//    OnLineOrder onLineOrder = orderRepository.findOne(id);
//    if (onLineOrder != null) {
//      if (onLineOrder.getState() == 4 || onLineOrder.getState() == 0) {
//        //调接口查询订单是否支付完成
//        SortedMap<String, Object> map = null;
//        String currOrderType;
//        if (orderType == 1) {
//          map = weiXinPayService.buildOrderQueryParams(onLineOrder);
//          currOrderType = "onLineOrder";
//        } else {
//          map = weiXinPayService.buildAPPOrderQueryParams(onLineOrder);
//          currOrderType = "APPOnLineOrder";
//        }
//        Map<String, Object> orderMap = weiXinPayService.orderStatusQuery(map);
//        String returnCode = (String) orderMap.get("return_code");
//        String resultCode = (String) orderMap.get("result_code");
//        String tradeState = (String) orderMap.get("trade_state");
//        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode) && "SUCCESS"
//            .equals(tradeState)) {
//          //保存微信掉单日志
//          weixinPayLogService.savePayLog(orderMap, currOrderType, 2);
//          //对订单进行处理
//          paySuccessByScore(onLineOrder);
//        }
//      }
//    }
//  }
}
