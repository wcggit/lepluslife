package com.jifenke.lepluslive.product.controller.dto;


import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;

import java.util.List;


/**
 * Created by wcg on 16/3/17.
 */
public class ProductDto {

  private Long id;

  //序号
  private Long sid;

  private String name;

  private String picture;

  private Integer price;
  //最低价
  private Integer minPrice;
  //库存
  private Integer repository;
  //已售
  private Integer saleNumber;
  // 该商品的所有订单使用的积分加和
  private Long pointsCount;
  //该商品的所有订单发放的红包加和
  private Long packetCount;

  private Integer freePrice; //免邮价格

  private String description;

  private String thumb; //商品缩略图

  private Integer state;

  private ProductType productType;

  private List<ProductSpec> productSpecs; //商品的规格

  //商品详情图
  private List<ProductDetail> productDetails;

  //轮播图
  private List<ScrollPicture> scrollPictures;

  public Integer getFreePrice() {
    return freePrice;
  }

  public void setFreePrice(Integer freePrice) {
    this.freePrice = freePrice;
  }

  public String getThumb() {
    return thumb;
  }

  public void setThumb(String thumb) {
    this.thumb = thumb;
  }

  public List<ProductSpec> getProductSpecs() {
    return productSpecs;
  }

  public void setProductSpecs(List<ProductSpec> productSpecs) {
    this.productSpecs = productSpecs;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public Integer getRepository() {
    return repository;
  }

  public void setRepository(Integer repository) {
    this.repository = repository;
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


  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public List<ScrollPicture> getScrollPictures() {
    return scrollPictures;
  }

  public void setScrollPictures(List<ScrollPicture> scrollPictures) {
    this.scrollPictures = scrollPictures;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public List<ProductDetail> getProductDetails() {
    return productDetails;
  }

  public void setProductDetails(List<ProductDetail> productDetails) {
    this.productDetails = productDetails;
  }
}
