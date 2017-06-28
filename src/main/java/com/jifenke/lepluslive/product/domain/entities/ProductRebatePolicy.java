package com.jifenke.lepluslive.product.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 规格分润、返鼓励金规则
 * Created by zhangwen on 2017/6/26.
 */
@Entity
@Table(name = "PRODUCT_REBATE_POLICY")
public class ProductRebatePolicy {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  private ProductSpec productSpec;

  private Date dateCreated = new Date();

  private Date dateUpdated = new Date();

  private Long rebateScore = 0L;  //返鼓励金

  private Long commission = 0L;  //总佣金

  private Long toMerchant = 0L; //绑定商户返佣金额

  private Long toPartner = 0L;  //绑定合伙人返佣金额

  private Long toPartnerManager = 0L;  //绑定城市合伙人返佣金额

  private Long toLePlusLife = 0L;  //绑定城市合伙人返佣金额

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductSpec getProductSpec() {
    return productSpec;
  }

  public void setProductSpec(ProductSpec productSpec) {
    this.productSpec = productSpec;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Long getRebateScore() {
    return rebateScore;
  }

  public void setRebateScore(Long rebateScore) {
    this.rebateScore = rebateScore;
  }

  public Long getCommission() {
    return commission;
  }

  public void setCommission(Long commission) {
    this.commission = commission;
  }

  public Long getToMerchant() {
    return toMerchant;
  }

  public void setToMerchant(Long toMerchant) {
    this.toMerchant = toMerchant;
  }

  public Long getToPartner() {
    return toPartner;
  }

  public void setToPartner(Long toPartner) {
    this.toPartner = toPartner;
  }

  public Long getToPartnerManager() {
    return toPartnerManager;
  }

  public void setToPartnerManager(Long toPartnerManager) {
    this.toPartnerManager = toPartnerManager;
  }

  public Long getToLePlusLife() {
    return toLePlusLife;
  }

  public void setToLePlusLife(Long toLePlusLife) {
    this.toLePlusLife = toLePlusLife;
  }

  public Date getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(Date dateUpdated) {
    this.dateUpdated = dateUpdated;
  }
}
