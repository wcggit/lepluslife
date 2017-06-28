package com.jifenke.lepluslive.global.filter;

import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;

/**
 * Created by wcg on 16/4/1.
 */
@Configuration
public class WebFilter extends WebMvcConfigurerAdapter {

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    WeiXinFilter weiXinFilter = new WeiXinFilter();
//    weiXinFilter.setWeiXinUserService(weiXinUserService);
    IdentifyUserFilter filter = new IdentifyUserFilter();
    SignFilter signFilter = new SignFilter();
    filter.setWeiXinUserService(weiXinUserService);
    filter.setLeJiaUserService(leJiaUserService);
    signFilter.setWeiXinUserService(weiXinUserService);
    signFilter.setLeJiaUserService(leJiaUserService);
    registry.addInterceptor(weiXinFilter)
        .addPathPatterns("/weixin/**", "/*/weixin/**", "/*/*/weixin/**");
    registry.addInterceptor(filter).addPathPatterns("/*/*/user/**");
    registry.addInterceptor(signFilter).addPathPatterns("/*/sign/**");
    super.addInterceptors(registry);
  }
}
