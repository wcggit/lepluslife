package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.jdom.JDOMException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 16/3/23.
 */
@RestController
@RequestMapping("/weixin")
public class WeixinReplyController {

  @Inject
  private WeiXinService weiXinService;

  @RequestMapping("/weixinReply")
  public
  @ResponseBody
  String weixinReply(HttpServletRequest request)
      throws IOException, JDOMException {
    if (!weiXinService
        .checkWeiXinRequest(request.getParameter("signature"), request.getParameter("timestamp"),
                            request.getParameter("nonce"))) {
      return null;

    }
    if (request.getParameter("echostr") != null) {
      return request.getParameter("echostr");
    }
//    InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), "utf-8");
//    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//    String str = null;
//    StringBuffer buffer = new StringBuffer();
//    while ((str = bufferedReader.readLine()) != null) {
//      buffer.append(str);
//    }
//    Map map = WeixinPayUtil.doXMLParse(buffer.toString());
//    System.out.println(map);
    return "";

  }


}
