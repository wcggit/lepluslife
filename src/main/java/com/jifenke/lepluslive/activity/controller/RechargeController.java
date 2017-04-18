package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.activity.service.ActivityPhoneRuleService;
import com.jifenke.lepluslive.activity.service.RechargeService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.jdom.JDOMException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//todo:待删除controller  进入充值记录页面action保留

/**
 * 手机充值 Created by zhangwen on 2016/10/26.
 */
@RestController
@RequestMapping("/front/phone")
public class RechargeController {

  @Inject
  private MessageService messageUtil;

  @Inject
  private RechargeService rechargeService;

  @Inject
  private ActivityPhoneOrderService orderService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private ActivityPhoneRuleService ruleService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private ActivityPhoneOrderService phoneOrderService;

  @Inject
  private MessageService messageService;

  /**
   * 进入充值页面 16/11/01
   */
  @RequestMapping(value = "/weixin/index", method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();

    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
    model.addAttribute("scoreB", scoreB);
    String[] str = dictionaryService.findDictionaryById(48L).getValue().split("_");
    Integer total = Integer.valueOf(str[0]);
    model.addAttribute("total", total);
    model.addAttribute("limit", str[1]);
    try {
      model
          .addAttribute("update",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str[2]).getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Integer used = orderService.todayUsedWorth();
    used = used != null ? used > total ? total : used : 0;
    int soldOut = 0;
    //是否已售罄
    if (used >= total) {
      soldOut = 1;
    }
    model.addAttribute("subState", weiXinUser.getSubState());
    model.addAttribute("phone", leJiaUser.getPhoneNumber());
    model.addAttribute("soldOut", soldOut);
    model.addAttribute("used", used);
    model.addAttribute("ruleList", JsonUtils.objectToJson(ruleService.findAllByState(1)));
    model.addAttribute("orderList", JsonUtils.objectToJson(
        orderService.findAllByLeJiaUser(weiXinUser.getLeJiaUser())));
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    return MvUtil.go("/activity/phone/index");
  }

  /**
   * 进入充值记录页面 16/11/04
   */
  @RequestMapping(value = "/weixin/orderList", method = RequestMethod.GET)
  public ModelAndView orderList(HttpServletRequest request, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    List<ActivityPhoneOrder> list = orderService.findAllByLeJiaUser(weiXinUser.getLeJiaUser());
    int totalWorth = 0;
    int totalScore = 0;
    for (ActivityPhoneOrder order : list) {
      if (order.getType() != null && order.getType() == 2) {
        totalScore += order.getTrueScoreB();
      } else {
        totalScore += order.getTrueScoreB() * 100;
      }
      totalWorth += order.getWorth();
    }
    model.addAttribute("orderList", list);
    model.addAttribute("totalWorth", totalWorth);
    model.addAttribute("totalScore", totalScore);
    return MvUtil.go("/activity/phone/orderList");
  }

  /**
   * 检查当前是否可以下单，以及下单价格  16/10/26
   *
   * @param phone 手机号码
   * @param worth 充值面值
   */
  @RequestMapping(value = "/check", method = RequestMethod.GET)
  public LejiaResult check(@RequestParam String phone, @RequestParam Integer worth) {

    Map result = rechargeService.check(phone, worth);
    if (result != null) {
      System.out.println(result.toString());
      if ("success".equals(result.get("status"))) {
        return LejiaResult.ok();
      } else {
        return LejiaResult.build(500, "" + result.get("message"));
      }
    }
    return LejiaResult.build(500, messageUtil.getMsg("500"));
  }

  /**
   * 充值回调函数 16/10/28
   */
  @RequestMapping(value = "/afterPay", method = RequestMethod.GET)
  public void afterPay(HttpServletRequest request, HttpServletResponse response)
      throws IOException, JDOMException {
    String result = "success";
    Map<String, String[]> map = request.getParameterMap();
    System.out.println(map.toString());
    String status = map.get("status")[0];
    String orderSid = map.get("sp_order_id")[0];

    if ("success".equalsIgnoreCase(status)) {
      String orderId = map.get("order_id")[0];
      Integer worth = Integer.valueOf(map.get("worth")[0]);
      String price = map.get("price")[0];
      String sign = map.get("sign")[0];
      try {//验证签名
        ActivityPhoneOrder
            order =
            orderService.checkSign(orderId, status, price, worth, orderSid, sign);

        if (order != null && order.getState() != 2) {//处理订单

          Integer p = new BigDecimal(price).multiply(new BigDecimal(100)).intValue();
          orderService.paySuccess(order, orderId, p, worth);
        } else {
          result = "failed";
        }
      } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("failed");
      }
    } else {
      //处理失败，对订单进行标识
      String message = map.get("message") != null ? map.get("message")[0] : "未知错误1";
      try {
        orderService.payFail(orderSid, message);
      } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("failed");
      }
    }
    //返回信息
    response.getWriter().write(result);
  }

  /**
   * APP进入充值页面时获取数据 16/12/07
   */
  @RequestMapping(value = "/app/index", method = RequestMethod.GET)
  public Map getInfo(@RequestParam String token) {
    Map<String, Object> result = new HashMap<>();
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
    result.put("score", scoreB.getScore());
    result.put("phone", leJiaUser.getPhoneNumber());
    result.put("ruleList", ruleService.findAllOrderByScoreDescWorthDesc());
    result.put("orderList", orderService.findAllByLeJiaUser(leJiaUser));
    String[] str = dictionaryService.findDictionaryById(48L).getValue().split("_");
    result.put("limit", str[1]);
    try {
      result.put("update", new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str[2]).getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * APP话费订单生成 生成支付参数  16/12/13
   *
   * @param ruleId 话费产品ID
   * @param phone  充值手机号
   */
  @RequestMapping(value = "/app/pay", method = RequestMethod.POST)
  public LejiaResult phoneOrderPay(@RequestParam Long ruleId, @RequestParam String phone,
                                   @RequestParam String token,
                                   HttpServletRequest request) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    Map<Object, Object> result = null;
    try {
      result = phoneOrderService.createPhoneOrder(ruleId, leJiaUser, phone, 1L);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
    }
    if (!"200".equals("" + result.get("status"))) {
      return LejiaResult
          .build((Integer) result.get("status"), messageService.getMsg("" + result.get("status")));
    }

    ActivityPhoneOrder order = (ActivityPhoneOrder) result.get("data");
    //话费产品如果是全积分，这直接调用充话费接口
    if (order.getPhoneRule().getPayType() == 3) {
      try {
        phoneOrderService.paySuccess(order.getOrderSid());
        return LejiaResult.build(2000, "支付成功", order.getId());
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
      }
    }
    //封装订单参数
    SortedMap<String, Object>
        map =
        weiXinPayService
            .buildAPPOrderParams(request, "话费充值", order.getOrderSid(), "" + order.getTruePrice(),
                                 Constants.PHONEORDER_NOTIFY_URL);
    //获取预支付id
    Map<String, Object> unifiedOrder = weiXinPayService.createUnifiedOrder(map);
    if (unifiedOrder.get("prepay_id") != null) {
      SortedMap<String, Object> sortedMap = weiXinPayService.buildAppParams(
          unifiedOrder.get("prepay_id").toString());
      sortedMap.put("orderId", order.getId());
      return LejiaResult.build(200, "ok", sortedMap);
    } else {
      return LejiaResult.build(4001, messageService.getMsg("4001"));
    }
  }

  /**
   * 进入充值记录页面 16/11/04
   */
  @RequestMapping(value = "/app/orderList", method = RequestMethod.GET)
  public LejiaResult orderList(@RequestParam String token) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    List<ActivityPhoneOrder> list = orderService.findAllByLeJiaUser(leJiaUser);
    int totalScore = 0;
    for (ActivityPhoneOrder order : list) {
      if (order.getType() != null && order.getType() == 2) {
        totalScore += order.getTrueScoreB() / 100;
      } else {
        totalScore += order.getTrueScoreB();
      }
    }
    Map<String, Object> map = new HashMap<>();
    map.put("orderList", list);
    map.put("totalScore", totalScore);
    return LejiaResult.ok(map);
  }

  /**
   * APP话费订单支付成功后请求订单信息  16/12/14
   *
   * @param orderId 订单主键
   */
  @RequestMapping(value = "/app/phoneSuccess/{orderId}", method = RequestMethod.GET)
  public LejiaResult phoneSuccessPage(@PathVariable String orderId) {
    return LejiaResult.ok(phoneOrderService.findByOrderId(orderId));
  }

//  /**
//   * 将订单重新充值  16/12/09
//   *
//   * @param orderSid 自有订单号
//   */
//  @RequestMapping(value = "/recharge")
//  public LejiaResult recharge(@RequestParam String orderSid) {
//    try {
//      return LejiaResult.ok(phoneOrderService.recharge(orderSid));
//    } catch (Exception e) {
//      e.printStackTrace();
//      return LejiaResult.build(500, "server error");
//    }
//  }
//
//  /**
//   * 将订单充值  16/12/09
//   *
//   * @param orderSid 自有订单号
//   */
//  @RequestMapping(value = "/reSubmit")
//  public LejiaResult reSubmit(@RequestParam String orderSid) {
//    ActivityPhoneOrder order = phoneOrderService.findByOrderSid(orderSid);
//
//    //支付回调成功调用第三方充值接口充值
//    Map<Object, Object>
//        result =
//        rechargeService.submit(order.getPhone(), order.getWorth(), order.getOrderSid());
//
//    if (result.get("status") == null || "failure".equalsIgnoreCase("" + result.get("status"))) {
//      //充值失败
//      return LejiaResult.build(500, "充值失败");
//    }
//    return LejiaResult.build(200, "充值成功");
//  }
}
