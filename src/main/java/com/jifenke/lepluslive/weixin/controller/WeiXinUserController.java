package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信信息相关
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/11 20:13
 **/
@RestController
@RequestMapping("/front/weixin")
public class WeiXinUserController {

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private WeiXinUserService weiXinUserService;

  /**
   * 乐加臻品商城获取openId使用  2017/5/11
   */
  @RequestMapping(value = "/snsapi_base_openid", method = RequestMethod.GET)
  public String getOpenId(@RequestParam String callbackUrl, @RequestParam String unionId,
                          HttpServletResponse response)
      throws IOException {
    //需要静默获取乐加生活的openId
    StringBuffer redirectUrl = new StringBuffer();
    redirectUrl.append(
        "https://open.weixin.qq.com/connect/oauth2/authorize?appid=").append(Constants.APPID)
        .append("&redirect_uri=")
        .append(callbackUrl).append("&response_type=code&scope=snsapi_base&state=")
        .append(unionId).append("#wechat_redirect");

    System.out.println(redirectUrl.toString());
    response.sendRedirect(redirectUrl.toString());
    return null;
  }

  @RequestMapping("/getOpenId")
  public String getOpenId(@RequestParam String action, @RequestParam String state,
                          @RequestParam String code, HttpServletResponse response)
      throws IOException {
    Map<String, Object> map = weiXinService.getSnsAccessToken(code);
    weiXinUserService.saveOpenId(state, String.valueOf(map.get("openid")));
    System.out.println(action + "====" + code + "====" + map.get("openid") + "=====" + state);
    response.sendRedirect(action);
    return null;
  }

}
