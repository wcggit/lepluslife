package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wcg on 16/6/8.
 */
public interface MerchantScrollRepository extends JpaRepository<MerchantScroll,Long> {

  List<MerchantScroll> findAllByMerchant(Merchant merchant);
}
