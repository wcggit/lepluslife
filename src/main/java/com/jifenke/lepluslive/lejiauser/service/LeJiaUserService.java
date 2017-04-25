package com.jifenke.lepluslive.lejiauser.service;

import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.lejiauser.controller.dto.LeJiaUserDto;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by wcg on 16/4/21.
 */
@Service
@Transactional(readOnly = true)
public class LeJiaUserService {

  @Value("${bucket.ossBarCodeReadRoot}")
  private String barCodeRootUrl;

  @Inject
  private LeJiaUserRepository leJiaUserRepository;

  @Inject
  private ScoreARepository scoreARepository;

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private MerchantService merchantService;
  @Inject
  private EntityManager em;


  /**
   * APP获取用户信息  16/09/07
   *
   * @param token 用户唯一标识
   * @return 个人信息
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Map getUserInfo(String token) {
    List<Object[]> list = leJiaUserRepository.getUserInfo(token);
    if (list.size() > 0) {
      Map<String, Object> map = new HashMap<>();
      Object[] o = list.get(0);
      map.put("nickname", o[0]);
      map.put("headImageUrl", o[1]);
      map.put("token", o[2]);
      map.put("phoneNumber", o[3]);
      map.put("scoreA", o[4]);
      map.put("scoreB", o[5]);
      map.put("bindMerchantId", o[6]);
      return map;
    }
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public LeJiaUser findUserByUserSid(String userSid) {
    return leJiaUserRepository.findByUserSid(userSid);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public LeJiaUser findUserById(Long id) {
    return leJiaUserRepository.findOne(id);
  }

  //保存用户信息 16/10/24
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveUser(LeJiaUser leJiaUser) {
    try {
      leJiaUserRepository.save(leJiaUser);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 判断该手机号是否已经注册
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public LeJiaUser findUserByPhoneNumber(String phoneNumber) {
    List<LeJiaUser> list = leJiaUserRepository.findByPhoneNumber(phoneNumber);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * @param phoneNumber 手机号
   * @param token       推送验证码
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LeJiaUserDto register(String phoneNumber, String token) throws IOException {

    LeJiaUser leJiaUser = new LeJiaUser();
    leJiaUser.setToken(token);
    leJiaUser.setPhoneNumber(phoneNumber);
    //设置条形码
//    byte[]
//        bytes =
//        barcodeService.barcode(leJiaUser.getUserSid(), BarcodeConfig.Barcode.defaultConfig());
//    String filePath = MvUtil.getFilePath(Constants.BAR_CODE_EXT);
//    fileImageService.SaveUserBarCode(bytes, filePath);
//    leJiaUser.setOneBarCodeUrl(barCodeRootUrl + "/" + filePath);

    RegisterOrigin registerOrigin = new RegisterOrigin();
    registerOrigin.setId(2L);
    leJiaUser.setRegisterOrigin(registerOrigin);
    leJiaUserRepository.save(leJiaUser);
    ScoreA scoreA = new ScoreA();
    scoreA.setScore(0L);
    scoreA.setLeJiaUser(leJiaUser);
    scoreARepository.save(scoreA);
    ScoreB scoreB = new ScoreB();
    scoreB.setScore(0L);
    scoreB.setLeJiaUser(leJiaUser);
    scoreBRepository.save(scoreB);

    return new LeJiaUserDto(scoreA.getScore(), scoreB.getScore(),
                            leJiaUser.getOneBarCodeUrl(), leJiaUser.getUserSid(),
                            leJiaUser.getHeadImageUrl(), leJiaUser.getPhoneNumber(),
                            leJiaUser.getPhoneNumber());
  }

  /**
   * 设置密码
   *
   * @param pwd 加密前密码
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void setPwd(LeJiaUser leJiaUser, String pwd) {
    String md5Pwd = MD5Util.MD5Encode(pwd, null);
    leJiaUser.setPwd(md5Pwd);
    leJiaUserRepository.save(leJiaUser);
  }


  /**
   * 登录
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LeJiaUser login(LeJiaUser leJiaUser, String pwd, String token) {

    if (pwd != null) {
      if (!MD5Util.MD5Encode(pwd, null).equals(leJiaUser.getPwd())) {
        return null;
      }
    } else {
      return null;
    }

    if (token != null && (!token.equals(leJiaUser.getToken()))) { //更新推送token
      leJiaUser.setToken(token);
      leJiaUserRepository.save(leJiaUser);
    }
    return leJiaUser;
  }

  /**
   * 根据关注来源,判断是否需要绑定商户和合伙人  16/09/20
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Merchant checkUserBindMerchant(WeiXinUser weiXinUser) {
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    String subSource = weiXinUser.getSubSource();
    Merchant merchant = null;
    //判断是否需要绑定商户
    if (subSource != null && subSource.startsWith("4")) {
      if (leJiaUser.getBindMerchant() == null) {
        Long merchantId = Long.valueOf(subSource.split("_")[2]);
        if (merchantId != null) {
          merchant = bindMerchantAndPartner(merchantId, leJiaUser);
        }
      }
    }
    return merchant;
  }

  /**
   * 会员扫码，绑定商户但不修改关注来源  16/09/29 判断是否需要绑定商户和合伙人
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void memberSubBindMerchant(WeiXinUser weiXinUser, Long merchantId) {
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    bindMerchantAndPartner(merchantId, leJiaUser);
  }

  /**
   * 绑定商户及合伙人 16/09/29
   *
   * @param merchantId 商户ID
   * @param leJiaUser  乐加用户
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Merchant bindMerchantAndPartner(Long merchantId, LeJiaUser leJiaUser) {
    Merchant merchant = merchantService.findMerchantById(merchantId);
    Partner partner = merchant.getPartner();
    Date date = new Date();
    if (merchant.getPartnership() != 2) {
      long userLimit = leJiaUserRepository.countMerchantBindLeJiaUser(merchantId);
      if (merchant.getUserLimit() > userLimit) {

        leJiaUser.setBindMerchant(merchant);
        leJiaUser.setBindMerchantDate(date);
        long
            partnerUserLimit =
            leJiaUserRepository.countPartnerBindLeJiaUser(partner.getId());
        if (partner.getUserLimit() > partnerUserLimit) {
          leJiaUser.setBindPartner(partner);
          leJiaUser.setBindPartnerDate(date);
        }
      }
    } else { //虚拟商户操作
      long partnerUserLimit = leJiaUserRepository.countPartnerBindLeJiaUser(partner.getId());
      if (partner.getUserLimit() > partnerUserLimit) {
        leJiaUser.setBindPartner(partner);
        leJiaUser.setBindPartnerDate(date);
        leJiaUser.setBindMerchant(merchant);
        leJiaUser.setBindMerchantDate(date);
      }
    }
    return merchant;
  }

  public long countPartnerBindLeJiaUser(Long id) {
    return leJiaUserRepository.countPartnerBindLeJiaUser(id);
  }

  public long countPartnerManagerBindLeJiaUser(Long id) {
    return leJiaUserRepository.countPartnerManagerBindLeJiaUser(id);
  }

  /**
   * 今日红包积分 收益
   *
   * @param token 用户token
   * @param today 今日
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Object[]> todayScoreAB(String token, String today) {
    String sql = null;

    sql = " SELECT today_a.tsa, today_b.tsb FROM "
          + " (SELECT SUM(sad.number) tsa, u.user_sid token FROM le_jia_user u INNER JOIN scorea sa ON u.id=sa.le_jia_user_id "
          + " INNER JOIN scorea_detail sad ON sa.id=sad.scorea_id "
          + " WHERE u.user_sid='" + token + "'"
          + " AND DATE(sad.date_created)='" + today + "' AND  sad.number>0) today_a, "
          + " (SELECT SUM(sbd.number) tsb, u.user_sid token FROM le_jia_user u INNER JOIN scorec sb ON u.id=sb.le_jia_user_id "
          + " INNER JOIN scorec_detail sbd ON sb.id=sbd.scorec_id "
          + " WHERE u.user_sid='" + token + "'"
          + " AND DATE(sbd.date_created)='" + today + "' AND sbd.number>0) today_b ";

    Query query = em.createNativeQuery(sql);
    return query.getResultList();
  }

  /**
   * 昨日红包积分 收益
   *
   * @param token     用户token
   * @param yesterday 昨日
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Object[]> yesterdayScoreAB(String token, String yesterday) {
    String sql = null;
//    sql = " SELECT SUM(sad.number), SUM(sbd.number) FROM le_jia_user u INNER JOIN scorea sa ON u.id=sa.le_jia_user_id INNER JOIN scoreb sb ON u.id=sb.le_jia_user_id "
//        + " INNER JOIN scorea_detail sad ON sa.id=sad.scorea_id INNER JOIN scoreb_detail sbd ON sb.id=sbd.scoreb_id "
//        + " WHERE sad.number>0 AND sbd.number>0 AND u.token='" + token + "'"
//        + " AND DATE(sad.date_created)='" + yesterday + "' AND DATE(sbd.date_created)='" + yesterday + "'";

    sql = " SELECT yesterday_a.ysa, yesterday_b.ysb FROM "
          + " (SELECT SUM(sad.number) ysa, u.user_sid token FROM le_jia_user u INNER JOIN scorea sa ON u.id=sa.le_jia_user_id "
          + " INNER JOIN scorea_detail sad ON sa.id=sad.scorea_id "
          + " WHERE u.user_sid='" + token + "'"
          + " AND DATE(sad.date_created)='" + yesterday + "' AND sad.number>0) yesterday_a, "
          + " (SELECT SUM(sbd.number) ysb, u.user_sid token FROM le_jia_user u INNER JOIN scorec sb ON u.id=sb.le_jia_user_id "
          + " INNER JOIN scorec_detail sbd ON sb.id=sbd.scorec_id "
          + " WHERE u.user_sid='" + token + "'"
          + " AND DATE(sbd.date_created)='" + yesterday + "' AND sbd.number>0) yesterday_b ";
//          + " WHERE yesterday_a.token = yesterday_b.token";

    Query query = em.createNativeQuery(sql);
    List<Object[]> list = query.getResultList();
    return list;
  }
}
