package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.product.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * 金币商品 Created by zhangwen on 2017/2/20.
 */
@Service
@Transactional(readOnly = true)
public class GoldProductService {

  @Inject
  private ProductRepository productRepository;

  /**
   * 分页获取金币商品列表 2017/2/20
   *
   * @param currPage 第几页
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findHotProductListByPage(Integer currPage) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = productRepository.findGoldProductListByPage((currPage - 1) * 10, 10);
    if (list != null && list.size() > 0) {
      for (Object[] o : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", o[0]);
        map.put("name", o[1]);
        map.put("price", o[2]);
//        map.put("minPrice", o[3]);
        map.put("minScore", o[3]);
        map.put("picture", o[4]);
//        map.put("thumb", o[6]);
        map.put("saleNumber", (int) o[5] + (int) o[6]);
//        map.put("hotStyle", o[9]);
        map.put("postage", o[7]);
//        map.put("buyLimit", o[11]);
//        map.put("repository", o[12]);
        mapList.add(map);
      }
      return mapList;
    }
    return null;
  }

}
