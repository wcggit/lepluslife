package com.jifenke.lepluslive.groupon.controller;

import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.groupon.service.GrouponRefundService;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 退款单
 * Created by zhangwen on 2017/6/20.
 */
@RestController
@RequestMapping("/groupon")
public class GrouponRefundController {

  @Inject
  private GrouponRefundService grouponRefundService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private MessageService messageService;

  /**
   * 团购单申请退款 APP需验签 APP&WEB  17/6/20
   *
   * @param orderId 订单ID
   */
  @RequestMapping(value = "/sign/refund", method = RequestMethod.POST)
  public LejiaResult refund(HttpServletRequest request, @RequestParam Long orderId) {
    LeJiaUser
        leJiaUser =
        leJiaUserService.findUserById(Long.valueOf("" + request.getAttribute("leJiaUserId")));
    try {
      Map<String, Object> result = grouponRefundService.refundApply(orderId, leJiaUser);
      String status = "" + result.get("status");
      if (!"200".equals(status)) {
        return LejiaResult.build(Integer.valueOf(status), messageService.getMsg(status));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "service error");
    }
    return LejiaResult.ok();
  }


}
