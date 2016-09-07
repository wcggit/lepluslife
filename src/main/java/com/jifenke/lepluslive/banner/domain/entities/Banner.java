package com.jifenke.lepluslive.banner.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.product.domain.entities.Product;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 各种轮播图 Created by zhangwen on 2016/8/26.
 */
@Entity
@Table(name = "BANNER")
public class Banner {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private BannerType bannerType;

  @ManyToOne
  private Merchant merchant;

  @ManyToOne
  private Product product;

  @ManyToOne
  private City city;

  private Date createDate;   //创建时间

  private Date lastUpDate;  //最近更新时间

  @NotNull
  private String picture;               //轮播图

  private String oldPicture;            //往期推荐图

  private Integer afterType = 1;        //后置类型  1=链接  2=商品   3=商家

  private String url;                   //轮播图对应的html的链接

  private String urlTitle;              //轮播图对应的html页面标题

  private Integer sid = 1;              //序号

  private Integer status = 1;           //状态  1=正常  0=下架

  private Integer alive = 1;          //当期往期   1=当期   0=往期

  private String title;                  //图片上的文案

  private String introduce;               //介绍

  private Integer price = 0;              //商品价格

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BannerType getBannerType() {
    return bannerType;
  }

  public void setBannerType(BannerType bannerType) {
    this.bannerType = bannerType;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getSid() {
    return sid;
  }

  public void setSid(Integer sid) {
    this.sid = sid;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Date getLastUpDate() {
    return lastUpDate;
  }

  public void setLastUpDate(Date lastUpDate) {
    this.lastUpDate = lastUpDate;
  }

  public Integer getAlive() {
    return alive;
  }

  public Integer getAfterType() {
    return afterType;
  }

  public void setAfterType(Integer afterType) {
    this.afterType = afterType;
  }

  public void setAlive(Integer alive) {
    this.alive = alive;
  }

  public String getIntroduce() {
    return introduce;
  }

  public void setIntroduce(String introduce) {
    this.introduce = introduce;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public String getOldPicture() {
    return oldPicture;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public void setOldPicture(String oldPicture) {
    this.oldPicture = oldPicture;
  }

  public String getUrlTitle() {
    return urlTitle;
  }

  public void setUrlTitle(String urlTitle) {
    this.urlTitle = urlTitle;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }
}
