package com.jifenke.lepluslive.coupon.controller;

import com.jifenke.lepluslive.coupon.service.LeJiaUserCouponService;
import com.jifenke.lepluslive.coupon.service.MerchantCouponService;
import com.jifenke.lepluslive.global.util.LejiaResult;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @ApiOperation(value = "获取某个商家的优惠券列表")
  @RequestMapping(value = "/merchant/list", method = RequestMethod.GET)
  public LejiaResult list(@RequestParam Long merchantId) {
    return LejiaResult.ok(merchantCouponService.findByMerchant(merchantId));
  }


  @ApiOperation(value = "分页查询附近商户的有效优惠券列表")
  @RequestMapping(value = "/merchant/around", method = RequestMethod.POST)
  public LejiaResult list(@RequestParam Double lng, @RequestParam Double lat,
                          @RequestParam Long cityId, @RequestParam Integer currPage) {
    if (currPage == null || currPage < 1) {
      currPage = 1;
    }
    return LejiaResult
        .ok(merchantCouponService.findByDistance(lat, lng, cityId, (currPage - 1) * 10));
  }
}
