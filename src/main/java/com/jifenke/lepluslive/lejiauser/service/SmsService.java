package com.jifenke.lepluslive.lejiauser.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.ValidateCode;
import com.jifenke.lepluslive.lejiauser.repository.ValidateCodeRepository;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 短信服务 Created by zhangwen on 2016/4/26.
 */
@Service
@Transactional(readOnly = true)
public class SmsService {

  @Inject
  private ValidateCodeRepository validateCodeRepository;

  @Inject
  private ValidateCodeService validateCodeService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Value("${sms.appkey}")
  private String appkey;

  @Value("${sms.secret}")
  private String secret;

  /**
   * 发送验证码并保存并创建一个验证码过期的定时任务 1=发送成功 0=发送次数过多，稍后再试
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<Object, Object> saveValidateCode(String phoneNumber, HttpServletRequest request,
                                              Integer type) throws Exception {
    Map<Object, Object> result = new HashMap<>();
    //获取ip地址，判断这个地址发送过几个验证码，防止短信轰炸
    String ipAddr = weiXinPayService.getIpAddr(request);
    Integer count = validateCodeRepository.countByIpAddrAndStatus(ipAddr, 0);
    if (count > 5) {
      result.put("status", 3002);//中文错误原因
      result.put("msg", "发送过于频繁，请稍后再试");//中文错误原因
      return result;
    }

    String code = MvUtil.getRandomNumber();
    ValidateCode validateCode = new ValidateCode();
    validateCode.setPhoneNumber(phoneNumber);
    validateCode.setCode(code);
    validateCode.setIpAddr(ipAddr);

    //发送短信
    String smsSendUrl = Constants.SMS_SEND_URL;
    String smsTemplateCode = null;
    if (type == 1) {//注册
      smsTemplateCode = Constants.SMS_REGISTER_CODE;
    } else if (type == 2) {
      smsTemplateCode = Constants.SMS_CHANGE_PWD_CODE;
    } else if (type == 3) {
      smsTemplateCode = Constants.SMS_BANGDING_CODE;
    }

    TaobaoClient client = new DefaultTaobaoClient(smsSendUrl, appkey, secret);
    AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
    req.setSmsType("normal");
    req.setSmsFreeSignName("乐加生活");
    req.setSmsParamString("{\"code\":\"" + code + "\",\"product\":\"乐加生活\"}");
    req.setRecNum(phoneNumber);
    req.setSmsTemplateCode(smsTemplateCode);

    try {
      AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
      String errorCode = rsp.getErrorCode();

      System.out.println(rsp.getBody());

      if (errorCode == null) {
        validateCode.setSendCode("success");
        result.put("status", 200);
      } else {
        validateCode.setSendCode(errorCode);
        String subMsg = rsp.getSubMsg();
        validateCode.setSubMsg(subMsg);
        validateCode.setSubCode(rsp.getSubCode());
        result.put("status", 3003);
        result.put("msg", subMsg);//中文错误原因
      }

      validateCodeRepository.save(validateCode);
      //生成验证码后,创建quartz任务
      validateCodeService.startValidateCodeJob(validateCode.getId());
      return result;
    } catch (ApiException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

//  /**
//   * 发送短信，此方法建议在job中调用，或者异步调用
//   */
//  public Map<Object, Object> sendValidateCode(String phoneNumber, String code, Integer type) {

//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        String smsSendUrl = Constants.SMS_SEND_URL;
//        String smsTemplateCode = null;
//        if (type == 1) {//注册
//          smsTemplateCode = Constants.SMS_REGISTER_CODE;
//        } else if (type == 2) {
//          smsTemplateCode = Constants.SMS_CHANGE_PWD_CODE;
//        } else if (type == 3) {
//          smsTemplateCode = Constants.SMS_BANGDING_CODE;
//        }
//
//        TaobaoClient client = new DefaultTaobaoClient(smsSendUrl, appkey, secret);
//        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//        // req.setExtend("123456");
//        req.setSmsType("normal");
//        req.setSmsFreeSignName("乐加生活");
//        req.setSmsParamString("{\"code\":\"" + code + "\",\"product\":\"乐加生活\"}");
//        req.setRecNum(phoneNumber);
//        req.setSmsTemplateCode(smsTemplateCode);
//        try {
//          AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
//          // System.out.println(rsp.getBody());
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//    }).start();
//  }

}
