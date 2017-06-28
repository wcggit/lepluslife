package com.jifenke.lepluslive.global.filter;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wcg on 16/4/1.
 */
public class WeiXinFilter implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse httpServletResponse, Object o) throws Exception {
    String action = request.getRequestURI();
    if (action.equals("/weixin/weixinReply") || action.equals("/weixin/load") || action
        .equals("/weixin/userRegister") || action
            .equals("/weixin/pay/afterPhonePay")) {
      return true;
    }
    String unionId = CookieUtils.getCookieValue(request, "leJiaUnionId");
    if (unionId != null) {
      return true;
    }
    try {
      String callbackUrl = Constants.WEI_XIN_ROOT_URL + "/weixin/userRegister?action=" + action;
      String
          redirectUrl =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.APPID
          + "&redirect_uri=" +
          URLEncoder.encode(callbackUrl, "UTF-8")
          + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
      HttpSession seesion = request.getSession();
      seesion.setAttribute("redirectUrl", redirectUrl);
      request.getRequestDispatcher("/weixin/load").forward(request, httpServletResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse, Object o,
                         ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse, Object o, Exception e)
      throws Exception {

  }
}
