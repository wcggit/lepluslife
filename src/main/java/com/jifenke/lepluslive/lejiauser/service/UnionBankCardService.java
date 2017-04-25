package com.jifenke.lepluslive.lejiauser.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.HttpClientUtil;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.RSAUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.domain.entities.UnionBankCard;
import com.jifenke.lepluslive.lejiauser.repository.UnionBankCardRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.inject.Inject;

/**
 * 银联用户银行卡注册 Created by zhangwen on 2017/4/19.
 */
@Service
@Transactional(readOnly = true)
public class UnionBankCardService {

  @Inject
  private UnionBankCardRepository repository;

  /**
   * 绑定银行卡 16/11/28
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void addUnionBankCard(LeJiaUser leJiaUser, String cardNum, String phoneNum,
                               Integer registerWay) {
    UnionBankCard unionBankCard = repository.findByNumber(cardNum);
    if (unionBankCard == null) {
      unionBankCard = new UnionBankCard();
      unionBankCard.setNumber(cardNum);
    }
    unionBankCard.setPhoneNumber(phoneNum);
    unionBankCard.setRegisterWay(registerWay);
    unionBankCard.setUserSid(leJiaUser.getUserSid());
    if (unionBankCard.getState() != 1) {
      Map map = register(cardNum, phoneNum);
      if ("0000".equals(map.get("msg_rsp_code"))) {
        unionBankCard.setState(1);
      }
    }
    repository.save(unionBankCard);
  }

  /**
   * 银联会员注册  2017/01/19
   *
   * @param bankNumber 银行卡号码
   */
  public Map register(String bankNumber, String phone) {
    //先银行卡加密
    Map map = reSignBank(bankNumber);
    if (!"0000".equals(map.get("msg_rsp_code"))) {
      return map;
    }
    //注册
    SortedMap<String, String> params = commonParams("102103");
    //业务项
    params.put("sp_chnl_no", Constants.MSG_SENDER);
    params.put("enc_card_no", map.get("enc_card_no").toString());//公钥RSA加密后卡号
    params.put("mobile_no", phone);
    //签名
    params.put("sign", RSAUtil.sign(getOriginStr(params)));
    String result = HttpClientUtil.post(Constants.BANK_REGISTER_URL, params, "utf-8");

    return JsonUtils.jsonToPojo(result, Map.class);
  }

  /**
   * 银行卡加密  2017/01/19
   *
   * @param bankNumber 银行卡号码
   */
  public Map reSignBank(String bankNumber) {
    SortedMap<String, String> params = commonParams("104003");
    //业务项
    params.put("sp_chnl_no", Constants.MSG_SENDER);//分配的渠道号
    params.put("pk_card_no", RSAUtil.encode(bankNumber));  //公钥RSA加密后卡号
    //params.put("mobile_no", "");  // o 手机号
    //签名
    params.put("sign", RSAUtil.sign(getOriginStr(params)));
    String result = HttpClientUtil.post(Constants.BANK_SIGN_URL, params, "utf-8");

    return JsonUtils.jsonToPojo(result, Map.class);
  }

  /**
   * 其他银商接口获取公共参数  2017/01/19
   *
   * @param code 交易代码
   */
  private SortedMap<String, String> commonParams(String code) {
    SortedMap<String, String> params = new TreeMap<>();
    params.put("msg_type", "10");
    params.put("msg_txn_code", code);
    params.put("msg_crrltn_id", UUID.randomUUID().toString().replace("-", ""));
    params.put("msg_flg", "0");
    params.put("msg_sender", Constants.MSG_SENDER);//分配的渠道号
    params.put("msg_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    params.put("msg_sys_sn", MvUtil.getOrderNumber());
    params.put("msg_ver", "0.1");
    return params;
  }

  private String getOriginStr(SortedMap<String, String> parameters) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      String k = entry.getKey();
      String v = entry.getValue();
      if (null != v) {
        sb.append(k).append("=").append(v).append("&");
      }
    }
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

}
