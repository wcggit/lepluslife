package com.jifenke.lepluslive.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

/**
 * http请求工具类 Created by zhangwen on 2016/10/26.
 */
public class HttpUtils {

  public static Map<Object, Object> get(String getUrl) {

    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    httpGet.addHeader("Content-Type", "application/json");
    CloseableHttpResponse response = null;
    try {
      response = client.execute(httpGet);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<Object, Object>
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

  /**
   * 可以自定义设置请求头的GET请求，返回结果Map 2017/4/19
   *
   * @param getUrl  请求地址
   * @param headers 请求头
   * @return MAP
   */
  public static Map<String, Object> get(String getUrl, Map<String, String> headers) {

    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    headers.forEach((k, v) -> httpGet.addHeader(k, v));

    CloseableHttpResponse response = null;
    try {
      response = client.execute(httpGet);
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

  /**
   * 获取key=val请求中所有参数
   */
  public static Map<String, Object> getParameters(ServletRequest request) {
    Map<String, String[]> params = request.getParameterMap();
    TreeMap<String, Object> parameters = new TreeMap<>();
    for (String key : params.keySet()) {
      String[] values = params.get(key);
      for (String val : values) {
        parameters.put(key, val);
      }
    }
    return parameters;
  }

  /**
   * 获取xml请求中所有参数
   */
  public static String getXmlParameter(ServletRequest request) {
    InputStreamReader inputStreamReader = null;
    String str = null;
    StringBuilder buffer = new StringBuilder();
    try {
      inputStreamReader = new InputStreamReader(request.getInputStream(), "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return buffer.toString();
  }


}
