package com.jifenke.lepluslive.product.controller;


import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.product.controller.dto.ProductDto;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.product.service.ScrollPictureService;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;

/**
 * Created by wcg on 16/3/9.
 */
@RestController
@RequestMapping("shop")
public class ProductController {

  @Inject
  private ProductService productService;

  @Inject
  private ScrollPictureService scrollPictureService;

  @Inject
  private DictionaryService dictionaryService;

  @ApiOperation(value = "获取所有的商品类别名称及顶部图片")
  @RequestMapping(value = "/type", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult findAllProductType() {
    return LejiaResult.ok(productService.findAllProductType());
  }

  //分页
  @ApiOperation(value = "查看商品列表")
  @RequestMapping(value = "/product", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public
  @ResponseBody
  List<ProductDto> findPageProduct(
      @RequestParam(value = "page", required = false) Integer offset,
      @RequestParam(value = "productType", required = true) Integer productType) {
    List<ProductDto> products = productService
        .findProductsByPage(offset, productType).stream()
        .map(product -> {
          ProductDto productDto = new ProductDto();
          try {
            BeanUtils.copyProperties(productDto, product);

            productDto.setMarkType(product.getMark() == null ? 0 : product.getMark().getType());

          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
          return productDto;
        }).collect(Collectors.toList());
    return products;

  }


  @RequestMapping(value = "/product/productType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public
  @ResponseBody
  List<ProductType> goProductTypePage() {
    return productService.findAllProductType();
  }

  @ApiOperation(value = "查看商品详情")
  @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public
  @ResponseBody
  ProductDto getProductDetail(@PathVariable Long id) {
    Product product = productService.findOneProduct(id);
    if (product != null) {
      List<ScrollPicture> scrollPictureList = scrollPictureService.findAllByProduct(product);
      ProductType productType = productService.findOneProductType(product.getProductType().getId());
      ProductDto productDto = new ProductDto();
      productDto.setProductSpecs(productService.findAllProductSpec(product));
      Integer
          FREIGHT_FREE_PRICE =
          Integer.parseInt(dictionaryService.findDictionaryById(1L).getValue());
      productDto.setFreePrice(FREIGHT_FREE_PRICE);
      try {
        BeanUtils.copyProperties(productDto, product);
        productDto.setProductType(productType);
        productDto.setScrollPictures(scrollPictureList.stream().map(scrollPicture -> {
          scrollPicture.setProduct(null);
          return scrollPicture;
        }).collect(
            Collectors.toList()));
        productDto.setProductDetails(productService.findAllProductDetailsByProduct(product));
      } catch (Exception e) {
        e.printStackTrace();
      }
      return productDto;
    }
    return null;
  }

  /**
   * 获取臻品列表  16/09/21
   *
   * @param page   当前页码
   * @param typeId 臻品类型 0=所有
   */
  @RequestMapping(value = "/productList", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult productList(@RequestParam(required = true) Integer page,
                          @RequestParam(required = true) Integer typeId) {
    if (page == null || page < 1) {
      page = 1;
    }
    if (typeId == null || typeId < 0) {
      typeId = 0;
    }
    List<Map> list = productService.findProductListByTypeAndPage(page, typeId);
    return LejiaResult.ok(list);
  }


}
