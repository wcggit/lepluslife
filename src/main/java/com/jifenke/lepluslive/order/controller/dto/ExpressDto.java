package com.jifenke.lepluslive.order.controller.dto;


/**
 * 物流信息 前台显示
 * Created by wcg on 16/4/5.
 */
public class ExpressDto {

  private String time;

  private String status;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
