package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 话费订单 Created by zhangwen on 2017/2/22.
 */
@RestController
@RequestMapping("/front/phone")
public class ActivityPhoneOrderController {

  private static Logger log = LoggerFactory.getLogger(ActivityPhoneOrderController.class);

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private MessageService messageService;

  @Inject
  private ActivityPhoneOrderService phoneOrderService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private WeiXinService weiXinService;

  /**
   * 进入充值记录页面 16/11/04
   */
  @RequestMapping(value = "/weixin/orderList", method = RequestMethod.GET)
  public ModelAndView orderList(HttpServletRequest request, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    List<ActivityPhoneOrder> list = phoneOrderService.findAllByLeJiaUser(weiXinUser.getLeJiaUser());
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
    return MvUtil.go("/gold/recharge/orderList");
  }

  /**
   * 金币话费订单生成 生成支付参数  17/2/22
   *
   * @param worth  话费金额
   * @param phone  充值手机号
   * @param payWay 5=公众号|1=APP
   */
  @RequestMapping(value = "/user/create", method = RequestMethod.POST)
  public LejiaResult createGoldOrder(@RequestParam Integer worth, @RequestParam String phone,
                                     HttpServletRequest request, @RequestParam Long payWay) {
    LeJiaUser
        leJiaUser =
        leJiaUserService.findUserById(Long.valueOf("" + request.getAttribute("leJiaUserId")));
    Map<String, Object> result = null;
    //判断话费充值是否超限
    result = phoneOrderService.checkUseScore(leJiaUser, phone, worth);
    String status = "" + result.get("status");
    if (!"200".equals(status)) {
      return LejiaResult.build(Integer.valueOf(status),
                               messageService.getMsg(status, (String[]) result.get("arrays")));
    }

    try {
      result = phoneOrderService.createPhoneOrder(leJiaUser, worth, phone, payWay);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
    }
    if (!"200".equals("" + result.get("status"))) {
      return LejiaResult
          .build((Integer) result.get("status"), messageService.getMsg("" + result.get("status")));
    }

    ActivityPhoneOrder order = (ActivityPhoneOrder) result.get("data");
    //话费产品如果是全金币，这直接调用充话费接口
    if (order.getTruePrice() == 0) {
      try {
        phoneOrderService.paySuccess(order.getOrderSid());
        return LejiaResult.build(2000, "支付成功", order.getId());
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
      }
    }
    SortedMap<String, Object> params = weiXinPayService
        .returnPayParams(payWay, request, "金币充值话费", order.getOrderSid(), "" + order.getTruePrice(),
                         Constants.PHONEORDER_NOTIFY_URL);
    if (params != null) {
      params.put("orderId", order.getId());
      return LejiaResult.ok(params);
    }
    return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
  }

  /**
   * 金币话费充值成功后查询数据  17/2/22
   *
   * @param orderId 话费订单
   */
  @RequestMapping(value = "/paySuccess", method = RequestMethod.POST)
  public LejiaResult paySuccess(@RequestParam String orderId) {

    ActivityPhoneOrder order = phoneOrderService.findByOrderId(orderId);
    HashMap<String, Object> map = new HashMap<>();
    if (order != null) {
      map.put("worth", order.getWorth());
      map.put("phoneNum", order.getPhone());
      map.put("truePrice", order.getTruePrice());
      map.put("trueScore", order.getTrueScoreB());
      map.put("payBack", order.getPayBackScore());
      return LejiaResult.build(200, "ok", map);
    }
    return LejiaResult.build(5006, messageService.getMsg("5006"));
  }

  /**
   * 获取充值记录  17/4/7
   */
  @RequestMapping(value = "/user/list", method = RequestMethod.POST)
  public Map<String, Object> orderList(HttpServletRequest request) {
    Long userId = Long.valueOf("" + request.getAttribute("leJiaUserId"));
    List<Map<String, Object>> list = phoneOrderService.findListByLeJiaUser(userId);
    Integer totalSave = 0;
    for (Map<String, Object> map : list) {
      totalSave += Integer.valueOf("" + map.get("trueScore"));
    }
    Map<String, Object> result = new HashMap<>();
    result.put("totalSave", totalSave);
    result.put("list", list);
    return result;
  }

  @RequestMapping(value = "/user/userTest", method = RequestMethod.GET)
  public LejiaResult userTest(HttpServletRequest request) {
    System.out
        .println("用户leJiaUserId=========================" + request.getAttribute("leJiaUserId"));
    return LejiaResult.ok("测试成功");
  }

}
