package com.jifenke.lepluslive.global.filter;

import com.jifenke.lepluslive.global.config.AppConstants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.HttpUtils;
import com.jifenke.lepluslive.global.util.SignUtil;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信和APP用户身份识别 以及APP验签 Created by zhangwen on 17/6/20.
 */
public class SignFilter implements HandlerInterceptor {

  private static Logger logger = LoggerFactory.getLogger(SignFilter.class);

  private WeiXinUserService weiXinUserService;

  private LeJiaUserService leJiaUserService;

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object o) throws Exception {
    String source = request.getParameter("source"); // APP || WEB
    if ("APP".equals(source)) {
      TreeMap<String, Object>
          parameters =
          (TreeMap<String, Object>) HttpUtils.getParameters(request);
      System.out.println("请求数据==================" + parameters.toString());
      //时间戳验证
      long currTime = System.currentTimeMillis();
      long reqTime = Long.valueOf(parameters.get("timestamp").toString());
      if (currTime + AppConstants.REQUEST_TIMESTAMP_ALLOW_RANGE < reqTime ||
          reqTime < currTime - AppConstants.REQUEST_TIMESTAMP_ALLOW_RANGE) {
        logger.error("时间戳有误" + reqTime);
        return false;
      }

      String sign = String.valueOf(parameters.get("sign"));
      parameters.remove("sign");

      StringBuilder sb = new StringBuilder();
      parameters.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
      String requestStr = sb.deleteCharAt(sb.length() - 1).toString();

      System.out.println("签名数据====" + requestStr);
      if (SignUtil.testSign(requestStr, sign)) { //验签
        System.out.println("success");
        if ("/user/sign/login".equals(request.getRequestURI())) {
          return true;
        }
        request.setAttribute("leJiaUserId",
                             leJiaUserService.findUserByUserSid(parameters.get("token").toString())
                                 .getId());
        return true;
      }
      logger.error("签名有误" + requestStr);
    } else if ("WEB".equals(source)) {
      String unionId = CookieUtils.getCookieValue(request, "leJiaUnionId");
      if (unionId != null) {
        WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByUnionId(unionId);
        if (weiXinUser != null) {
          request.setAttribute("leJiaUserId", weiXinUser.getLeJiaUser().getId());
          return true;
        }
      }
    }
    request.getRequestDispatcher("/user/notFound").forward(request, response);
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

  public void setWeiXinUserService(WeiXinUserService weiXinUserService) {
    this.weiXinUserService = weiXinUserService;
  }

  public void setLeJiaUserService(LeJiaUserService leJiaUserService) {
    this.leJiaUserService = leJiaUserService;
  }
}
