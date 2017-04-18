package com.jifenke.lepluslive.product.domain.entities;

import com.jifenke.lepluslive.weixin.domain.entities.Category;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 商品模块 Created by zhangwen on 2017/3/29.
 */
@Entity
@Table(name = "PRODUCT_MODULE")
public class ProductModule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date dateCreated = new Date();

  private Integer state = 1;  //上线=1|下线=0

  private Integer rank; // 每个模块的排序 正整数 越大越靠前

  private String picture;   //首页展示配图

  @ManyToOne
  private Category module; //商品所属模块

  @ManyToOne
  private Product product;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getRank() {
    return rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Category getModule() {
    return module;
  }

  public void setModule(Category module) {
    this.module = module;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }
}
