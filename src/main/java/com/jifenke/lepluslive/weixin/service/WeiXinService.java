package com.jifenke.lepluslive.weixin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 16/3/18.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinService {

  @Value("${weixin.appSecret}")
  private String secret;

  @Value("${weixin.appId}")
  private String appid;

  @Value("${weixin.grantType}")
  private String grantType;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private DictionaryService dictionaryService;


  public Map<String, Object> getSnsAccessToken(String code) {
    String
        getUrl =
        "https://api.weixin.qq.com/sns/oauth2/access_token?secret=" + secret + "&appid=" + appid
        + "&code=" + code + "&grant_type=" + grantType;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    httpGet.addHeader("Content-Type", "application/json");
    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object>
          map =
          mapper.readValue(new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
                           Map.class);
      EntityUtils.consume(entity);
      response.close();
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Map<String, Object> getDetailWeiXinUser(String accessToken, String openid) {
    String
        getUrl =
        "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    httpGet.addHeader("Content-Type", "application/json;charset=utf8mb4");
    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object>
          userDetail =
          mapper.readValue(new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
                           Map.class);
      EntityUtils.consume(entity);
      response.close();
      return userDetail;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  //主动获取微信用户信息
  public Map<String, Object> getWeiXinUserInfo(String openid) {
    String accessToken = dictionaryService.findDictionaryById(7L).getValue();
//    String
//        accessToken =
//        "Gu-yGZhWqC5ADzk5SS-gYf31AawVLMPuRPNeivwiJ6r9azPQ3wNGvYScEfFeb_pDtepmKNp8nCJs5HLjx9jJE7aNiLsL3IofTYoziwNab_wD7P6ez0AyrwAtV49JklfjXRAfAJAQKT";
    String
        getUrl =
        "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid="
        + openid;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    httpGet.addHeader("Content-Type", "application/json;charset=utf8mb4");
    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object>
          userDetail =
          mapper.readValue(new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
                           Map.class);
      EntityUtils.consume(entity);
      response.close();
      return userDetail;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser getCurrentWeiXinUser(HttpServletRequest request) {
    String openId = CookieUtils.getCookieValue(request, appid + "-user-open-id");
    return weiXinUserService.findWeiXinUserByOpenId(openId);
  }

  @Transactional(readOnly = true)
  public boolean checkWeiXinRequest(String signature, String timestamp, String nonce) {
    String[] strs = new String[]{Constants.WEI_XIN_TOKEN, timestamp, nonce};
    Arrays.sort(strs);
    String sign = strs[0] + strs[1] + strs[2];
    return DigestUtils.sha1Hex(sign).equals(signature);
  }
}
