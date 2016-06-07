package com.jifenke.lepluslive.product.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 商品推荐 Created by zhangwen on 16/6/6.
 */
@Entity
@Table(name = "PRODUCT_RECOMMEND")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductRecommend {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotNull
  private String picture;

  private Integer sid;   //序号

  @JsonIgnore
  private Integer state = 1;  //0=删除 1=有效

  @JsonIgnore
  private Date createDate = new Date();  //创建时间

  @JsonIgnore
  private Date lastUpDate;  //最近更新时间

  @JsonIgnore
  @ManyToOne
  private Product product;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getPicture() {
    return picture;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getLastUpDate() {
    return lastUpDate;
  }

  public void setLastUpDate(Date lastUpDate) {
    this.lastUpDate = lastUpDate;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Integer getSid() {
    return sid;
  }

  public void setSid(Integer sid) {
    this.sid = sid;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
