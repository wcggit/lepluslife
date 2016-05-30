package com.jifenke.lepluslive.weixin.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhangwen on 2016/5/25.
 */
@Entity
@Table(name = "AUTO_REPLY_IMAGE_TEXT")
public class AutoReplyImageText {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String textTitle;   //标题
  private String textNote;    //描述
  private String imageUrl;    //图片的地址
  private String textLink;    //点击图文跳转的链接

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTextTitle() {
    return textTitle;
  }

  public void setTextTitle(String textTitle) {
    this.textTitle = textTitle;
  }

  public String getTextNote() {
    return textNote;
  }

  public void setTextNote(String textNote) {
    this.textNote = textNote;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getTextLink() {
    return textLink;
  }

  public void setTextLink(String textLink) {
    this.textLink = textLink;
  }
}
