package com.jifenke.lepluslive.Address.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

import java.util.List;

/**
 * Created by zhangwen on 2016/5/3.
 */
@Controller
@RequestMapping("/address")
public class AddressController {

  @Inject
  private AddressService addressService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @ApiOperation(value = "查看我的收获地址列表")
  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult list(@RequestParam(required = false) String token) {

    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);

    List<Address> addressList = addressService.findAllAddressByLeJiaUser(leJiaUser);

    return LejiaResult.build(200, "ok", addressList);
  }

  @ApiOperation(value = "新增或修改收货地址")
  @RequestMapping(value = "/edit", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult edit(@ApiParam(value = "用户身份标识token") @RequestParam(required = false) String token,
                   @ApiParam(value = "地址id(如果是新增地址则为空)") @RequestParam(required = false) Long id,
                   @ApiParam(value = "姓名") @RequestParam(required = false) String name,
                   @ApiParam(value = "详细地址") @RequestParam(required = false) String location,
                   @ApiParam(value = "电话号码") @RequestParam(required = false) String phoneNumber,
                   @ApiParam(value = "省") @RequestParam(required = false) String province,
                   @ApiParam(value = "市") @RequestParam(required = false) String city,
                   @ApiParam(value = "区") @RequestParam(required = false) String county
  ) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser == null) {
      return LejiaResult.build(206, "未找到用户");
    }
    Address
        address =
        addressService
            .editAddress(leJiaUser, name, location, phoneNumber, province, city, county, id);
    if (address == null) {
      return LejiaResult.build(301, "未找到收货地址记录");
    }
    return LejiaResult.build(200, "ok", address);
  }

  @ApiOperation(value = "修改默认收货地址")
  @RequestMapping(value = "/changeState", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult changeState(
      @ApiParam(value = "原默认地址id(如果只有一个地址则为空)") @RequestParam(required = false) Long oldId,
      @ApiParam(value = "新默认地址id") @RequestParam(required = false) Long newId) {

    return addressService.changeState(oldId, newId);
  }
}
