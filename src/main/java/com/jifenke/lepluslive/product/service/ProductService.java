package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.repository.ProductDetailRepository;
import com.jifenke.lepluslive.product.repository.ProductRepository;
import com.jifenke.lepluslive.product.repository.ProductSpecRepository;
import com.jifenke.lepluslive.product.repository.ProductTypeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by wcg on 16/3/9.
 */
@Service
@Transactional(readOnly = true)
public class ProductService {

  @Inject
  private ProductRepository productRepository;

  @Inject
  private ProductTypeRepository productTypeRepository;

  @Inject
  private ProductSpecRepository productSpecRepository;

  @Inject
  private ProductDetailRepository productDetailRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Product> findProductsByPage(Integer offset, Integer productType) {
    if (offset == null) {
      offset = 1;
    }

    Page<Product>
        page =
        productRepository.findByStateAndProductType(
            new PageRequest(offset - 1, 10, new Sort(Sort.Direction.ASC, "sid")), 1,
            new ProductType(
                productType));
    return page.getContent();
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Product findOneProduct(Long id) {
    return productRepository.findOne(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ProductSpec> findAllProductSpec(Product product) {
    return productSpecRepository.findAllByProductAndState(product,1);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void editProduct(Product product) throws Exception {
    Product origin = productRepository.findOne(product.getId());
    origin.setSid(product.getSid());
    origin.setName(product.getName());
    origin.setMinPrice(product.getMinPrice());
    origin.setPacketCount(product.getPacketCount());
    origin.setPicture(product.getPicture());
    origin.setPointsCount(product.getPointsCount());
    origin.setSaleNumber(product.getSaleNumber());
    origin.setState(product.getState());
    origin.setPrice(product.getPrice());
    productRepository.save(product);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LejiaResult putOnProduct(Long id, Long sid) {
    Product product = productRepository.findOne(id);
    if (product == null) {
      return LejiaResult.build(500, "商品不存在");
    }
    product.setSid(sid);
    productRepository.save(product);
    return LejiaResult.build(200, "成功上架商品");
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LejiaResult pullOffProduct(Long id) {
    Product product = productRepository.findOne(id);
    if (product == null) {
      return LejiaResult.build(500, "商品不存在");
    }
    product.setSid(0L);
    productRepository.save(product);
    return LejiaResult.build(200, "成功下架商品");
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Long getTotalCount() {
    return productRepository.getTotalCount();
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void editProductType(ProductType productType) {
    ProductType productTypeOri = productTypeRepository.findOne(productType.getId());
    productTypeOri.setType(productType.getType());
    productTypeRepository.save(productTypeOri);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ProductType> findAllProductType() {
    return productTypeRepository.findAll();
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void deleteProductType(Integer id) {
    productTypeRepository.delete(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void addProductType(ProductType productType) {
    productTypeRepository.save(productType);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ProductType findOneProductType(Integer id) {
    return productTypeRepository.findOne(id);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ProductDetail> findAllProductDetailsByProduct(Product product) {
    return productDetailRepository.findAllByProductOrderBySid(product);
  }

  public List<Product> findProducts() {
    return productRepository.findAll();
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public ProductSpec editProductSpecRepository(Long productSpecId, int productNum) {
    ProductSpec productSpec = productSpecRepository.findOne(productSpecId);
    Integer repository = productSpec.getRepository() - productNum;
    if(repository>=0) {
      productSpec.setRepository(repository);
      productSpecRepository.save(productSpec);
      return  productSpec;
    }

    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void orderCancle(List<OrderDetail> orderDetails) {
    for (OrderDetail orderDetail : orderDetails) {
      ProductSpec productSpec = orderDetail.getProductSpec();
      Integer repository = productSpec.getRepository() + orderDetail.getProductNumber();
      productSpec.setRepository(repository);
      productSpecRepository.save(productSpec);
    }

  }

  public ProductSpec findOneProductSpec(Long productSpec) {
    return productSpecRepository.findOne(productSpec);
  }
}
