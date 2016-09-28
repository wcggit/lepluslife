package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinQrCode;
import com.jifenke.lepluslive.weixin.repository.WeiXinQrCodeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 永久二维码 Created by zhangwen on 2016/9/27.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinQrCodeService {

  @Inject
  private WeiXinQrCodeRepository weiXinQrCodeRepository;

  /**
   * 根据ticket获取二维码所属类型 16/09/27
   *
   * @param ticket 二维码标识
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinQrCode findByTicket(String ticket) {
    List<WeiXinQrCode> list = weiXinQrCodeRepository.findByTicket(ticket);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
