package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.repository.OrderDetailRepository;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.controller.dto.OrderDto;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.order.repository.OrderRepository;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.service.JobThread;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
  private WeiXinService weiXinService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private OrderRepository orderRepository;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private Scheduler scheduler;

  @Inject
  private OrderDetailRepository orderDetailRepository;

  private static String jobGroupName = "ORDER_JOBGROUP_NAME";
  private static String triggerGroupName = "ORDER_TRIGGERGROUP_NAME";


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OnLineOrder createOrder(OrderDto orderDto, WeiXinUser weiXinUser, Address address) {
    Product product = productService.findOneProduct(orderDto.getProductId());
//    productService.editProductSpecRepository(orderDto.getProductSpec(), orderDto.getProductNum());
    OnLineOrder onLineOrder = new OnLineOrder();
    ProductSpec productSpec = productService.findOneProductSpec(orderDto.getProductSpec());
    //判断是否包邮
    if (orderDto.getTotalPrice().intValue() >= Constants.FREIGHT_FREE_PRICE) {
      onLineOrder.setFreightPrice(0L);
      onLineOrder.setTotalPrice(orderDto.getTotalPrice());
    } else {
      onLineOrder.setFreightPrice(Constants.FREIGHT_PRICE.longValue());
      onLineOrder.setTotalPrice(orderDto.getTotalPrice() + Constants.FREIGHT_PRICE.longValue());
    }

    onLineOrder.setTruePrice(orderDto.getTotalPrice());
    onLineOrder.setTotalScore(orderDto.getTotalScore());
    onLineOrder.setLeJiaUser(weiXinUser.getLeJiaUser());
    onLineOrder.setState(-1);
    onLineOrder.setAddress(address);
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    OrderDetail orderDetail = new OrderDetail();

    orderDetail.setProduct(product);
    orderDetail.setState(1);
    orderDetail.setProductNumber(orderDto.getProductNum());
    orderDetail.setProductSpec(productSpec);
    orderDetail.setOnLineOrder(onLineOrder);
    orderDetails.add(orderDetail);

    orderRepository.save(onLineOrder);

    //====创建订单后,生成quartz任务
//    createOrderScheduler(order.getId());
    JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
    jobThread.start();

    return onLineOrder;
  }

