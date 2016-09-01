package com.jifenke.lepluslive.banner.domain.criteria;

/**
 * 查询条件 Created by zhangwen on 16/8/26.
 */
public class BannerCriteria {

  private Integer type;

  private Integer offset;

  private Integer status; //状态   1=正常  0=下架

  private Integer alive;   //1=当期   0=往期

  private String startDate;

  private String endDate;

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getAlive() {
    return alive;
  }

  public void setAlive(Integer alive) {
    this.alive = alive;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
}
