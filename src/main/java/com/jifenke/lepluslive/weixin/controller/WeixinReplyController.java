package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeixinReplyService;

import org.jdom.JDOMException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/3/23.
 */
@RestController
@RequestMapping("/weixin")
public class WeixinReplyController {

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private WeixinReplyService weixinReplyService;

  @RequestMapping("/weixinReply")
  public String weixinReply(HttpServletRequest request, HttpServletResponse response)
      throws IOException, JDOMException {

    if (!weiXinService
        .checkWeiXinRequest(request.getParameter("signature"), request.getParameter("timestamp"),
                            request.getParameter("nonce"))) {
      return "";
    }
    if (request.getParameter("echostr") != null) {
      return request.getParameter("echostr");
    }
    InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), "utf-8");
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    String str = null;
    StringBuffer buffer = new StringBuffer();
    while ((str = bufferedReader.readLine()) != null) {
      buffer.append(str);
    }
    Map<String,Object> map = WeixinPayUtil.doXMLParse(buffer.toString());
    System.out.println(map);

    String res;
    switch ((String) map.get("MsgType")) {
      case "event":
        res = weixinReplyService.routeWeixinEvent(map);
        break;
      default:
        res = weixinReplyService.routeWeixinMessage(map);
        break;
    }

    return res;
  }


}
