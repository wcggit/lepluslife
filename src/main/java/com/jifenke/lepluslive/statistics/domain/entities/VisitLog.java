package com.jifenke.lepluslive.statistics.domain.entities;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 页面访问记录/按钮点击记录等操作日志 Created by zhangwen on 2017/3/10.
 */
@Entity
@Table(name = "VISIT_LOG")
public class VisitLog {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private Date dateCreated = new Date();

  private String user;   //用户唯一凭证，unionID但不限于unionID

  private String category; //产品详情=product|公众号按钮=menu

  private String target;   //标明属于哪个具体产品或按钮等唯一识别符

  private String source;  //标明操作来源

  public VisitLog(String user, String category, String target, String source) {
    this.user = user;
    this.category = category;
    this.target = target;
    this.source = source;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
