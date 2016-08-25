package com.jifenke.lepluslive.activity.controller.dto;

/**
 * Created by wcg on 16/4/21.
 */
public class SportDto {

  private Integer totalA;

  private Integer totalB;

  private String rule;

  private String hour;

  public SportDto(Integer totalA, Integer totalB, String rule, String hour) {
    this.totalA = totalA;
    this.totalB = totalB;
    this.rule = rule;
    this.hour = hour;
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
