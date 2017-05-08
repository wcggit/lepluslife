package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.repository.ProductDetailRepository;
import com.jifenke.lepluslive.product.repository.ProductRepository;
import com.jifenke.lepluslive.product.repository.ProductSpecRepository;
import com.jifenke.lepluslive.product.repository.ProductTypeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  /**
   * 支付成功后修改订单中product的销售量 16/09/27
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void editProductSaleByPayOrder(OnLineOrder order) throws Exception {
    List<OrderDetail> list = order.getOrderDetails();
    try {
      for (OrderDetail detail : list) {
        Product product = detail.getProduct();
        product
            .setSaleNumber((product.getSaleNumber() == null ? 0 : product.getSaleNumber()) + detail
                .getProductNumber());
        productRepository.save(product);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Product> findProductsByPage(Integer offset, Integer productType) {
    if (offset == null) {
      offset = 1;
    }

    Page<Product>
        page =
        productRepository.findByStateAndProductTypeAndType(
            new PageRequest(offset - 1, 10, new Sort(Sort.Direction.ASC, "sid")), 1,
            new ProductType(
                productType), 1);
    return page.getContent();
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Product findOneProduct(Long id) {
    return productRepository.findOne(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ProductSpec> findAllProductSpec(Product product) {
    return productSpecRepository.findAllByProductAndState(product, 1);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ProductType> findAllProductType() {
    return productTypeRepository.findAll();
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ProductType findOneProductType(Integer id) {
    return productTypeRepository.findOne(id);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ProductDetail> findAllProductDetailsByProduct(Product product) {
    return productDetailRepository.findAllByProductOrderBySid(product);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public ProductSpec editProductSpecRepository(Long productSpecId, int productNum) {
    ProductSpec productSpec = productSpecRepository.findOne(productSpecId);
    Integer repository = productSpec.getRepository() - productNum;
    if (repository >= 0) {
      productSpec.setRepository(repository);
      productSpecRepository.save(productSpec);
      return productSpec;
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

  /**
   * 分页获取爆品列表 16/09/21
   *
   * @param currPage 第几页
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findHotProductListByPage(Integer currPage) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = productRepository.findHotProductListByPage((currPage - 1) * 10, 10);
    if (list != null && list.size() > 0) {
      for (Object[] o : list) {
        Map<String, Object> map = convertData(o);
        map.put("repository", o[12]);
        mapList.add(map);
      }
      return mapList;
    }
    return null;
  }

  /**
   * 分页获取臻品列表 16/09/21
   *
   * @param currPage 第几页
   * @param typeId   臻品类型  0=所有类型
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findProductListByTypeAndPage(Integer currPage, Integer typeId) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = null;
    if (typeId == 0) { //不分类
      list = productRepository.findProductListByPage((currPage - 1) * 10, 10);
    } else { //分类
      list = productRepository.findProductListByTypeAndPage(typeId, (currPage - 1) * 10, 10);
    }
    if (list != null && list.size() > 0) {
      for (Object[] o : list) {
        Map<String, Object> map = convertData(o);
        map.put("repository", o[12]);
        mapList.add(map);
      }
      return mapList;
    }
    return null;
  }

  private Map<String, Object> convertData(Object[] o) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", o[0]);
    map.put("name", o[1]);
    map.put("price", o[2]);
    map.put("minPrice", o[3]);
    map.put("minScore", o[4]);
    map.put("picture", o[5]);
    map.put("thumb", o[6]);
    map.put("saleNumber", (int) o[7] + (int) o[8]);
    map.put("hotStyle", o[9]);
    map.put("postage", o[10]);
    map.put("buyLimit", o[11]);
    return map;
  }

  //  /**
//   * 获取主打爆品 16/09/21
//   */
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//  public Map findMainHotProduct() {
//    List<Object[]> list = productRepository.findMainHotProductList();
//    if (list != null && list.size() > 0) {
//      Object[] o = list.get(0);
//      Map<String, Object> map = convertData(o);
//      map.put("repository", o[12]);
//      return map;
//    }
//    return null;
//  }
}
