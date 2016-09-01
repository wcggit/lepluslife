package com.jifenke.lepluslive.activity.controller.dto;

/**
 * 运动 Created by zhangwen on 16/9/1.
 */
public class SportDto {

  private Integer totalA;

  private Integer totalB;

  private String rule;

  private String hour;

  private Integer status;  //今日是否领取 1=已领取 0=未领取

  public SportDto(Integer totalA, Integer totalB, String rule, String hour, Integer status) {
    this.totalA = totalA;
    this.totalB = totalB;
    this.rule = rule;
    this.hour = hour;
    this.status = status;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getHour() {
    return hour;
  }

  public void setHour(String hour) {
    this.hour = hour;
  }

  public Integer getTotalA() {
    return totalA;
  }

  public void setTotalA(Integer totalA) {
    this.totalA = totalA;
  }

  public Integer getTotalB() {
    return totalB;
  }

  public void setTotalB(Integer totalB) {
    this.totalB = totalB;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }
}
