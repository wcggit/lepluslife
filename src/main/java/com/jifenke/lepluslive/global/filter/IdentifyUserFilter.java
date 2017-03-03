package com.jifenke.lepluslive.global.filter;

import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信和APP用户身份识别(以便统一提供和用户相关的数据) Created by zhangwen on 17/2/23.
 */
public class IdentifyUserFilter implements HandlerInterceptor {

  private WeiXinUserService weiXinUserService;

  private LeJiaUserService leJiaUserService;

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object o) throws Exception {

    String unionId = CookieUtils.getCookieValue(request, "leJiaUnionId");
    if (unionId != null) {
      WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByUnionId(unionId);
      if (weiXinUser != null) {
        request.setAttribute("leJiaUserId", weiXinUser.getLeJiaUser().getId());
        return true;
      }
    } else {
      String token = request.getParameter("token");
      if (token != null) {
        LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
        if (leJiaUser != null) {
          request.setAttribute("leJiaUserId", leJiaUser.getId());
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
