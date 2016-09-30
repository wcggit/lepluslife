package com.jifenke.lepluslive.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.inject.Inject;
import javax.sql.DataSource;


/**
 * Created by wcg on 16/3/27.
 */
@Configuration
public class SchedulerConfigration {

  @Inject
  private DataSource dataSource;

  @Autowired
  private ResourceLoader resourceLoader;

  @Bean
  public SchedulerFactoryBean schedulerFactory(){
    SchedulerFactoryBean bean = new SchedulerFactoryBean ();
    bean.setConfigLocation(resourceLoader.getResource("classpath:quartz.properties"));
    bean.setApplicationContextSchedulerContextKey("applicationContextKey");
    bean.setSchedulerName("order");
    bean.setDataSource(dataSource);
    return bean;
  }
}
