package com.jifenke.lepluslive.weixin.domain.entities;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by zhangwen on 2016/5/25.
 */
@Entity
@Table(name = "AUTO_REPLY_RULE")
public class AutoReplyRule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  //    replyType自动回复的类型：
//    'focusReply'：关注公众号自动回复；
//    'defaultReply'：默认自动回复；
//    'keywordReply'：关键词自动回复。
  private String replyType;
  private String keyword;  //自动回复的关键词
  private String replyText;   //文字自动回复的内容

  //    图文回复，由于图文回复的条数有一定限制，因此此处直接在规则表中存储
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText0;  //图文回复，主回复，显示该回复的图片
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText1; //图文回复列表第1条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText2;//图文回复列表第2条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText3;//图文回复列表第3条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText4;  //图文回复列表第4条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText5; //图文回复列表第5条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText6;//图文回复列表第6条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText7;   //图文回复列表第7条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText8;  //图文回复列表第8条
  @OneToOne(fetch = FetchType.LAZY)
  private AutoReplyImageText replyImageText9; //图文回复列表第9条

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReplyType() {
    return replyType;
  }

  public void setReplyType(String replyType) {
    this.replyType = replyType;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getReplyText() {
    return replyText;
  }

  public void setReplyText(String replyText) {
    this.replyText = replyText;
  }

  public AutoReplyImageText getReplyImageText0() {
    return replyImageText0;
  }

  public void setReplyImageText0(AutoReplyImageText replyImageText0) {
    this.replyImageText0 = replyImageText0;
  }

  public AutoReplyImageText getReplyImageText1() {
    return replyImageText1;
  }

  public void setReplyImageText1(AutoReplyImageText replyImageText1) {
    this.replyImageText1 = replyImageText1;
  }

  public AutoReplyImageText getReplyImageText2() {
    return replyImageText2;
  }

  public void setReplyImageText2(AutoReplyImageText replyImageText2) {
    this.replyImageText2 = replyImageText2;
  }

  public AutoReplyImageText getReplyImageText3() {
    return replyImageText3;
  }

  public void setReplyImageText3(AutoReplyImageText replyImageText3) {
    this.replyImageText3 = replyImageText3;
  }

  public AutoReplyImageText getReplyImageText4() {
    return replyImageText4;
  }

  public void setReplyImageText4(AutoReplyImageText replyImageText4) {
    this.replyImageText4 = replyImageText4;
  }

  public AutoReplyImageText getReplyImageText5() {
    return replyImageText5;
  }

  public void setReplyImageText5(AutoReplyImageText replyImageText5) {
    this.replyImageText5 = replyImageText5;
  }

  public AutoReplyImageText getReplyImageText6() {
    return replyImageText6;
  }

  public void setReplyImageText6(AutoReplyImageText replyImageText6) {
    this.replyImageText6 = replyImageText6;
  }

  public AutoReplyImageText getReplyImageText7() {
    return replyImageText7;
  }

  public void setReplyImageText7(AutoReplyImageText replyImageText7) {
    this.replyImageText7 = replyImageText7;
  }

  public AutoReplyImageText getReplyImageText8() {
    return replyImageText8;
  }

  public void setReplyImageText8(AutoReplyImageText replyImageText8) {
    this.replyImageText8 = replyImageText8;
  }

  public AutoReplyImageText getReplyImageText9() {
    return replyImageText9;
  }

  public void setReplyImageText9(AutoReplyImageText replyImageText9) {
    this.replyImageText9 = replyImageText9;
  }
}
