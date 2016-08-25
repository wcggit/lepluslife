package com.jifenke.lepluslive.activity.controller.dto;

/**
 * Created by wcg on 16/4/21.
 */
public class RockDto {

  private Integer times;

  private Integer totalA;

  private Integer totalB;

  private Integer currA;

  private Integer currB;

  public RockDto(Integer times, Integer totalA, Integer totalB, Integer currA,
                 Integer currB) {
    this.times = times;
    this.totalA = totalA;
    this.totalB = totalB;
    this.currA = currA;
    this.currB = currB;
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

  public Integer getTimes() {
    return times;
  }

  public void setTimes(Integer times) {
    this.times = times;
  }

  public void setTotalB(Integer totalB) {
    this.totalB = totalB;
  }

  public Integer getCurrA() {
    return currA;
  }

  public void setCurrA(Integer currA) {
    this.currA = currA;
  }

  public Integer getCurrB() {
    return currB;
  }

  public void setCurrB(Integer currB) {
    this.currB = currB;
  }
}
