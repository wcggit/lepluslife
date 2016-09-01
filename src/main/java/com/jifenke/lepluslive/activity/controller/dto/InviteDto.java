package com.jifenke.lepluslive.activity.controller.dto;

/**
 * 邀请页返回数据 Created by zhangwen on 16/9/1.
 */
public class InviteDto {

  private Integer totalA;

  private Integer totalB;

  private Integer number;  //共邀请人数

  private String picture;   //分享图片

  private String title;     //分享标题

  private String content;  //分享内容

  private String url;       //分享链接

  public InviteDto() {
  }

  public InviteDto(Integer totalA, Integer totalB, Integer number, String picture,
                   String title, String content, String url) {
    this.totalA = totalA;
    this.totalB = totalB;
    this.number = number;
    this.picture = picture;
    this.title = title;
    this.content = content;
    this.url = url;
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

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
