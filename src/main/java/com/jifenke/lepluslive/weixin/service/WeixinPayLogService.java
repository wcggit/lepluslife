package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.WeixinPayLog;
import com.jifenke.lepluslive.weixin.repository.WeixinPayLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import javax.inject.Inject;

/**
 * 微信支付记录 Created by zhangwen on 2016/5/25.
 */
@Service
@Transactional(readOnly = true)
public class WeixinPayLogService {

  @Inject
  private WeixinPayLogRepository weixinPayLogRepository;

  /**
   * 支付成功回调时保存日志 17/02/22 zhangwen
   *
   * @param map       回调参数
   * @param orderType 订单类型
   * @param logType   回调类型 1=支付后微信回调日志|2=掉单查询订单日志
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void savePayLog(Map<String, Object> map, String orderType, int logType) {
    WeixinPayLog log = new WeixinPayLog();
    log.setOutTradeNo(String.valueOf(map.get("out_trade_no")));
    log.setReturnCode(String.valueOf(map.get("return_code")));
    log.setResultCode(String.valueOf(map.get("result_code")));
    log.setTradeState(String.valueOf(map.get("trade_state"))); //仅查询有返回
    log.setLogType(logType);
    log.setOrderType(orderType);
    log.setOpenId(String.valueOf(map.get("openid")));
    log.setTotalFee(map.get("total_fee") != null ? Long.valueOf("" + map.get("total_fee")) : 0L);
    log.setTransactionId(String.valueOf(map.get("transaction_id")));
    log.setTimeEnd(String.valueOf(map.get("time_end")));
    weixinPayLogRepository.save(log);
  }

}