//  public void createOrderScheduler(Long orderId) {
//    //====创建订单后,生成quartz任务
//
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    try {
//      Date time = sdf.parse(sdf.format(new Date().getTime() + Constants.ORDER_EXPIRED));
//      JobDetail completedOrderJobDetail = JobBuilder.newJob(OrderJob.class)
//          .withIdentity("OrderJob" + orderId, jobGroupName)
//          .usingJobData("orderId", orderId)
//          .build();
//      Trigger completedOrderJobTrigger = TriggerBuilder.newTrigger()
//          .withIdentity(
//              TriggerKey.triggerKey("autoCompletedOrderJobTrigger"
//                                    + orderId, triggerGroupName))
//          .startAt(time)
//          .build();
//      scheduler.scheduleJob(completedOrderJobDetail, completedOrderJobTrigger);
//      scheduler.start();
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

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
    System.out.println(onLineOrder.getState() + "之前");
    if (onLineOrder.getState() == 0) {
      onLineOrder.setState(1);
      scoreAService.paySuccess(onLineOrder.getLeJiaUser()
          , onLineOrder.getTruePrice(), onLineOrder.getOrderSid());
      scoreBService.paySuccess(onLineOrder.getLeJiaUser(), onLineOrder.getTrueScore(),
                               onLineOrder.getOrderSid());
      System.out.println(onLineOrder.getState() + "之后");
      orderRepository.save(onLineOrder);
    }


  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<OnLineOrder> getCurrentUserOrders(LeJiaUser leJiaUser) {
    List<Integer> states = new ArrayList<>();
    states.add(4);
    return orderRepository
        .findAllByLeJiaUserAndStateNotInOrderByCreateDateDesc(leJiaUser, states);
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
        productService.orderCancle(onLineOrder.getOrderDetails());
        orderRepository.save(onLineOrder);
      }
      if (onLineOrder.getState() == -1) {
        orderRepository.delete(onLineOrder);
      }
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OnLineOrder findOrderById(Long orderId, Boolean flag) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails().stream().filter((orderDetail) ->
                                                                                       orderDetail
                                                                                           .getState()
                                                                                       == 0
    ).collect(Collectors.toList());

    if (orderDetails.size() == onLineOrder.getOrderDetails().size()) {
      orderRepository.delete(onLineOrder);
    }

    if (flag == null) {
      flag = false;
    }
    if (flag) {
      onLineOrder.getOrderDetails().removeAll(orderDetails);
    }

    return onLineOrder;
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

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OnLineOrder setPriceScoreForOrder(Long orderId, String truePrice, Long trueScore) {
    OnLineOrder onLineOrder = orderRepository.findOne(orderId);
    onLineOrder.setOrderSid(MvUtil.getOrderNumber());
    onLineOrder.setTruePrice((long) (Float.parseFloat(truePrice) * 100));
    onLineOrder.setTrueScore(trueScore);
    if (onLineOrder.getState() == -1) {
      List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
      if (productService.editProductSpecRepository(orderDetails.get(0).getProductSpec().getId(),
                                                   orderDetails.get(0).getProductNumber()) == null
          ) {
        return null;
      }
    }
    onLineOrder.setState(0);
    orderRepository.save(onLineOrder);
    return onLineOrder;
  }

  /**
   * 购物车生成订单基本信息
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public OnLineOrder createCartOrder(List<CartDetailDto> cartDetailDtos, WeiXinUser weiXinUser,
                                     Address address) {
    OnLineOrder onLineOrder = new OnLineOrder();
    List<OrderDetail> orderDetails = onLineOrder.getOrderDetails();
    Long totalPrice = 0L;
    Long totalScore = 0L;
    for (CartDetailDto cartDetailDto : cartDetailDtos) {
      OrderDetail orderDetail = new OrderDetail();
      Product product = productService.findOneProduct(cartDetailDto.getProduct().getId());
      ProductSpec productSpec = productService.editProductSpecRepository(
          cartDetailDto.getProductSpec().getId(),
          cartDetailDto.getProductNumber());
      if (productSpec != null) {
        totalPrice += productSpec.getPrice() * cartDetailDto.getProductNumber();
        totalScore +=
            (productSpec.getPrice() - productSpec.getMinPrice()) * cartDetailDto.getProductNumber();
        orderDetail.setState(1);
      } else {
        orderDetail.setState(0);
      }
      orderDetail.setProduct(product);
      orderDetail.setProductNumber(cartDetailDto.getProductNumber());
      orderDetail.setProductSpec(productSpec);
      orderDetail.setOnLineOrder(onLineOrder);
      orderDetails.add(orderDetail);
    }
    //判断是否包邮
    if (totalPrice.intValue() >= Constants.FREIGHT_FREE_PRICE) {
      onLineOrder.setFreightPrice(0L);
      onLineOrder.setTotalPrice(totalPrice);
    } else {
      onLineOrder.setFreightPrice(Constants.FREIGHT_PRICE.longValue());
      onLineOrder.setTotalPrice(totalPrice + Constants.FREIGHT_PRICE.longValue());
    }
    onLineOrder.setLeJiaUser(weiXinUser.getLeJiaUser());
    onLineOrder.setAddress(address);
    onLineOrder.setOrderDetails(orderDetails);
    onLineOrder.setState(0);
    onLineOrder.setTruePrice(totalPrice);
    onLineOrder.setTotalScore(totalScore);
    orderRepository.save(onLineOrder);
    JobThread jobThread = new JobThread(onLineOrder.getId(), scheduler);
    jobThread.start();
    return onLineOrder;
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

//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  public void deleteOrder(Long orderId) {
//    orderRepository.delete(orderId);
//  }
}
