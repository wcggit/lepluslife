package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.ValidateCode;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangwen on 2016/4/25.
 */
public interface ValidateCodeRepository extends JpaRepository<ValidateCode, Long> {

  ValidateCode findByPhoneNumberAndCodeAndStatus(String phoneNumber, String code, Integer status);

  //某个手机号未过期的验证码总数
  Integer countByPhoneNumberAndStatus(String phoneNumber, Integer status);

}
