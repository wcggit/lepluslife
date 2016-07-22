package com.jifenke.lepluslive.web.rest;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.repository.AddressRepository;
import com.jifenke.lepluslive.Application;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.job.OrderJob;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantInfo;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.order.repository.OrderDetailRepository;
import com.jifenke.lepluslive.order.repository.OrderRepository;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.topic.domain.entities.Topic;
import com.jifenke.lepluslive.topic.service.TopicService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeixinPayLogService;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import java.util.Date;
import java.util.List;
import java.util.Map;


import javax.inject.Inject;


/**
 * Created by wcg on 16/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles({Constants.SPRING_PROFILE_DEVELOPMENT})
public class ttt {

  @Inject
  private AddressRepository addressRepository;

  @Inject
  private WeiXinUserRepository weiXinUserRepository;

  @Inject
  private LeJiaUserRepository leJiaUserRepository;

  @Inject
  private ScoreARepository scoreARepository;

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private OrderDetailRepository orderDetailRepository;

  @Inject
  private OrderRepository orderRepository;

  @Inject
  private WeixinPayLogService weixinPayLogService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private MerchantRepository merchantRepository;

  @Inject
  private MerchantService merchantService;

  @Test
  public void tttt() {
    List<Merchant> list = merchantRepository.findAll();
    for (Merchant merchant : list) {
      MerchantInfo info = merchant.getMerchantInfo();
      if (info == null) {
        merchantService.test(merchant);
      }
    }

  }
}

////  public static void main(String[] args) {
////    int x[][] = new int[9][9];
////    for(int i=0;i<9;i++){
////      for(int y=0;y<9;y++){
////        x[i][y]=new Random().nextInt(2);
////      }
////    }
////    Scanner input = new Scanner(System.in);
////    int a = input.nextInt();
////    int b = input.nextInt();
////    int n = input.nextInt();
////
////    for(int z=1;z<n;z++){
////      int m = x[a][b];
////      int a1 = x[a-1][b];
////      int a2 = x[a+1][b];
////      int a3 = x[a][b+1];
////      int a4 = x[a][b-1];
////
////
////
////    }
//
//
//
//  }


