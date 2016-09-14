package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUserInfo;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserInfoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
  private DictionaryService dictionaryService;

  /**
   * 检测是否引导和是否弹出填写手机号弹窗 16/09/09
   *
   * @param weiXinUser 用户信息
   * @return 结果
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Integer> checkYdAndWarning(WeiXinUser weiXinUser) {
    int yd = 1, phone = 1, hasPhone = 1;//1=引导过和7天内弹窗过或有手机号
    Date date = new Date();
    WeiXinUserInfo info = null;
    String version = dictionaryService.findDictionaryById(28L).getValue();
    String phoneNumber = weiXinUser.getLeJiaUser().getPhoneNumber();
    List<WeiXinUserInfo> list = weiXinUserInfoRepository.findByWeiXinUser(weiXinUser);
    Map<String, Integer> map = new HashMap<>();
    if (list != null && list.size() > 0) {
      info = list.get(0);
      if (!version.equals(info.getVersion())) {//版本更新,出现引导
        yd = 0;
        info.setVersion(version);
      }
      if (info.getYinDao() == 0) {
        yd = 0;
        info.setYinDao(1);
      }
      if (phoneNumber == null || "".equals(phoneNumber)) {
        hasPhone = 0;
        //判断7天内是否引导过
        Date lastOpenDate = info.getLastOpenDate();
        long between = date.getTime() - lastOpenDate.getTime();
        if (between > 7 * 24 * 3600000) {//出现弹窗
          phone = 0;
          info.setLastOpenDate(date);  //记录最后一次电话弹窗时间
        }
      }
      if (phone == 0 || yd == 0) {
        weiXinUserInfoRepository.save(info);
      }
    } else { //出现引导
      yd = 0;
      info = new WeiXinUserInfo();
      info.setWeiXinUser(weiXinUser);
      info.setCreateDate(date);
      info.setLastOpenDate(date);
      info.setVersion(version);
      if (phoneNumber == null || "".equals(phoneNumber)) {//出现弹窗
        hasPhone = 0;
        phone = 0;
      }
      weiXinUserInfoRepository.save(info);
    }
    map.put("phone", phone);
    map.put("yd", yd);
    map.put("hasPhone", hasPhone);
    return map;
  }

}
