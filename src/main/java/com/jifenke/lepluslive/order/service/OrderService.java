package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.controller.dto.OrderShare;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.order.repository.OrderRepository;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductRebatePolicy;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductRebatePolicyService;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
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

import java.net.URLDecoder;
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
  private WeixinPayLogService weixinPayLogService;

  @Inject
  private OnLineOrderShareService orderShareService;

  @Inject
  private AddressService addressService;

  @Inject
  private ProductRebatePolicyService productRebatePolicyService;

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
      map.putIfAbsent("status_" + i, 0);
    }
    return map;
  }

  /**
   * 商品订单支付成功回调 2017/02/22 zhangwen
   *
   * @param map 支付回调参数
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void paySuccess(Map<String, Object> map) throws Exception {
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
   * 臻品订单支付成功执行 2017/02/22 zhangwen
   *
   * @param onLineOrder 支付成功后,a积分加,C积分减,修改订单状态为已支付  并分润
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void paySuccessByScore(OnLineOrder onLineOrder) throws Exception {
    PayOrigin payOrigin = onLineOrder.getPayOrigin();
    PayOrigin payWay = new PayOrigin();
    if (onLineOrder.getState() == 0 || onLineOrder.getState() == 4) {

      OrderShare share = productRebatePolicyService.getByPaySuccess(onLineOrder);
      Long rebateScore = share.getRebateScore();
      onLineOrder.setState(1);
      onLineOrder.setPayState(1);
      onLineOrder.setSource(1);
      onLineOrder.setPayDate(new Date());
      onLineOrder.setPayBackA(rebateScore);
      LeJiaUser user = onLineOrder.getLeJiaUser();
      if (rebateScore > 0) {
        ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(onLineOrder.getLeJiaUser());
        scoreAService.saveScore(scoreA, 1, rebateScore);
        scoreAService
            .saveScoreDetail(scoreA, 1, rebateScore, 1, "乐+臻品商城返鼓励金", onLineOrder.getOrderSid());
      }
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
      } catch (Exception e) {
        e.printStackTrace();
      }
      share.setUserId(onLineOrder.getLeJiaUser().getId());
      share.setOrderSid(onLineOrder.getOrderSid());
      new Thread(() -> { //合伙人和商家分润
        orderShareService.share(share);
      }).start();

      orderRepository.save(onLineOrder);
    }
  }

  /**
   * 金币订单支付成功执行 2017/02/22 zhangwen
   *
   * @param onLineOrder 支付成功后,a积分加,C积分减,修改订单状态为已支付   17/02/22
   */
  @Transactional(propagation = Propagation.REQUIRED)
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
          ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(onLineOrder.getLeJiaUser());
          scoreAService.saveScore(scoreA, 1, payBackScore);
          scoreAService
              .saveScoreDetail(scoreA, 1, payBackScore, 1, "乐+金币商城返鼓励金", onLineOrder.getOrderSid());
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

  @Transactional(propagation = Propagation.REQUIRED)
  public void orderCancle(Long orderId) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    productService.orderCancle(orderDetails);
    onLineOrder.setState(4);
    orderRepository.save(onLineOrder);
  }

  @Transactional(propagation = Propagation.REQUIRED)
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


  @Transactional(propagation = Propagation.REQUIRED)
  public void confrimOrder(Long orderId) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    onLineOrder.setState(3);
    onLineOrder.setConfirmDate(new Date());
    orderRepository.save(onLineOrder);
  }


  @Transactional(propagation = Propagation.REQUIRED)
  public void editAddress(OnLineOrder onLineOrder) {
    orderRepository.save(onLineOrder);
  }

  /**
   * 臻品订单提交 公众号&APP 17/06/27
   *
   * @param orderId     订单id
   * @param payWay      5=公众号|1=APP
   * @param trueScore   实际使用金币
   * @param transmitWay 物流方式 1=自提
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Map<String, Object> submitOrder(Long orderId, Long payWay, Long trueScore,
                                         Integer transmitWay) {
    Map<String, Object> result = new HashMap<>();
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);

    if (onLineOrder.getState() == 1) { //订单已经支付
      result.put("status", 5012);
      return result;
    }
    if (onLineOrder.getState() == 4) { //订单已经失效
      result.put("status", 5009);
      return result;
    }

    if (trueScore < 0 || trueScore > onLineOrder.getTotalPrice()) {
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
    long truePrice = onLineOrder.getTotalPrice() - trueScore;
    if (transmitWay == 1 && onLineOrder.getFreightPrice() > 0) {
      truePrice = truePrice - onLineOrder.getFreightPrice();
      onLineOrder.setFreightPrice(0L);
    }

    if (onLineOrder.getTrueScore() != 0 && !onLineOrder.getTrueScore().equals(trueScore)) {
      onLineOrder.setOrderSid(MvUtil.getOrderNumber()); //当订单参数变化时，必须重新设置,否者导致微信订单号重复报错
    }

    onLineOrder.setTruePrice(truePrice);
    onLineOrder.setTransmitWay(transmitWay);
    onLineOrder.setTrueScore(trueScore);
    onLineOrder.setPayOrigin(new PayOrigin(payWay));

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
   * 购买商品数据转换
   */
  public List<CartDetailDto> stringToList(String cartDetails) {
    try {
      cartDetails = URLDecoder.decode(cartDetails, "utf8");
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<CartDetailDto> detailDtoList = new ArrayList<>();
    String[] details = cartDetails.split(",");
    for (String detail : details) {
      String[] s = detail.split("_");
      CartDetailDto cartDetailDto = new CartDetailDto();
      Product product = new Product();
      ProductSpec productSpec = new ProductSpec();
      product.setId(Long.parseLong(s[0]));
      productSpec.setId(Long.parseLong(s[1]));
      cartDetailDto.setProduct(product);
      cartDetailDto.setProductSpec(productSpec);
      cartDetailDto.setProductNumber(Integer.parseInt(s[2]));
      detailDtoList.add(cartDetailDto);
    }
    return detailDtoList;
  }

  /**
   * 创建臻品订单  2017/6/27
   *
   * @param carts     商品信息
   * @param leJiaUser 用户
   * @param source    APP||WEB
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Map<String, Object> createOrder(String carts, LeJiaUser leJiaUser, String source) {
    List<CartDetailDto> detailList = stringToList(carts);
    //payWayId  订单来源 5=公众号|1=APP
    long payWayId = 5;
    if ("APP".equalsIgnoreCase(source)) {
      payWayId = 1;
    }
    Address address = addressService.findAddressByLeJiaUserAndState(leJiaUser);

    Map<String, Object> result = new HashMap<>();
    OnLineOrder onLineOrder = new OnLineOrder();
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    Long orderPrice = 0L;
    Long freightPrice = 0L;
    Long totalScore = 0L;//订单最高可使用金币(以后不变)
    Long totalPrice = 0L;    //订单共需付款金额(以后不变) 包括邮费
    Long truePrice = 0L;  //该订单最低需使用金额 不包括邮费
    Long FREIGHT_PRICE = 0L;//邮费
    int free = 0; //是否包邮 1=是
    Long singleSpecTotalPrice = 0L; //某种产品的邮费
    long payBackA = 0;
    Map<Long, Long[]> postageList = new HashMap<>();
    for (CartDetailDto cartDetailDto : detailList) {
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
        totalPrice += productSpec.getPrice() * cartDetailDto.getProductNumber();
        truePrice += productSpec.getMinPrice() * cartDetailDto.getProductNumber();
        totalScore += productSpec.getMinScore() * cartDetailDto.getProductNumber();
        singleSpecTotalPrice = productSpec.getPrice() * cartDetailDto.getProductNumber();

        ProductRebatePolicy
            policy =
            productRebatePolicyService.findByProductSpec(productSpec);
        if (policy != null) {
          payBackA += policy.getRebateScore() * cartDetailDto.getProductNumber();
        }
      } else {
        result.put("status", 5005); //某件商品的某种规格无库存了
        String[] o = new String[1];
        o[0] = product.getName();
        result.put("arrays", o);
        result.put("data", product.getName() + "_" + cartDetailDto.getProductNumber());
        break;
      }
      //判断是否包邮
      if (free == 0) {
        if (product.getPostage() != 0) { //该产品不包邮
          //判断Map中是否有该产品的总价
          if (postageList.get(product.getId()) == null) {
            if (product.getFreePrice() == 0 || singleSpecTotalPrice < product
                .getFreePrice()) {//不满足此商品包邮最低价
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
      for (Map.Entry<Long, Long[]> entry : postageList.entrySet()) {
        Long[] longs = entry.getValue();
        if (longs[1] > FREIGHT_PRICE) {
          FREIGHT_PRICE = longs[1];
        }
      }
    }
    freightPrice = FREIGHT_PRICE;
    totalPrice += FREIGHT_PRICE;

    onLineOrder.setLeJiaUser(leJiaUser);
    if (address != null) {
      onLineOrder.setAddress(address);
    }
    onLineOrder.setOrderDetails(orderDetails);
    onLineOrder.setOrderPrice(orderPrice);
    onLineOrder.setTotalPrice(totalPrice);
    onLineOrder.setTruePrice(truePrice);
    onLineOrder.setTotalScore(totalScore);
    onLineOrder.setFreightPrice(freightPrice);
    onLineOrder.setPayBackA(payBackA);
    onLineOrder.setPayOrigin(new PayOrigin(payWayId));//设置支付来源
    orderRepository.save(onLineOrder);

    JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
    jobThread.start();

    result.put("status", 200);
    result.put("data", onLineOrder);
    return result;
  }
}
