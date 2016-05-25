package com.jifenke.lepluslive.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jifenke.lepluslive.order.domain.entities.ExpressInfo;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.repository.ExpressInfoRepository;

import net.sf.json.JSONObject;

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
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

/**
 * 查询物流信息 Created by zhangwen on 2016/5/9.
 */
@Service
@Transactional(readOnly = true)
public class ExpressInfoService {

  @Inject
  private ExpressInfoRepository expressInfoRepository;

  @Value("${express.appkey}")
  private String expressAppkey;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public ExpressInfo findExpressAndSave(OnLineOrder order) {

    //查询数据库是否已保存
    ExpressInfo expressInfo = expressInfoRepository.findOneByOnLineOrder(order);

    if (expressInfo != null && (3 == expressInfo.getStatus())) {
      return expressInfo;
    }
    Date date = new Date();
    if (expressInfo != null && ((date.getTime() - expressInfo.getFreshDate().getTime())
                                < 7200000)) {
      return expressInfo;
    }

    Map<String, Object> map = getExpressInfo(order.getExpressNumber());
    Integer expressStatus = Integer.valueOf(map.get("status").toString());
    if (expressStatus == null || expressStatus != 0) {
      return null;
    }

    JSONObject jasonObject = JSONObject.fromObject(map.get("result"));
    Map mapList = (Map) jasonObject;
    if (expressInfo == null) {
      expressInfo = new ExpressInfo();
      expressInfo.setOnLineOrder(order);
      expressInfo.setExpressCompany(order.getExpressCompany());
      expressInfo.setExpressNumber(mapList.get("number").toString());
    }

    Integer status = Integer.parseInt(mapList.get("deliverystatus").toString());
    expressInfo.setStatus(status);
    expressInfo.setContent(mapList.get("list").toString());
    expressInfo.setFreshDate(date);

    expressInfoRepository.save(expressInfo);

    return expressInfo;
  }


  private Map<String, Object> getExpressInfo(String expressNumber) {

    String
        getUrl =
        "http://api.jisuapi.com/express/query?appkey=" + expressAppkey + "&type=auto&number="
        + expressNumber;
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

}
