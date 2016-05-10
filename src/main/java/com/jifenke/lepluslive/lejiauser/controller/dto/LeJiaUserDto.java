package com.jifenke.lepluslive.lejiauser.controller.dto;

/**
 * Created by wcg on 16/4/21.
 */
public class LeJiaUserDto {

    private Long scoreA;

    private Long scoreB;

    private String userOneBarCode;

    public LeJiaUserDto(Long scoreA, Long scoreB, String userOneBarCode, String token, String headImageUrl) {
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.userOneBarCode = userOneBarCode;
        this.token = token;
        this.headImageUrl = headImageUrl;
    }

    public String getToken() {

        return token;
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

    private String token;   //sid

    private String headImageUrl;


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
