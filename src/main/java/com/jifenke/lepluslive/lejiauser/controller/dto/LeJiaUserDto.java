package com.jifenke.lepluslive.lejiauser.controller.dto;

/**
 * Created by wcg on 16/4/21.
 */
public class LeJiaUserDto {

  private Long scoreA;

  private Long scoreB;

  private String userOneBarCode;

  private String token;   //sid

  private String headImageUrl;

  private String userName; //用户名 暂用手机号码

  private String phoneNumber;

  public LeJiaUserDto(Long scoreA, Long scoreB, String userOneBarCode, String token,
                      String headImageUrl, String userName, String phoneNumber) {
    this.scoreA = scoreA;
    this.scoreB = scoreB;
    this.userOneBarCode = userOneBarCode;
    this.token = token;
    this.headImageUrl = headImageUrl;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getToken() {

    return token;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getHeadImageUrl() {
    return headImageUrl;
  }

  public void setHeadImageUrl(String headImageUrl) {
    this.headImageUrl = headImageUrl;
  }


  public LeJiaUserDto(Long scoreA, Long scoreB, String userOneBarCode) {
    this.scoreA = scoreA;
    this.scoreB = scoreB;
    this.userOneBarCode = userOneBarCode;
  }

  public LeJiaUserDto() {
  }

  public Long getScoreA() {
    return scoreA;
  }

  public void setScoreA(Long scoreA) {
    this.scoreA = scoreA;
  }

  public Long getScoreB() {
    return scoreB;
  }

  public void setScoreB(Long scoreB) {
    this.scoreB = scoreB;
  }

  public String getUserOneBarCode() {
    return userOneBarCode;
  }

  public void setUserOneBarCode(String userOneBarCode) {
    this.userOneBarCode = userOneBarCode;
  }
}
