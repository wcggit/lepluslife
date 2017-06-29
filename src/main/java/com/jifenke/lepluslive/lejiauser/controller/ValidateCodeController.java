package com.jifenke.lepluslive.lejiauser.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.Verify;
import com.jifenke.lepluslive.lejiauser.service.SmsService;
import com.jifenke.lepluslive.lejiauser.service.VerifyService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;

/**
 * 验证码
 * Created by zhangwen on 17/6/29.
 */
@RestController
@RequestMapping("/code")
public class ValidateCodeController {

  @Inject
  private SmsService smsService;

  @Inject
  private VerifyService verifyService;

  /**
   * @param type 3=注册
   */
  @ApiOperation(value = "注册和找回密码时发送验证码")
  @RequestMapping(value = "/sign/send", method = RequestMethod.POST)
  public LejiaResult sendCode(@RequestParam String phoneNumber,
                              @RequestParam String pageSid,
                              @RequestParam Integer type,
                              HttpServletRequest request) {
//    LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);  //是否已注册
//    if (type == 1) {
//      if (leJiaUser != null) {
//        return LejiaResult.build(2007, "该手机号已注册,请直接登录");
//      }
//    } else if (type == 2) {
//      if (leJiaUser == null) {
//        return LejiaResult.build(2005, "该手机号未注册");
//      }
//    }
    Verify
        verify =
        verifyService.findByPageSidAndUserId(pageSid, Long.valueOf(
            "" + request.getAttribute("leJiaUserId")));
    if (verify != null) {
      try {
        Map<Object, Object>
            result =
            smsService.saveValidateCode(phoneNumber, request, type, verify);
        String status = "" + result.get("status");
        if ("200".equals(status)) {
          return LejiaResult.build(200, "发送成功");
        } else {
          return LejiaResult.build(Integer.valueOf(status), "" + result.get("msg"));
        }
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "服务器异常");
      }
    }
    return LejiaResult.build(1000, "异常发送请求(刷新试试?)");

  }

}
