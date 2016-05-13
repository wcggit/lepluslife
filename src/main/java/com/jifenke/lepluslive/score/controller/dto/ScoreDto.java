package com.jifenke.lepluslive.score.controller.dto;

import java.util.ArrayList;

/**
 * 积分和红包前台展示 Created by zhangwen on 2016/5/12.
 */
public class ScoreDto {


  private String createDate;

  ArrayList<Object> list;

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public ArrayList<Object> getList() {
    return list;
  }

  public void setList(ArrayList<Object> list) {
    this.list = list;
  }
}
