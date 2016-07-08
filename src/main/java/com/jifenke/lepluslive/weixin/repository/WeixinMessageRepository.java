package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.WeixinMessage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/5/25.
 */
public interface WeixinMessageRepository extends JpaRepository<WeixinMessage, Long> {

  public List<WeixinMessage> findOneByMsgID(String msgID);

}
