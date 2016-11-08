package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.WeixinPayLog;
import com.jifenke.lepluslive.weixin.repository.WeixinPayLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/5/25.
 */
@Service
@Transactional(readOnly = true)
public class WeixinPayLogService {

  @Inject
  private WeixinPayLogRepository weixinPayLogRepository;

  /**
   * 查询订单时掉单保存日志 16/09/29
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void savePayLog(String outTradeNo, String returnCode, String resultCode,
                         String tradeState, String orderType) {
    WeixinPayLog
        weixinPayLog =
        new WeixinPayLog(outTradeNo, returnCode, resultCode, tradeState, 2, orderType);
    weixinPayLogRepository.save(weixinPayLog);
  }

  /**
   * 支付成功回调时保存日志 16/09/29
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void savePayLog(String orderSid, String orderType) {
    String currState = "SUCCESS";

    WeixinPayLog
        weixinPayLog =
        new WeixinPayLog(orderSid, currState, currState, 1, orderType);
    weixinPayLogRepository.save(weixinPayLog);
  }

}
