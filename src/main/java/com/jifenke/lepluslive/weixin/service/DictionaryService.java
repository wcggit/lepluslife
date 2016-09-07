package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.Dictionary;
import com.jifenke.lepluslive.weixin.repository.DictionaryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/5/25.
 */
@Service
@Transactional(readOnly = true)
public class DictionaryService {

  @Inject
  private DictionaryRepository dictionaryRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Dictionary findDictionaryById(Long id) {

    return dictionaryRepository.findOne(id);
  }

  //获取分享文案 06/09/01
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Map<String,String> findInvite() {
    Map<String, String> map = new HashMap<>();
    map.put("pic", dictionaryRepository.getOne(22L).getValue());
    map.put("title", dictionaryRepository.getOne(23L).getValue());
    map.put("content", dictionaryRepository.getOne(24L).getValue());
    map.put("url", dictionaryRepository.getOne(25L).getValue());
    return map;
  }

}
