package com.jifenke.lepluslive.Address.repository;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wcg on 16/3/21.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findByLeJiaUserAndStateNot(LeJiaUser leJiaUser,Integer state);

  Address findByLeJiaUserAndState(LeJiaUser leJiaUser, Integer state);

  List<Address> findAllAddressByLeJiaUserAndStateNot(LeJiaUser leJiaUser,Integer state);
}
