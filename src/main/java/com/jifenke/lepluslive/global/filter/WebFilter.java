package com.jifenke.lepluslive.global.filter;

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

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    WeiXinFilter weiXinFilter = new WeiXinFilter();
    weiXinFilter.setWeiXinUserService(weiXinUserService);
    registry.addInterceptor(weiXinFilter).addPathPatterns("/weixin/**");
    super.addInterceptors(registry);
  }
}
