package com.jifenke.lepluslive.product.domain.entities;

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
 * Created by wcg on 16/3/9.
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

  private Integer type = 1; //商品类型 1=常规|2=秒杀

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

  @Column(name = "points_count")
  private Long pointsCount = 0L;    // 该商品的所有订单使用的积分加和

  @Column(name = "packet_count")
  private Long packetCount = 0L;  //该商品的所有订单发放的红包加和

  private String description;

  private Integer state;

  private String thumb;//缩略图,显示在订单里

  private Integer postage = 0;   //该商品所需邮费

  private Integer buyLimit = 1;  //每个用户限购数量 0=无限制

  private Integer hotStyle = 0;  //1=爆款

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_type_id")
  private ProductType productType;

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

  public Long getPointsCount() {
    return pointsCount;
  }

  public void setPointsCount(Long pointsCount) {
    this.pointsCount = pointsCount;
  }

  public Long getPacketCount() {
    return packetCount;
  }

  public void setPacketCount(Long packetCount) {
    this.packetCount = packetCount;
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
