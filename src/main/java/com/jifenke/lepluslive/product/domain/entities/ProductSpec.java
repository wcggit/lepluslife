package com.jifenke.lepluslive.product.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * Created by wcg on 16/3/16.
 */
@Entity
@Table(name = "PRODUCT_SPEC")
public class ProductSpec {

  public ProductSpec(Long id) {
    this.id = id;
  }

  public ProductSpec() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JsonIgnore
  private Product product;

  @NotNull
  private Integer repository = 0;

  @Version
  private Long version = 0L;

  private String specDetail;

  @NotNull
  private String picture;

  @NotNull
  private Long price;

  @Column(name = "min_price")
  @NotNull
  private Long minPrice;

  private Integer minScore = 0;  //兑换最低所需金币

  private Integer saleNumber = 0; //销售量

  @JsonIgnore
  private Integer state;

  private Integer costPrice = 0;  //成本价

  private Integer otherPrice = 0;  //其他费用

  public Integer getCostPrice() {
    return costPrice;
  }

  public void setCostPrice(Integer costPrice) {
    this.costPrice = costPrice;
  }

  public Integer getOtherPrice() {
    return otherPrice;
  }

  public void setOtherPrice(Integer otherPrice) {
    this.otherPrice = otherPrice;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getRepository() {
    return repository;
  }

  public void setRepository(Integer repository) {
    this.repository = repository;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getSpecDetail() {
    return specDetail;
  }

  public void setSpecDetail(String specDetail) {
    this.specDetail = specDetail;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Long getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Long minPrice) {
    this.minPrice = minPrice;
  }

  public Integer getMinScore() {
    return minScore;
  }

  public void setMinScore(Integer minScore) {
    this.minScore = minScore;
  }

  public Integer getSaleNumber() {
    return saleNumber;
  }

  public void setSaleNumber(Integer saleNumber) {
    this.saleNumber = saleNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProductSpec)) {
      return false;
    }

    ProductSpec that = (ProductSpec) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
