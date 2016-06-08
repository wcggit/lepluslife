package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUserInfo;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserInfoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/6/8.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinUserInfoService {

  @Inject
  private WeiXinUserInfoRepository weiXinUserInfoRepository;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUserInfo findWeiXinUserInfoByOpenId(String openId) {
    return weiXinUserInfoRepository.findByOpenId(openId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveWeiXinUserInfo(Map map) {
    String openId = map.get("FromUserName").toString();
    WeiXinUserInfo weiXinUserInfo = findWeiXinUserInfoByOpenId(openId);
    if (weiXinUserInfo == null) {
      weiXinUserInfo = new WeiXinUserInfo();
      weiXinUserInfo.setOpenId(openId);
      WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
      if (weiXinUser != null) {
        weiXinUserInfo.setWeiXinUser(weiXinUser);
      }
    }
    if (map.get("Latitude") != null) {
      weiXinUserInfo.setLatitude(Double.valueOf((String) map.get("Latitude")));
    }
    if (map.get("Longitude") != null) {
      weiXinUserInfo.setLongitude(Double.valueOf((String) map.get("Longitude")));
    }
//    if (map.get("Precision") != null) {
//      weiXinUserInfo.setPrecision(Double.valueOf((String) map.get("Precision")));
//    }
    try {
      weiXinUserInfoRepository.save(weiXinUserInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
