package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.global.service.SqlService;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.repository.GrouponOrderRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.service.OnLineOrderShareService;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

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
 * 团购订单
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponOrderService {

  @Inject
  private GrouponOrderRepository repository;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private GrouponProductService grouponProductService;

  @Inject
  private GrouponCodeService grouponCodeService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private OnLineOrderShareService onLineOrderShareService;

  @Inject
  private SqlService sqlService;

  public GrouponOrder findById(Long id) {
    return repository.findOne(id);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void saveGrouponOrder(GrouponOrder grouponOrder) {
    repository.save(grouponOrder);
  }

  /**
   * 创建订单   2017/6/19
   *
   * @param leJiaUser 用户
   * @param payOrigin 订单来源 0=公众号|1=app
   * @param productId 团购商品ID
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Map<String, Object> createOrder(LeJiaUser leJiaUser, Integer payOrigin,
                                         Long productId) {
    Map<String, Object> result = new HashMap<>();
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByLeJiaUser(leJiaUser);
    GrouponProduct product = grouponProductService.findById(productId);
    ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
    GrouponOrder order = new GrouponOrder();
    if (weiXinUser != null && product != null) {
      order.setLeJiaUser(leJiaUser);
      order.setGrouponProduct(product);
      if (payOrigin == 0) {
        order.setPayOrigin(0);
      } else {
        order.setPayOrigin(1);
      }
      if (weiXinUser.getState() == 1 && product.getLjStorage() > 0) { //是会员且会员库存>0
        order.setOrderType(1L);
        order.setTotalPrice(product.getLjPrice());
        order.setRebateScorea(product.getRebateScorea());
        order.setRebateScorec(product.getRebateScorec());
      } else if (product.getNormalStorage() > 0) {
        order.setOrderType(0L);
        order.setTotalPrice(product.getNormalPrice());
      } else { //无库存
        result.put("status", 5003);
        return result;
      }
    } else {
      result.put("status", 500);
      return result;
    }
    repository.save(order);
    result.put("status", 200);
    result.put("data", convertFromCreateOrder(order, scoreA));
    return result;
  }

  /**
   * 获取订单列表 APP&公众号 2017/6/20
   *
   * @param leJiaUser  用户
   * @param currPage   页码 最小=1
   * @param orderState 0=待使用|1=已使用|2=退款|9=全部
   */
  public List listOrderByLeJiaUser(LeJiaUser leJiaUser, Integer currPage, Integer orderState) {
    StringBuffer sql = new StringBuffer();

    sql.append(
        "SELECT o.id AS orderId,o.buy_num AS buyNum,o.true_pay AS truePay,o.scorea AS scorea,o.expired_date AS expiredDate,o.order_state AS orderState,p.display_picture AS picture,p.`name` AS name FROM groupon_order o INNER JOIN groupon_product p ON o.groupon_product_id = p.id WHERE");
    if (leJiaUser != null) {
      sql.append(" o.le_jia_user_id = ").append(leJiaUser.getId());
    } else {
      return null;
    }
    sql.append(" AND o.state = 1");
    if (orderState == 0 || orderState == 1 || orderState == 2) {
      sql.append(" AND o.order_state = ").append(orderState);
    }
    if (currPage == null || currPage < 1) {
      currPage = 1;
    }
    sql.append(" ORDER BY o.id DESC LIMIT ").append((currPage - 1) * 10).append(",10");
    return sqlService.listBySql(sql.toString());
  }

  /**
   * 订单支付成功||订单详情页 APP&WEB 请求数据  2017/6/19
   *
   * @param orderId   订单ID
   * @param leJiaUser 用户
   */
  public Map<String, Object> findByOrderDetail(Long orderId, LeJiaUser leJiaUser) {
    GrouponOrder order = repository.findOne(orderId);
    //只能看到自己的订单
    if (order.getLeJiaUser().getId().equals(leJiaUser.getId())) {
      return convertFromOrderDetail(order);
    }
    return null;
  }

  /**
   * 订单提交 APP&WEB   2017/6/20
   *
   * @param leJiaUser 用户
   * @param orderId   团购订单ID
   * @param buyNum    购买数量
   * @param useScore  使用鼓励金 单位/分
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Map<String, Object> submitOrder(LeJiaUser leJiaUser, Long orderId,
                                         Integer buyNum, Long useScore) throws Exception {
    Map<String, Object> result = new HashMap<>();
    GrouponOrder order = repository.findOne(orderId);
    ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
    GrouponProduct product = order.getGrouponProduct();
    //下单前校验
    int check = checkSubmitOrder(order, product, scoreA, useScore, buyNum);
    if (check != 200) {
      result.put("status", check);
      return result;
    }
    try {
      //创建团购码
      Date expiredDate = grouponCodeService.createGrouponCode(order, buyNum);
      //修改订单信息
      Long newTotalPrice = order.getTotalPrice() * buyNum;
      if (buyNum > 1) {
        order.setBuyNum(buyNum);
        order.setTotalPrice(newTotalPrice);
        order.setRebateScorea(order.getRebateScorea() * buyNum);
        order.setRebateScorec(order.getRebateScorec() * buyNum);
      }
      order.setScorea(useScore);
      order.setTruePay(newTotalPrice - useScore);
      order.setExpiredDate(expiredDate);
      repository.save(order);
      repository.flush();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("save error");
    }
    result.put("status", 200);
    result.put("data", order);
    return result;
  }

  /**
   * 支付成功订单信息处理  2017/6/22
   *
   * @param orderSid 订单号
   * @param orderId  微信订单号 纯鼓励金支付时=当前时间
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void paySuccess(String orderSid, String orderId) {
    Date date = new Date();
    GrouponOrder order = repository.findByOrderSid(orderSid);
    try {
      //修改订单信息
      order.setState(1L);
      order.setCompleteDate(date);
      order.setOrderId(orderId);
      repository.save(order);
      //修改券码信息
      grouponCodeService.paySuccess(order);
      //减鼓励金
      if (order.getScorea() > 0) {
        ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(order.getLeJiaUser());
        scoreAService.saveScore(scoreA, 0, order.getScorea());
        scoreAService
            .saveScoreDetail(scoreA, 0, order.getScorea(), 15004, "团购消耗鼓励金", orderSid);
      }
      //发鼓励金
      if (order.getRebateScorea() > 0) {
        ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(order.getLeJiaUser());
        scoreAService.saveScore(scoreA, 1, order.getRebateScorea());
        scoreAService
            .saveScoreDetail(scoreA, 1, order.getRebateScorea(), 15004, "团购返鼓励金", orderSid);
      }
      //发金币
      if (order.getRebateScorec() > 0) {
        ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(order.getLeJiaUser());
        scoreCService.saveScoreC(scoreC, 1, order.getRebateScorec());
        scoreCService
            .saveScoreCDetail(scoreC, 1, order.getRebateScorec(), 15004, "团购返金币", orderSid);
      }
      //todo:发模板消息
      //核销时分润
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //下单校验  2017/6/20
  private int checkSubmitOrder(GrouponOrder order, GrouponProduct product, ScoreA scoreA,
                               Long useScore, Integer buyNum) {
    if (order == null || product == null || scoreA == null || useScore == null || useScore < 0
        || buyNum == null
        || buyNum < 1 || order.getTotalPrice() * buyNum < useScore) {
      return 502;
    }
    if (order.getOrderType() == 0 && useScore != 0) {
      return 502;
    }
    if (product.getState() == 0) {
      return 11002;
    }
    //校验订单状态
    if (order.getState() == 1) {
      return 5012;
    }
    //校验库存
    if ((order.getOrderType() == 0 && buyNum > product.getNormalStorage()) || (
        order.getOrderType() == 1 && buyNum > product.getLjStorage())) {//普通订单库存不足||乐加订单库存不足
      return 11003;
    }
    //校验鼓励金余额是否充足
    if (useScore > scoreA.getScore()) {
      return 6004;
    }

    return 200;
  }

  //订单支付成功||订单详情页 数据转换  2017/6/19
  private Map<String, Object> convertFromOrderDetail(GrouponOrder order) {
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> product = new HashMap<>();
    List<Map<String, Object>> codeList = new ArrayList<>();
    List<GrouponCode> codes = order.getGrouponCodes();

    GrouponProduct grouponProduct = order.getGrouponProduct();
    product.put("id", grouponProduct.getId());
    product.put("name", grouponProduct.getName());
    product.put("description", grouponProduct.getDescription());
    product.put("displayPicture", grouponProduct.getDisplayPicture());
    product.put("originPrice", grouponProduct.getOriginPrice());
    product.put("normalPrice", grouponProduct.getNormalPrice());
    product.put("ljPrice", grouponProduct.getLjPrice());
    result.put("product", product);

    for (GrouponCode code : codes) {
      Map<String, Object> map = new HashMap<>();
      map.put("code", code.getSid());
      map.put("state", code.getState());
      codeList.add(map);
    }
    result.put("codeList", codeList);

    result.put("buyNum", order.getBuyNum());
    result.put("orderState", order.getOrderState());
    result.put("orderSid", order.getOrderSid());
    result.put("totalPrice", order.getTotalPrice());
    result.put("truePay", order.getTruePay());
    result.put("scorea", order.getScorea());//实付鼓励金
    result.put("completeDate", order.getCompleteDate()); //付款时间
    result.put("expiredDate", codes.get(0).getExpiredDate());
    result.put("startDate", codes.get(0).getStartDate());
    result.put("orderType", order.getOrderType());
    result.put("rebateScorea", order.getRebateScorea());
    result.put("rebateScorec", order.getRebateScorec());
    result
        .put("qrCodeUrl", "http://www.lepluspay.com/wx/groupon/verify/" + order.getOrderSid());

    return result;
  }

  //创建订单数据转换  2017/6/19
  private Map<String, Object> convertFromCreateOrder(GrouponOrder order, ScoreA scoreA) {
    Map<String, Object> result = new HashMap<>();

    GrouponProduct grouponProduct = order.getGrouponProduct();

    result.put("displayPicture", grouponProduct.getDisplayPicture());
    result.put("name", grouponProduct.getName());
    result.put("orderId", order.getId());
    result.put("price", order.getTotalPrice());
    result.put("orderType", order.getOrderType());
    result.put("rebateScorea", order.getRebateScorea());
    result.put("rebateScorec", order.getRebateScorec());

    result.put("score", scoreA.getScore());
    return result;
  }
}
