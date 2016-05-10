package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.job.OrderJob;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wcg on 16/4/26.
 */
public class JobThread extends Thread {

  private Long orderId;

  private Scheduler scheduler;

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Scheduler getScheduler() {
    return scheduler;
  }

  public void setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  private static String jobGroupName = "ORDER_JOBGROUP_NAME";
  private static String triggerGroupName = "ORDER_TRIGGERGROUP_NAME";

  public JobThread(Long orderId, Scheduler scheduler) {
    this.orderId = orderId;
    this.scheduler = scheduler;
  }

  @Override
  public void run() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date time = sdf.parse(sdf.format(new Date().getTime() + Constants.ORDER_EXPIRED));
      JobDetail completedOrderJobDetail = JobBuilder.newJob(OrderJob.class)
          .withIdentity("OrderJob" + orderId, jobGroupName)
          .usingJobData("orderId", orderId)
          .build();
      Trigger completedOrderJobTrigger = TriggerBuilder.newTrigger()
          .withIdentity(
              TriggerKey.triggerKey("autoCompletedOrderJobTrigger"
                                    + orderId, triggerGroupName))
          .startAt(time)
          .build();
      scheduler.scheduleJob(completedOrderJobDetail, completedOrderJobTrigger);
      scheduler.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
