package com.jifenke.lepluslive.product.domain.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/3/11.
 */
@Entity
@Table(name = "PRODUCT_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductType {

  public ProductType(Integer id) {
    this.id = id;
  }

  public ProductType() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String type;

  private String picture;  //app不同商品规格对应的图片链接

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
