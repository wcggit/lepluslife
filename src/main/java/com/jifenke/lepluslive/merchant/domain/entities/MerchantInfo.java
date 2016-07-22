package com.jifenke.lepluslive.merchant.domain.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/5/5.
 */
@Entity
@Table(name = "MERCHANT_INFO")
public class MerchantInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private BigDecimal star = new BigDecimal(0);   //星级

  private Integer perSale = 0;  //客单价

  private Integer park = 1;   //有停车? 0=无

  private Integer wifi = 1;   //有wifi? 0=无

  private Integer card = 1;   //可刷卡? 0=不可

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getStar() {
    return star;
  }

  public void setStar(BigDecimal star) {
    this.star = star;
  }

  public Integer getPerSale() {
    return perSale;
  }

  public void setPerSale(Integer perSale) {
    this.perSale = perSale;
  }

  public Integer getPark() {
    return park;
  }

  public void setPark(Integer park) {
    this.park = park;
  }

  public Integer getWifi() {
    return wifi;
  }

  public void setWifi(Integer wifi) {
    this.wifi = wifi;
  }

  public Integer getCard() {
    return card;
  }

  public void setCard(Integer card) {
    this.card = card;
  }
}
