package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinQrCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/9/26.
 */
public interface WeiXinQrCodeRepository extends JpaRepository<WeiXinQrCode, Integer> {

  /**
   * 根据ticket获取二维码所属类型 16/09/27
   */
  List<WeiXinQrCode> findByTicket(String ticket);


}
