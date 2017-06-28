package com.jifenke.lepluslive.product.domain.entities;

import com.jifenke.lepluslive.weixin.domain.entities.Category;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 线上商品
 * Created by zhangwen on 16/3/9.
 */
@Entity
@Table(name = "PRODUCT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long sid;

  @NotNull
  private String name;

  private Integer type = 1; //商品类型 1=常规|2=限量|3=秒杀|4=金币

  @NotNull
  private String picture;

  @NotNull
  private Integer price; //市场价

  @Column(name = "min_price")
  @NotNull
  private Integer minPrice; //购买最低金额

  private Integer minScore = 0;  //兑换最低所需积分

  @Column(name = "sale_num")
  private Integer saleNumber = 0;  //销售数量

  private Integer customSale = 0;  //自定义起始销售量

  private String description;

  private Integer state;

  private String thumb;//缩略图,显示在订单里

  private String qrCodePicture;

  private Integer postage = 0;   //该商品所需邮费 0=包邮

  private Integer freePrice = 0;  //不包邮时，满此价格包邮，针对普通商品  =0是 不包邮

  private Integer buyLimit = 1;  //每个用户限购数量 0=无限制    临时：100=banner-img|50=products

  private Integer hotStyle = 0;  //1=爆款  临时：2=数码|3=北京公交卡|4=加油卡|5=美妆个护|6=神奇百货

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_type_id")
  private ProductType productType;

  @ManyToOne
  private Category mark; //商品角标  为null时无角标

  private Integer isBackRed = 0;    //是否返红包 1返 0不返
  private Integer backRedType = 2;  //返红包类型 1比例返还 2金额返还
  private Integer backRatio = 0;    //返还比例
  private Integer backMoney = 0;    //返还金额

  public String getThumb() {
    return thumb;
  }

  public void setThumb(String thumb) {
    this.thumb = thumb;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSid() {
    return sid;
  }

  public void setSid(Long sid) {
    this.sid = sid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Integer minPrice) {
    this.minPrice = minPrice;
  }

  public Integer getSaleNumber() {
    return saleNumber;
  }

  public void setSaleNumber(Integer saleNumber) {
    this.saleNumber = saleNumber;
  }

  public String getQrCodePicture() {
    return qrCodePicture;
  }

  public void setQrCodePicture(String qrCodePicture) {
    this.qrCodePicture = qrCodePicture;
  }

  public Integer getFreePrice() {
    return freePrice;
  }

  public void setFreePrice(Integer freePrice) {
    this.freePrice = freePrice;
  }

  public Category getMark() {
    return mark;
  }

  public void setMark(Category mark) {
    this.mark = mark;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getMinScore() {
    return minScore;
  }

  public void setMinScore(Integer minScore) {
    this.minScore = minScore;
  }

  public Integer getCustomSale() {
    return customSale;
  }

  public void setCustomSale(Integer customSale) {
    this.customSale = customSale;
  }

  public Integer getPostage() {
    return postage;
  }

  public void setPostage(Integer postage) {
    this.postage = postage;
  }

  public Integer getHotStyle() {
    return hotStyle;
  }

  public void setHotStyle(Integer hotStyle) {
    this.hotStyle = hotStyle;
  }

  public Integer getBuyLimit() {
    return buyLimit;
  }

  public void setBuyLimit(Integer buyLimit) {
    this.buyLimit = buyLimit;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getIsBackRed() {
    return isBackRed;
  }

  public void setIsBackRed(Integer isBackRed) {
    this.isBackRed = isBackRed;
  }

  public Integer getBackRedType() {
    return backRedType;
  }

  public void setBackRedType(Integer backRedType) {
    this.backRedType = backRedType;
  }

  public Integer getBackRatio() {
    return backRatio;
  }

  public void setBackRatio(Integer backRatio) {
    this.backRatio = backRatio;
  }

  public Integer getBackMoney() {
    return backMoney;
  }

  public void setBackMoney(Integer backMoney) {
    this.backMoney = backMoney;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Product)) {
      return false;
    }

    Product product = (Product) o;

    if (id != null ? !id.equals(product.id) : product.id != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
