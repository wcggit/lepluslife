package com.jifenke.lepluslive.Address.repository;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wcg on 16/3/21.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

  Address findByLeJiaUser(LeJiaUser leJiaUser);

  Address findByLeJiaUserAndState(LeJiaUser leJiaUser, Integer state);

  List<Address> findAllAddressByLeJiaUser(LeJiaUser leJiaUser);
}
