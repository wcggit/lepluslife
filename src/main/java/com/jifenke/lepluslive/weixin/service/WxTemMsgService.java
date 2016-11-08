package com.jifenke.lepluslive.weixin.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jifenke.lepluslive.weixin.domain.entities.WxTemMsg;
import com.jifenke.lepluslive.weixin.repository.WxTemMsgRepository;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


/**
 * 发送模板消息 Created by zhangwen on 2016/5/10.
 */
@Service
@Transactional(readOnly = true)
public class WxTemMsgService {

  @Inject
  private WxTemMsgRepository wxTemMsgRepository;

  @Inject
  private DictionaryService dictionaryService;

  /**
   * 根据temId不同，发送不同的消息 keys  封装参数
   */
  public void sendTemMessage(String openId, Long temId, String[] keys) {

    WxTemMsg wxTemMsg = wxTemMsgRepository.findOne(temId);

    HashMap<String, Object> map2 = new HashMap<>();

    HashMap<String, Object> mapfirst = new HashMap<>();
    mapfirst.put("value", wxTemMsg.getFirst());
    mapfirst.put("color", wxTemMsg.getColor());

    HashMap<String, Object> mapRemark = new HashMap<>();
    mapRemark.put("value", wxTemMsg.getRemark());
    mapRemark.put("color", wxTemMsg.getColor());

    map2.put("first", mapfirst);
    map2.put("remark", mapRemark);

    int i = 1;

    for (String key : keys) {
      HashMap<String, Object> mapKey = new HashMap<>();
      mapKey.put("value", key);
      mapKey.put("color", wxTemMsg.getColor());
      map2.put("keyword" + i, mapKey);
      i++;
    }

    // 先封装一个 JSON 对象
    JSONObject param = new JSONObject();

    param.put("touser", openId);
    param.put("template_id", wxTemMsg.getTemplateId());
    param.put("url", wxTemMsg.getUrl());
    param.put("data", map2);

    sendTemplateMessage(param,7L);
  }

  /**
   * 发送模板消息
   */
  private void sendTemplateMessage(JSONObject param, Long wxId) {
    try {
      // 绑定到请求 Entry

      StringEntity
          se =
          new StringEntity(new String(param.toString().getBytes("utf8"), "iso8859-1"));

      //获取token
      String token = dictionaryService.findDictionaryById(wxId).getValue();

      String
          getUrl =
          "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
      CloseableHttpClient httpclient = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(getUrl);
      httpPost.addHeader("Content-Type", "application/json");
      httpPost.setEntity(se);
      CloseableHttpResponse response = null;

      response = httpclient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object>
          map =
          mapper.readValue(
              new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
              Map.class);
      EntityUtils.consume(entity);
      response.close();
      System.out.println(map.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

