package com.jifenke.lepluslive.groupon.controller;

import com.jifenke.lepluslive.global.config.AppConstants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.HttpUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.service.GrouponOrderService;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeixinPayLogService;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangwen on 2017/6/19.
 */
@RestController
@RequestMapping("/groupon")
public class GrouponOrderController {

  private static Logger log = LoggerFactory.getLogger(GrouponOrderController.class);

  @Inject
  private GrouponOrderService grouponOrderService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private MessageService messageService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private WeixinPayLogService weixinPayLogService;

  /**
   * 跳转到支付成功页面  2017/6/19
   *
   * @param orderId 订单号
   */
  @RequestMapping(value = "/paySuccess/{orderId}")
  public ModelAndView goPaySuccessPage(@PathVariable Long orderId, Model model,
                                       HttpServletRequest request) {
    model.addAttribute("order", grouponOrderService
        .findByOrderDetail(orderId, weiXinService.getCurrentWeiXinUser(request).getLeJiaUser()));
    return MvUtil.go("/groupon/order/success");
  }

  /**
   * 跳转到订单详情页面  2017/6/20
   *
   * @param orderId 订单号
   */
  @RequestMapping(value = "/weixin/orderDetail")
  public ModelAndView goOrderDetailPage(@RequestParam Long orderId, Model model,
                                        HttpServletRequest request) {
    model.addAttribute("order", grouponOrderService
        .findByOrderDetail(orderId, weiXinService.getCurrentWeiXinUser(request).getLeJiaUser()));
    return MvUtil.go("/groupon/order/detail");
  }

  /**
   * 跳转到订单列表页面  2017/6/20
   */
  @RequestMapping(value = "/weixin/orderList")
  public ModelAndView goOrderListPage() {
    return MvUtil.go("/groupon/order/list");
  }

  /**
   * 团购商品创建订单 APP验签 APP&WEB  2017/6/19
   *
   * @param payOrigin 0=公众号|1=app
   * @param id        团购商品ID
   */
  @RequestMapping(value = "/sign/create", method = RequestMethod.POST)
  public LejiaResult createOrder(HttpServletRequest request, @RequestParam Integer payOrigin,
                                 @RequestParam Long id) {

    LeJiaUser
        leJiaUser =
        leJiaUserService.findUserById(Long.valueOf("" + request.getAttribute("leJiaUserId")));

    Map<String, Object> result = grouponOrderService.createOrder(leJiaUser, payOrigin, id);
    String status = "" + result.get("status");
    if (!"200".equals(status)) {
      return LejiaResult.build(Integer.valueOf(status), messageService.getMsg(status));
    }

    return LejiaResult.ok(result.get("data"));
  }

  /**
   * 订单确认页提交&生成支付参数 APP验签 APP&WEB  2017/6/20
   *
   * @param orderId  团购订单ID
   * @param buyNum   购买数量
   * @param useScore 使用鼓励金 单位/元
   */
  @RequestMapping(value = "/sign/submit", method = RequestMethod.POST)
  public LejiaResult submitOrder(HttpServletRequest request, @RequestParam Long orderId,
                                 @RequestParam Integer buyNum, @RequestParam String useScore) {
    if (useScore == null || useScore.equals("")) {
      useScore = "0";
    }
    Long newScoreA = new BigDecimal(useScore).multiply(new BigDecimal(100)).longValue();
    LeJiaUser
        leJiaUser =
        leJiaUserService.findUserById(Long.valueOf("" + request.getAttribute("leJiaUserId")));

    try {
      Map<String, Object>
          result =
          grouponOrderService.submitOrder(leJiaUser, orderId, buyNum, newScoreA);

      String status = result.get("status").toString();
      if (!"200".equals(status)) {
        return LejiaResult
            .build((Integer) result.get("status"),
                   messageService.getMsg("" + result.get("status")));
      }
      GrouponOrder order = (GrouponOrder) result.get("data");
      if (order.getTruePay() == 0) {//纯红包支付处理
        grouponOrderService.paySuccess(order.getOrderSid(), LocalDateTime.now().toString());
        return LejiaResult.build(2000, "success", order.getId());
      }

      //payOrigin 0 公众号 1 app ==> payWay 5=公众号|1=APP
      long payWay = 1;
      if (order.getPayOrigin() == 0) {
        payWay = 5;
      }
      SortedMap<String, Object> params = weiXinPayService
          .returnPayParams(payWay, request, "团购商品购买", order.getOrderSid(),
                           "" + order.getTruePay(),
                           AppConstants.GROUPON_ORDER_NOTIFY_URL);
      if (params != null) {
        return LejiaResult.ok(params);
      }
      return LejiaResult.build(500, "出现未知错误,请尝试重新下单");
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "save error");
    }
  }

  /**
   * 订单列表页 查询数据 需验签 APP&WEB  17/6/20
   *
   * @param currPage   页码 最小=1
   * @param orderState 0=待使用|1=已使用|2=退款|9=全部
   */
  @RequestMapping(value = "/sign/orderList", method = RequestMethod.POST)
  public LejiaResult orderList(HttpServletRequest request, @RequestParam Integer currPage,
                               @RequestParam Integer orderState) {

    return LejiaResult.ok(grouponOrderService
                              .listOrderByLeJiaUser(leJiaUserService.findUserById(
                                  Long.valueOf("" + request.getAttribute("leJiaUserId"))),
                                                    currPage, orderState));
  }

  /**
   * 订单支付成功||订单详情页 查询数据 需验签 APP  17/6/19
   *
   * @param orderId 订单ID
   */
  @RequestMapping(value = "/sign/orderDetail", method = RequestMethod.POST)
  public LejiaResult orderDetail(@RequestParam String token, @RequestParam Long orderId) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    return LejiaResult.ok(grouponOrderService.findByOrderDetail(orderId, leJiaUser));
  }

  /**
   * 微信支付成功回调函数 17/6/22
   */
  @RequestMapping(value = "/afterPay", produces = MediaType.APPLICATION_XML_VALUE)
  public void afterPhonePay(HttpServletRequest request, HttpServletResponse response)
      throws IOException, JDOMException {
    String req = HttpUtils.getXmlParameter(request);
    Map<String, Object> map = WeixinPayUtil.doXMLParse(req);
    if (map != null) {
      //验签
      String
          sign =
          weiXinPayService.createSign(map, String.valueOf(map.get("trade_type")));
      if (map.get("sign") != null && map.get("sign").toString().equals(sign)) {
        //保存微信支付日志
        weixinPayLogService.savePayLog(map, "GrouponOrder", 1);
        String orderSid = (String) map.get("out_trade_no");
        String returnCode = (String) map.get("return_code");
        String resultCode = (String) map.get("result_code");
        String orderId = (String) map.get("transaction_id");
        //操作订单
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
          try {
            grouponOrderService.paySuccess(orderSid, orderId);
          } catch (Exception e) {
            log.error(e.getMessage());
            response.setContentType("application/xml");
            response.getWriter().write("<xml><return_code>FAIL</return_code></xml>");
            return;
          }
        }
        //返回微信的信息
        response.setContentType("application/xml");
        response.getWriter().write("<xml><return_code>" + returnCode + "</return_code></xml>");
      }
    }
  }

}
