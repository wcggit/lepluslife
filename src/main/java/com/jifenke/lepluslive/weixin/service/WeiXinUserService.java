package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.lejiauser.BarcodeConfig;
import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.lejiauser.service.BarcodeService;
import com.jifenke.lepluslive.filemanage.service.FileImageService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/18.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinUserService {

  @Value("${bucket.ossBarCodeReadRoot}")
  private String barCodeRootUrl;

  @Inject
  private FileImageService fileImageService;

  @Inject
  private BarcodeService barcodeService;

  @Inject
  private WeiXinUserRepository weiXinUserRepository;

  @Inject
  private ScoreARepository scoreARepository;

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private ScoreBDetailRepository scoreBDetailRepository;

  @Inject
  private LeJiaUserRepository leJiaUserRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser findWeiXinUserByOpenId(String openId) {
    return weiXinUserRepository.findByOpenId(openId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser findWeiXinUserByUnionId(String unionId) {
    return weiXinUserRepository.findByUnionId(unionId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public WeiXinUser saveBarCodeForUser(WeiXinUser weiXinUser) throws IOException {
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    byte[]
        bytes =
        barcodeService.barcode(leJiaUser.getUserSid(), BarcodeConfig.Barcode.defaultConfig());
    String filePath = MvUtil.getFilePath(Constants.BAR_CODE_EXT);
    fileImageService.SaveUserBarCode(bytes, filePath);

    leJiaUser.setOneBarCodeUrl(barCodeRootUrl + "/" + filePath);
    leJiaUserRepository.save(leJiaUser);
    return weiXinUser;
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveWeiXinUser(Map<String, Object> userDetail, Map<String, Object> map)
      throws IOException {
    String openid = userDetail.get("openid").toString();
    String
        unionId =
        userDetail.get("unionid") != null ? userDetail.get("unionid").toString() : null;
    WeiXinUser weiXinUser = weiXinUserRepository.findByOpenId(openid);
    ScoreA scoreA = null;
    ScoreB scoreB = null;
    if (weiXinUser == null) {
      weiXinUser = new WeiXinUser();
      weiXinUser.setLastUpdated(new Date());
      LeJiaUser leJiaUser = new LeJiaUser();
      leJiaUser.setHeadImageUrl(userDetail.get("headimgurl").toString());
      leJiaUser.setWeiXinUser(weiXinUser);
      RegisterOrigin registerOrigin = new RegisterOrigin();
      registerOrigin.setId(1L);
      leJiaUser.setRegisterOrigin(registerOrigin);
      leJiaUserRepository.save(leJiaUser);
      weiXinUser.setLeJiaUser(leJiaUser);
      scoreA = new ScoreA();
      scoreA.setScore(0L);
      scoreA.setTotalScore(0L);
      scoreA.setLeJiaUser(leJiaUser);
      scoreARepository.save(scoreA);
      scoreB = new ScoreB();
      scoreB.setScore(0L);
      scoreB.setTotalScore(0L);
      scoreB.setLeJiaUser(leJiaUser);
      scoreBRepository.save(scoreB);
    }

    weiXinUser.setOpenId(openid);
    weiXinUser.setUnionId(unionId);
    weiXinUser.setCity(userDetail.get("city").toString());
    weiXinUser.setCountry(userDetail.get("country").toString());
    weiXinUser.setSex(Long.parseLong(userDetail.get("sex").toString()));
    weiXinUser.setNickname(userDetail.get("nickname").toString());
    weiXinUser.setLanguage(userDetail.get("language").toString());
    weiXinUser.setHeadImageUrl(userDetail.get("headimgurl").toString());
    weiXinUser.setProvince(userDetail.get("province").toString());
    weiXinUser.setAccessToken(map.get("access_token").toString());
    weiXinUser.setRefreshToken(map.get("refresh_token").toString());
    weiXinUser.setLastUserInfoDate(new Date());
    weiXinUser.setState(1);
    weiXinUserRepository.save(weiXinUser);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveWeiXinUserBySubscribe(Map<String, Object> userDetail)
      throws IOException {
    String openid = userDetail.get("openid").toString();
    String
        unionId =
        userDetail.get("unionid") != null ? userDetail.get("unionid").toString() : null;
    Date date = new Date();
    WeiXinUser weiXinUser = new WeiXinUser();
    weiXinUser.setLastUpdated(date);
    LeJiaUser leJiaUser = new LeJiaUser();
    leJiaUser.setHeadImageUrl(userDetail.get("headimgurl").toString());
    leJiaUser.setWeiXinUser(weiXinUser);
    RegisterOrigin registerOrigin = new RegisterOrigin();
    registerOrigin.setId(1L);
    leJiaUser.setRegisterOrigin(registerOrigin);
    leJiaUserRepository.save(leJiaUser);
    weiXinUser.setLeJiaUser(leJiaUser);
    ScoreA scoreA = new ScoreA();
    scoreA.setScore(0L);
    scoreA.setTotalScore(0L);
    scoreA.setLeJiaUser(leJiaUser);
    scoreARepository.save(scoreA);
    ScoreB scoreB = new ScoreB();
    scoreB.setScore(0L);
    scoreB.setTotalScore(0L);
    scoreB.setLeJiaUser(leJiaUser);
    scoreBRepository.save(scoreB);

    weiXinUser.setOpenId(openid);
    weiXinUser.setUnionId(unionId);
    weiXinUser.setCity(userDetail.get("city").toString());
    weiXinUser.setCountry(userDetail.get("country").toString());
    weiXinUser.setSex(Long.parseLong(userDetail.get("sex").toString()));
    weiXinUser.setNickname(userDetail.get("nickname").toString());
    weiXinUser.setLanguage(userDetail.get("language").toString());
    weiXinUser.setHeadImageUrl(userDetail.get("headimgurl").toString());
    weiXinUser.setProvince(userDetail.get("province").toString());
    weiXinUser.setLastUserInfoDate(date);
    weiXinUser.setState(1);
    weiXinUserRepository.save(weiXinUser);
  }

  /**
   * app的微信登录
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LeJiaUser saveWeiXinUserByApp(WeiXinUser weiXinUser, String unionId, String openid,
                                       String country, String city, String nickname,
                                       String province,
                                       String language, String headImgUrl, Long sex, String token)
      throws IOException {
    ScoreA scoreA = null;
    ScoreB scoreB = null;
    LeJiaUser leJiaUser = null;
    Date date = new Date();
    if (weiXinUser == null) {
      weiXinUser = new WeiXinUser();
      weiXinUser.setLastUpdated(date);
      leJiaUser = new LeJiaUser();
      leJiaUser.setHeadImageUrl(headImgUrl);
      leJiaUser.setToken(token);
      leJiaUser.setWeiXinUser(weiXinUser);
      RegisterOrigin registerOrigin = new RegisterOrigin();
      registerOrigin.setId(2L);
      leJiaUser.setRegisterOrigin(registerOrigin);
      leJiaUserRepository.save(leJiaUser);
      weiXinUser.setLeJiaUser(leJiaUser);
      scoreA = new ScoreA();
      scoreA.setScore(0L);
      scoreA.setTotalScore(0L);
      scoreA.setLeJiaUser(leJiaUser);
      scoreARepository.save(scoreA);
      scoreB = new ScoreB();
      scoreB.setScore(0L);
      scoreB.setTotalScore(0L);
      scoreB.setLeJiaUser(leJiaUser);
      scoreBRepository.save(scoreB);
    }

    weiXinUser.setAppOpenId(openid);
    weiXinUser.setUnionId(unionId);
    weiXinUser.setCity(city);
    weiXinUser.setCountry(country);
    weiXinUser.setSex(sex);
    weiXinUser.setNickname(nickname);
    weiXinUser.setLanguage(language);
    weiXinUser.setHeadImageUrl(headImgUrl);
    weiXinUser.setProvince(province);
    weiXinUser.setLastUserInfoDate(date);
    weiXinUser.setState(1);
    weiXinUserRepository.save(weiXinUser);

    return leJiaUser;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void openHongBao(WeiXinUser weiXinUser, String phoneNumber) {
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    leJiaUser.setPhoneNumber(phoneNumber);
    leJiaUserRepository.save(leJiaUser);

//    ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser);
    ScoreB scoreB = scoreBRepository.findByLeJiaUser(leJiaUser);
//    scoreA.setScore(scoreA.getScore() + 1000);
//    scoreA.setTotalScore(scoreA.getTotalScore() + 1000);
    Date date = new Date();
//    scoreA.setLastUpdateDate(date);
    scoreB.setLastUpdateDate(date);
    scoreB.setScore(scoreB.getScore() + 100);
    scoreB.setTotalScore(scoreB.getTotalScore() + 100);
    scoreBRepository.save(scoreB);
//    scoreARepository.save(scoreA);

//    ScoreADetail scoreADetail = new ScoreADetail();
//    scoreADetail.setNumber(1000L);
//    scoreADetail.setScoreA(scoreA);
//    scoreADetail.setOperate("关注送红包");
//    scoreADetailRepository.save(scoreADetail);

    ScoreBDetail scoreBDetail = new ScoreBDetail();
    scoreBDetail.setNumber(100L);
    scoreBDetail.setScoreB(scoreB);
    scoreBDetail.setOperate("关注送积分");
    scoreBDetailRepository.save(scoreBDetail);
    weiXinUser.setHongBaoState(1);
    weiXinUserRepository.save(weiXinUser);
  }
}
