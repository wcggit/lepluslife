package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.activity.service.ActivityPhoneRuleService;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.activity.service.RechargeService;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.jdom.JDOMException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
      totalScore += order.getTrueScoreB();
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
      String message = map.get("message")[0];
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

}
