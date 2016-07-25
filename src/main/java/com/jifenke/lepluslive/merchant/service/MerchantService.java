package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.controller.dto.MerchantDto;
import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantDetail;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantInfo;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScroll;
import com.jifenke.lepluslive.merchant.repository.MerchantDetailRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantInfoRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantScrollRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class MerchantService {

  @Inject
  private MerchantRepository merchantRepository;

  @Inject
  private MerchantDetailRepository merchantDetailRepository;

  @Inject
  private EntityManagerFactory entityManagerFactory;

  @Inject
  MerchantInfoRepository merchantInfoRepository;

  @Inject
  private CityService cityService;

  @Inject
  private MerchantScrollRepository merchantScrollRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Merchant> findMerchantsByPage(Integer offset) {
    if (offset == null) {
      offset = 1;
    }
    return merchantRepository.findAll(
        new PageRequest(offset - 1, 10, new Sort(Sort.Direction.ASC, "sid"))).getContent();
  }

  /**
   * 获取商家详情
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Merchant findMerchantById(Long id) {
    return merchantRepository.findOne(id);
  }

  /**
   * 获取商家轮播图
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<MerchantDetail> findAllMerchantDetailByMerchant(Merchant merchant) {
    return merchantDetailRepository.findAllByMerchant(merchant);
  }

  /**
   * 按照距离远近对商家排序  以后可以被findMerchantListByCustomCondition取代 open app 暂时使用
   *
   * @param latitude  经度
   * @param longitude 纬度
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<MerchantDto> findOrderByDistance(Double latitude, Double longitude) {

    List<MerchantDto> dtoList = new ArrayList<>();
    List<Object[]>
        list =
        merchantRepository.findOrderByDistance(latitude, longitude, 0, 10);
    for (Object[] o : list) {
      MerchantDto merchantDto = new MerchantDto();
      merchantDto.setId(Long.parseLong(o[0].toString()));
      merchantDto.setSid(Integer.parseInt(o[1].toString()));
      merchantDto.setLocation(o[2].toString());
      merchantDto.setPhoneNumber(o[3].toString());
      merchantDto.setName(o[4].toString());
      merchantDto.setPicture(o[5].toString());
//      merchantDto.setDiscount(o[6] != null ? Integer.parseInt(o[6].toString()) : 10);
//      merchantDto.setRebate(o[7] != null ? Integer.parseInt(o[7].toString()) : 0);
      merchantDto.setLng(Double.parseDouble(o[6].toString()));
      merchantDto.setLat(Double.parseDouble(o[7].toString()));
      merchantDto.setDistance(o[8] != null ? Double.valueOf(o[8].toString()) : null);
      dtoList.add(merchantDto);
    }
    return dtoList;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<MerchantDto> findMerchantListByCustomCondition(Double latitude, Double longitude,
                                                             Integer page, Long type, Long cityId,
                                                             Long areaId) {
    if (page == null) {
      page = 1;
    }

    EntityManager em = entityManagerFactory.createEntityManager();
    //定义SQL
    String sql = null;

    sql =
        "SELECT m.id,m.sid,m.location,m.phone_number,m.`name`,m.picture,m.lng,m.lat, ROUND( 6378.138 * 2 * ASIN(SQRT(POW(SIN(("
        + latitude + " * PI() / 180 - m.lat * PI() / 180) / 2),2) + COS(" + latitude
        + " * PI() / 180) * COS(m.lat * PI() / 180) * POW(SIN((" + longitude
        + " * PI() / 180 - m.lng * PI() / 180) / 2),2))) * 1000) AS distance FROM merchant m WHERE 1=1";

    if (type != null) {
      sql += " AND m.merchant_type_id = " + type;
    }

    if (areaId != null) {
      sql += " AND m.area_id = " + areaId;
      sql += " ORDER BY sid LIMIT " + (page - 1) * 10 + "," + 10 + "";
    } else if (cityId != null) {
      sql += " AND m.city_id = " + cityId;
      sql += " ORDER BY sid LIMIT " + (page - 1) * 10 + "," + 10 + "";
    } else if (latitude != null) {
      sql += " ORDER BY distance LIMIT " + (page - 1) * 10 + "," + 10 + "";
    } else {
      sql += " ORDER BY sid LIMIT " + (page - 1) * 10 + "," + 10 + "";
    }

    //创建原生SQL查询QUERY实例
    Query query = em.createNativeQuery(sql);

    List<Object[]> list = query.getResultList();

    em.close();
    entityManagerFactory.close();

    List<MerchantDto> dtoList = new ArrayList<>();
    for (Object[] o : list) {
      MerchantDto merchantDto = new MerchantDto();
      merchantDto.setId(Long.parseLong(o[0].toString()));
      merchantDto.setSid(Integer.parseInt(o[1].toString()));
      merchantDto.setLocation(o[2].toString());
      merchantDto.setPhoneNumber(o[3].toString());
      merchantDto.setName(o[4].toString());
      merchantDto.setPicture(o[5].toString());
//      merchantDto.setDiscount(o[6] != null ? Integer.parseInt(o[6].toString()) : 10);
//      merchantDto.setRebate(o[7] != null ? Integer.parseInt(o[7].toString()) : 0);
      merchantDto.setLng(Double.parseDouble(o[6].toString()));
      merchantDto.setLat(Double.parseDouble(o[7].toString()));
      merchantDto.setDistance(o[8] != null ? Double.valueOf(o[8].toString()) : null);
      dtoList.add(merchantDto);
    }
    return dtoList;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<MerchantDto> findWxMerchantListByCustomCondition(Integer status, Double latitude,
                                                               Double longitude,
                                                               Integer page, Long type,
                                                               String cityName, Integer condition) {

    EntityManager em = entityManagerFactory.createEntityManager();
    //定义SQL
    String sql = null;
    if (status == 1) {
      sql =
          "SELECT m.id,m.sid,m.location,m.phone_number,m.`name`,m.picture,m.lng,m.lat,t.`name` AS tName,ci.`name` AS cName,mi.star,area.`name` AS aName,ROUND( 6378.138 * 2 * ASIN(SQRT(POW(SIN(("
          + latitude + " * PI() / 180 - m.lat * PI() / 180) / 2),2) + COS(" + latitude
          + " * PI() / 180) * COS(m.lat * PI() / 180) * POW(SIN((" + longitude
          + " * PI() / 180 - m.lng * PI() / 180) / 2),2))) * 1000) AS distance FROM merchant m,merchant_type t,city ci,merchant_info mi,area WHERE m.merchant_type_id = t.id AND m.city_id = ci.id AND m.merchant_info_id=mi.id AND m.area_id=area.id";
    } else {
      sql =
          "SELECT m.id,m.sid,m.location,m.phone_number,m.`name`,m.picture,m.lng,m.lat,t.`name` AS tName,ci.`name` AS cName,mi.star,area.`name` AS aName FROM merchant m,merchant_type t,city ci,merchant_info mi,area WHERE m.merchant_type_id = t.id AND m.city_id = ci.id AND m.merchant_info_id=mi.id AND m.area_id=area.id";
    }

    sql += " AND m.state = 1";

    if (type != null) {
      sql += " AND m.merchant_type_id = " + type;
    }

    if (cityName != null && cityName.length() > 1) {
      cityName = cityName.substring(0, cityName.length() - 1);
      City city = cityService.findCityByName(cityName);
      if (city != null) {
        sql += " AND m.city_id = " + city.getId();
      }
    }

    if (condition != null) {
      if (condition == 2) {  //送红包最多
        sql += " ORDER BY m.lj_commission DESC";
      } else if (condition == 3) { //评价最高
        sql += " ORDER BY mi.star DESC";
      }
    }

    if (status == 1 && condition != null && condition != 0) { //离我最近
      sql += " ,distance ASC ";
    }

    sql += " LIMIT " + (page - 1) * 10 + "," + 10;

    Query query = em.createNativeQuery(sql);
    List<Object[]> list = query.getResultList();

    em.close();
    entityManagerFactory.close();

    List<MerchantDto> dtoList = new ArrayList<>();
    for (Object[] o : list) {
      MerchantDto merchantDto = new MerchantDto();
      merchantDto.setId(Long.parseLong(o[0].toString()));
      merchantDto.setSid(Integer.parseInt(String.valueOf(o[1])));
      merchantDto.setLocation(String.valueOf(o[2]));
      merchantDto.setPhoneNumber(String.valueOf(o[3]));
      merchantDto.setName(String.valueOf(o[4]));
      merchantDto.setPicture(o[5] != null ? String.valueOf(o[5]) : null);
//      merchantDto.setDiscount(o[6] != null ? Integer.parseInt(o[6].toString()) : 10);
//      merchantDto.setRebate(o[7] != null ? Integer.parseInt(o[7].toString()) : 0);
      merchantDto.setLng(Double.parseDouble(String.valueOf(o[6])));
      merchantDto.setLat(Double.parseDouble(String.valueOf(o[7])));
      merchantDto.setTypeName(String.valueOf(o[8]));
      merchantDto.setCityName(String.valueOf(o[9]));
      merchantDto.setStar(new BigDecimal(String.valueOf(o[10])));
      merchantDto.setAreaName(String.valueOf(o[11]));
      if (o.length > 12) {
        merchantDto.setDistance(o[12] != null ? Double.valueOf(o[12].toString()) : null);
      }
      dtoList.add(merchantDto);
    }
    return dtoList;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void test(Merchant merchant) {
    MerchantInfo info = new MerchantInfo();
    merchantInfoRepository.save(info);
    merchant.setMerchantInfo(info);
    merchantRepository.save(merchant);
  }

  public List<MerchantScroll> findAllScorllPicture(Merchant merchant) {
    return merchantScrollRepository.findAllByMerchant(merchant);
  }

}
