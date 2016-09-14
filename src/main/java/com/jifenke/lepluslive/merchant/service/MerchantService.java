package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.controller.dto.MerchantDto;
import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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
  private EntityManager em;

  @Inject
  private MerchantInfoRepository merchantInfoRepository;

  @Inject
  private CityService cityService;

  @Inject
  private MerchantDetailRepository merchantDetailRepository;

  @Inject
  private MerchantScrollRepository merchantScrollRepository;

  /**
   * 获取商家详情
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Merchant findMerchantById(Long id) {
    return merchantRepository.findOne(id);
  }

  /**
   * APP获取商家详情  06/09/02
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Map findMerchant(Long id) {
    Map<String, Object> map = new HashMap<>();
    Merchant merchant = new Merchant();
    merchant.setId(id);
    List<Object[]> list = merchantRepository.findMerchantById(id);
    for (Object[] o : list) {
      map.put("id", o[0]);
      map.put("name", o[1]);
      map.put("location", o[2]);
      map.put("phoneNumber", o[3]);
      map.put("lng", o[4]); //经
      map.put("lat", o[5]);
      map.put("typeName", o[6]);
      map.put("areaName", o[7]);
      map.put("star", o[8]);
      map.put("card", o[9]);
      map.put("park", o[10]);
      map.put("perSale", o[11]);
      map.put("wifi", o[12]);
    }
    //商家轮播图
    List<Map> scrollList = new ArrayList<>();
    List<Object[]> list2 = merchantScrollRepository.findMerchantScrollsByMerchantId(id);
    for (Object[] o : list2) {
      Map<String, Object> m = new HashMap<>();
      m.put("sid", o[0]);
      m.put("picture", o[1]);
      scrollList.add(m);
    }
    map.put("scrolls", scrollList);
    //商家详情图
    List<Map> detailList = new ArrayList<>();
    List<Object[]> list3 = merchantDetailRepository.findMerchantDetailsByMerchantId(id);
    for (Object[] o : list3) {
      Map<String, Object> m = new HashMap<>();
      m.put("sid", o[0]);
      m.put("picture", o[1]);
      detailList.add(m);
    }
    map.put("details", detailList);
    return map;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findMerchantListByCustomCondition(Integer status, Double latitude,
                                                     Double longitude,
                                                     Integer page, Long type, Long cityId,
                                                     Integer condition, String value) {
    String sql = null;
    if (status == 1) {
      sql =
          "SELECT m.id,m.sid,m.location,m.`name`,m.picture,t.`name` AS tName,mi.star,area.`name` AS aName,m.lj_commission,m.scorearebate,m.partnership,ROUND( 6378.138 * 2 * ASIN(SQRT(POW(SIN(("
          + latitude + " * PI() / 180 - m.lat * PI() / 180) / 2),2) + COS(" + latitude
          + " * PI() / 180) * COS(m.lat * PI() / 180) * POW(SIN((" + longitude
          + " * PI() / 180 - m.lng * PI() / 180) / 2),2))) * 1000) AS distance FROM merchant m,merchant_type t,city ci,merchant_info mi,area WHERE m.merchant_type_id = t.id AND m.city_id = ci.id AND m.merchant_info_id=mi.id AND m.area_id=area.id";
    } else {
      sql =
          "SELECT m.id,m.sid,m.location,m.`name`,m.picture,t.`name` AS tName,mi.star,area.`name` AS aName,m.lj_commission,m.scorearebate,m.partnership FROM merchant m,merchant_type t,city ci,merchant_info mi,area WHERE m.merchant_type_id = t.id AND m.city_id = ci.id AND m.merchant_info_id=mi.id AND m.area_id=area.id";
    }

    sql += " AND m.state = 1";

    if (type != null) {
      sql += " AND m.merchant_type_id = " + type;
    }

    if (cityId != null) {
      sql += " AND m.city_id = " + cityId;
    }

    if (value != null && (!"".equals(value))) {
      sql += " AND (m.`name` LIKE '%" + value + "%' OR location LIKE '%" + value + "%') ";
    }

    if (condition != null) {
      if (condition == 2) {  //送红包最多
        sql += " ORDER BY m.lj_commission DESC";
      } else if (condition == 3) { //评价最高
        sql += " ORDER BY mi.star DESC";
      }
    }

    if (status == 1) {
      if (condition != null) {
        if (condition != 0) {  //先其他 后离我最近
          sql += " ,distance ASC ";
        } else {                //离我最近
          sql += " ORDER BY distance ASC ";
        }
      } else {
        sql += " ORDER BY distance ASC ";
      }
    }
    if (page < 1) {
      page = 1;
    }
    sql += " LIMIT " + (page - 1) * 10 + "," + 10;

    Query query = em.createNativeQuery(sql);
    List<Object[]> list = query.getResultList();

    List<Map> mapList = new ArrayList<>();
    for (Object[] o : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("id", o[0]);
      map.put("sid", o[1]);
      map.put("location", o[2]);
      map.put("name", o[3]);
      map.put("picture", o[4]);
      map.put("typeName", o[5]);
      map.put("star", o[6]);
      map.put("area", o[7]);
      map.put("commission", o[8]);
      map.put("aRebate", o[9]);
      map.put("friend", o[10]);
      if (o.length > 11) {
        map.put("distance", o[11]);
      } else {
        map.put("distance", 0);
      }
      mapList.add(map);
    }
    return mapList;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<MerchantDto> findWxMerchantListByCustomCondition(Integer status, Double latitude,
                                                               Double longitude,
                                                               Integer page, Long type,
                                                               String cityName, Integer condition,
                                                               Integer partnership) {

//    EntityManager em = entityManagerFactory.createEntityManager();
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

    if (null != partnership && 1 == partnership) {
      sql += " AND m.partnership = 1 ";
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

    if (status == 1) {
      if (condition != null) {
        if (condition != 0) {  //先其他 后离我最近
          sql += " ,distance ASC ";
        } else {                //离我最近
          sql += " ORDER BY distance ASC ";
        }
      } else {
        sql += " ORDER BY distance ASC ";
      }
    }

    sql += " LIMIT " + (page - 1) * 10 + "," + 10;

    Query query = em.createNativeQuery(sql);
    List<Object[]> list = query.getResultList();

//    em.close();
//    entityManagerFactory.close();

    List<MerchantDto> dtoList = new ArrayList<>();
    for (Object[] o : list) {
      MerchantDto merchantDto = new MerchantDto();
      merchantDto.setId(Long.parseLong(o[0].toString()));
      merchantDto.setSid(Integer.parseInt(String.valueOf(o[1])));
      merchantDto.setLocation(String.valueOf(o[2]));
      merchantDto.setPhoneNumber(String.valueOf(o[3]));
      merchantDto.setName(String.valueOf(o[4]));
      merchantDto.setPicture(o[5] != null ? String.valueOf(o[5]) : null);
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

  /**
   * 获取商家轮播图
   */
  public List<MerchantScroll> findAllScorllPicture(Merchant merchant) {
    return merchantScrollRepository.findAllByMerchant(merchant);
  }

  /**
   * 根据二维码参数获取商家信息  16/09/07
   *
   * @param parameter 二维码参数
   * @return 商家id
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Object findMerchantIdByParameter(String parameter) {
    List<Object[]> list = merchantInfoRepository.findByParameter(parameter);
    if (list.size() > 0) {
      return list.get(0)[0];
    }
    return null;
  }

  /**
   * 支付成功页获取附近商家列表 16/09/13
   *
   * @param merchant 支付商家
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findPaySuccessMerchantList(Merchant merchant) {
    String sql = null;
    int count = 5;
    List<Map> mapList = new ArrayList<>();
    if (merchant.getState() == 1 && merchant.getReceiptAuth() == 1) {
      count = 4;
      Map<String, Object> map = new HashMap<>();
      map.put("id", merchant.getId());
      map.put("sid", merchant.getSid());
      map.put("location", merchant.getLocation());
      map.put("name", merchant.getName());
      map.put("picture", merchant.getPicture());
      map.put("typeName", merchant.getMerchantType().getName());
      map.put("star", merchant.getMerchantInfo().getStar());
      map.put("area", merchant.getArea().getName());
      map.put("commission", merchant.getLjCommission());
      map.put("aRebate", merchant.getScoreARebate());
      map.put("friend", merchant.getPartnership());
      map.put("perSale", merchant.getMerchantInfo().getPerSale());
      map.put("distance", 0);
      mapList.add(map);
    }
    if (merchant.getLat() != null && merchant.getLat() != 0) {
      sql =
          "SELECT m.id,m.sid,m.location,m.`name`,m.picture,t.`name` AS tName,mi.star,area.`name` AS aName,m.lj_commission,m.scorearebate,m.partnership,mi.per_sale,ROUND( 6378.138 * 2 * ASIN(SQRT(POW(SIN(("
          + merchant.getLat() + " * PI() / 180 - m.lat * PI() / 180) / 2),2) + COS(" + merchant
              .getLat()
          + " * PI() / 180) * COS(m.lat * PI() / 180) * POW(SIN((" + merchant.getLng()
          + " * PI() / 180 - m.lng * PI() / 180) / 2),2))) * 1000) AS distance FROM merchant m,merchant_type t,city ci,merchant_info mi,area WHERE m.merchant_type_id = t.id AND m.city_id = ci.id AND m.merchant_info_id=mi.id AND m.area_id=area.id";
      sql += " AND m.state = 1 AND m.receipt_auth = 1 ORDER BY distance ASC ";
    } else { //根据区域查询
      sql =
          "SELECT m.id,m.sid,m.location,m.`name`,m.picture,t.`name` AS tName,mi.star,area.`name` AS aName,m.lj_commission,m.scorearebate,m.partnership,mi.per_sale FROM merchant m,merchant_type t,city ci,merchant_info mi,area WHERE m.merchant_type_id = t.id AND m.city_id = ci.id AND m.merchant_info_id=mi.id AND m.area_id=area.id AND m.area_id="
          + merchant.getArea().getId();
      sql += " AND m.state = 1 AND m.receipt_auth = 1 ORDER BY mi.star DESC ";
    }
    sql += " LIMIT 0," + count;
    Query query = em.createNativeQuery(sql);
    List<Object[]> list = query.getResultList();
    if (list.size() < count) {
      count = count - list.size();
      sql =
          "SELECT m.id,m.sid,m.location,m.`name`,m.picture,t.`name` AS tName,mi.star,area.`name` AS aName,m.lj_commission,m.scorearebate,m.partnership,mi.per_sale FROM merchant m,merchant_type t,city ci,merchant_info mi,area WHERE m.merchant_type_id = t.id AND m.city_id = ci.id AND m.merchant_info_id=mi.id AND m.area_id=area.id AND m.area_id !="
          + merchant.getArea().getId() + " AND m.city_id = " + merchant.getCity().getId()
          + " AND m.state = 1 AND m.receipt_auth = 1 ORDER BY mi.star DESC LIMIT 0," + count;
      Query query2 = em.createNativeQuery(sql);
      List<Object[]> list2 = query2.getResultList();
      list.addAll(list2);
    }
    for (Object[] o : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("id", o[0]);
      map.put("sid", o[1]);
      map.put("location", o[2]);
      map.put("name", o[3]);
      map.put("picture", o[4]);
      map.put("typeName", o[5]);
      map.put("star", o[6]);
      map.put("area", o[7]);
      map.put("commission", o[8]);
      map.put("aRebate", o[9]);
      map.put("friend", o[10]);
      map.put("perSale", o[11]);
      if (o.length > 12) {
        map.put("distance", o[12]);
      } else {
        map.put("distance", 0);
      }
      mapList.add(map);
    }
    return mapList;
  }

  //  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//  public List<Merchant> findMerchantsByPage(Integer offset) {
//    if (offset == null) {
//      offset = 1;
//    }
//    return merchantRepository.findAll(
//        new PageRequest(offset - 1, 10, new Sort(Sort.Direction.ASC, "sid"))).getContent();
//  }

  //  /**
//   * 按照距离远近对商家排序  以后可以被findMerchantListByCustomCondition取代 open app 暂时使用
//   *
//   * @param latitude  经度
//   * @param longitude 纬度
//   */
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//  public List<MerchantDto> findOrderByDistance(Double latitude, Double longitude) {
//
//    List<MerchantDto> dtoList = new ArrayList<>();
//    List<Object[]>
//        list =
//        merchantRepository.findOrderByDistance(latitude, longitude, 0, 10);
//    for (Object[] o : list) {
//      MerchantDto merchantDto = new MerchantDto();
//      merchantDto.setId(Long.parseLong(o[0].toString()));
//      merchantDto.setSid(Integer.parseInt(o[1].toString()));
//      merchantDto.setLocation(o[2].toString());
//      merchantDto.setPhoneNumber(o[3].toString());
//      merchantDto.setName(o[4].toString());
//      merchantDto.setPicture(o[5].toString());
//      merchantDto.setLng(Double.parseDouble(o[6].toString()));
//      merchantDto.setLat(Double.parseDouble(o[7].toString()));
//      merchantDto.setDistance(o[8] != null ? Double.valueOf(o[8].toString()) : null);
//      dtoList.add(merchantDto);
//    }
//    return dtoList;
//  }


}
