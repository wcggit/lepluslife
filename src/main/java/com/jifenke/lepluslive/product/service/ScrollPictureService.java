package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.repository.ScrollPictureRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/11.
 */
@Service
@Transactional(readOnly = true)
public class ScrollPictureService {

  @Inject
  private ScrollPictureRepository repository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ScrollPicture> findAllByProduct(Product product) {
    return repository.findAllByProductOrderBySid(product);
  }
}
