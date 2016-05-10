package com.jifenke.lepluslive.weixin.controller.dto;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;

/**
 * Created by wcg on 16/4/18.
 */
public class CartDetailDto {

  private Product product;

  private ProductSpec productSpec;

  private Integer productNumber;

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public ProductSpec getProductSpec() {
    return productSpec;
  }

  public void setProductSpec(ProductSpec productSpec) {
    this.productSpec = productSpec;
  }

  public Integer getProductNumber() {
    return productNumber;
  }

  public void setProductNumber(Integer productNumber) {
    this.productNumber = productNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CartDetailDto that = (CartDetailDto) o;

    if (product != null ? !product.equals(that.product) : that.product != null) {
      return false;
    }
    if (productSpec != null ? !productSpec.equals(that.productSpec) : that.productSpec != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = product != null ? product.hashCode() : 0;
    result = 31 * result + (productSpec != null ? productSpec.hashCode() : 0);
    return result;
  }
}
