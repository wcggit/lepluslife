package com.jifenke.lepluslive.coupon.controller;

import com.jifenke.lepluslive.coupon.domain.entities.LeJiaUserCoupon;
import com.jifenke.lepluslive.coupon.service.LeJiaUserCouponService;
import com.jifenke.lepluslive.coupon.service.MerchantCouponService;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;

/**
 * 优惠券相关 Created by zhangwen on 2016/11/29.
 */
@RestController
@RequestMapping("/front/coupon")
public class CouponController {

  @Inject
  private MerchantCouponService merchantCouponService;

  @Inject
  private LeJiaUserCouponService userCouponService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private MessageService messageUtil;

  @ApiOperation(value = "获取某个商家的优惠券列表")
  @RequestMapping(value = "/merchant/list", method = RequestMethod.GET)
  public LejiaResult list(@RequestParam Long merchantId) {
    return LejiaResult.ok(merchantCouponService.findByMerchant(merchantId));
  }

  @ApiOperation(value = "分页查询附近商户的有效优惠券列表")
  @RequestMapping(value = "/merchant/around", method = RequestMethod.POST)
  public LejiaResult around(@RequestParam Double lng, @RequestParam Double lat,
                            @RequestParam Long cityId, @RequestParam Integer currPage) {
    if (currPage == null || currPage < 1) {
      currPage = 1;
    }
    return LejiaResult
        .ok(merchantCouponService.findByDistance(lat, lng, cityId, (currPage - 1) * 10));
  }

  @ApiOperation(value = "领取优惠券")
  @RequestMapping(value = "/user/getCoupon", method = RequestMethod.POST)
  public LejiaResult getCoupon(@RequestParam String token, @RequestParam Long couponId) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    try {
      String status = merchantCouponService.getCoupon(leJiaUser, couponId);
      if (!"200".equals(status)) {
        return LejiaResult.build(Integer.valueOf(status), messageUtil.getMsg(status));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, messageUtil.getMsg("500"));
    }
    return LejiaResult.ok();
  }

  @ApiOperation(value = "查看用户某一状态的优惠券列表")
  @RequestMapping(value = "/user/list", method = RequestMethod.POST)
  public LejiaResult list(@RequestParam String token, @RequestParam Integer state,
                          @RequestParam Integer currPage) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    List<Map<String, Object>> list = null;
    if (leJiaUser != null) {
      list = userCouponService.findByLeJiaUserAndState(leJiaUser.getId(), state, currPage);
    }
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "核销优惠券")
  @RequestMapping(value = "/user/useCoupon", method = RequestMethod.POST)
  public LejiaResult useCoupon(@RequestParam String token, @RequestParam Long userCouponId) {
    try {
      String status = userCouponService.useCoupon(token, userCouponId);
      if (!"200".equals(status)) {
        return LejiaResult.build(Integer.valueOf(status), messageUtil.getMsg(status));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, messageUtil.getMsg("500"));
    }
    return LejiaResult.ok();
  }
}
