package com.jifenke.lepluslive.weixin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinMessage;
import com.jifenke.lepluslive.weixin.repository.WeixinMessageRepository;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/7/4.
 */
@Service
@Transactional(readOnly = true)
public class WeixinMessageService {

  @Inject
  private WeixinMessageRepository weixinMessageRepository;

  public WeixinMessage findMessageByMsgID(String msgID) {

    List<WeixinMessage> list = weixinMessageRepository.findOneByMsgID(msgID);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveNews(WeixinMessage weixinMessage) throws Exception {
    try {
      weixinMessageRepository.save(weixinMessage);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

}
