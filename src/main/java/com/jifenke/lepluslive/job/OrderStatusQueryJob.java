package com.jifenke.lepluslive.job;

import com.jifenke.lepluslive.order.service.OrderService;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 5分钟后查询订单是否支付完成防止掉单 Created by zhangwen on 16/05/26.
 */
public class OrderStatusQueryJob extends BaseJobBean {

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    ApplicationContext applicationContext = super.getApplicationContext(context);
    OrderService orderService = applicationContext.getBean("orderService", OrderService.class);
    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    Long orderId = dataMap.getLong("orderId");

    orderService.orderStatusQuery(orderId);
  }
}
