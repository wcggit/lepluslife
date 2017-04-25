package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.lejiauser.service.RegisterOriginService;
import com.jifenke.lepluslive.lejiauser.service.ValidateCodeService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import javax.inject.Inject;

/**
 * 商户注册码 用于消费者绑定手机号并绑定商户 Created by zhangwen on 2017/3/9.
 */
@Service
@Transactional(readOnly = true)
public class BindUserService {

  @Inject
  private ValidateCodeService validateCodeService;

  @Inject
  private RegisterOriginService registerOriginService;

  @Inject
  private LeJiaUserService leJiaUserService;

  /**
   * 商户注册码绑定用户 手机号注册
   *
   * @param weiXinUser  用户
   * @param merchant    商户
   * @param code        验证码
   * @param phoneNumber 手机号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void bindUserSubmit(WeiXinUser weiXinUser, Merchant merchant, String code,
                             String phoneNumber) {

    Boolean b = validateCodeService.findByPhoneNumberAndCode(phoneNumber, code);
    if (b) {
      Date date = new Date();
      if (weiXinUser.getState() != 1) {
        weiXinUser.setState(1);
        weiXinUser.setStateDate(date);
      }
      LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
      leJiaUser.setPhoneBindDate(date);
      leJiaUser.setPhoneNumber(phoneNumber);
      leJiaUser.setCityId(merchant.getCity().getId());
      RegisterOrigin
          registerOrigin =
          registerOriginService.findAndSaveByMerchantAndOriginType(merchant, 2);
      leJiaUser.setRegisterOrigin(registerOrigin);
      if (leJiaUser.getBindMerchant() == null) {
        leJiaUserService.bindMerchantAndPartner(merchant.getId(), leJiaUser);
      }
    }
  }

}
