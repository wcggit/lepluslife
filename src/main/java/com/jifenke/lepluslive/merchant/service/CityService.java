package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Area;
import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.repository.AreaRepository;
import com.jifenke.lepluslive.merchant.repository.CityRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class CityService {

  @Inject
  private CityRepository cityRepository;

  @Inject
  private AreaRepository areaRepository;

  /**
   * 获取所有的城市列表  2016/12/21
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<City> findAllCity() {
    return cityRepository.findAll();
  }

  /**
   * 获取热门城市列表  2016/12/21
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<City> findHotCity() {
    return cityRepository.findByHotOrderBySidDesc(1);
  }

  public City findCityById(Long id) {

    City city = cityRepository.findOne(id);

    return city;
  }

  public City findCityByName(String name) {

    List<City> cityList = cityRepository.findByName(name);
    if (cityList != null && cityList.size() > 0) {
      return cityList.get(0);
    }
    return null;
  }

  /**
   * 获取某个城市的所有地区列表
   *
   * @param id 城市id
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Area> findAreaListByCity(Long id) {
    City city = new City();
    city.setId(id);
    return areaRepository.findAllByCity(city);
  }
}
