package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.groupon.service.GrouponOrderService;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnline;
import com.jifenke.lepluslive.merchant.service.MerchantScanPayWayService;
import com.jifenke.lepluslive.merchant.service.MerchantWalletOnlineService;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrderShare;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.order.repository.OnLineOrderShareRepository;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWalletOnline;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import com.jifenke.lepluslive.partner.service.PartnerManagerWalletOnlineService;
import com.jifenke.lepluslive.partner.service.PartnerWalletOnlineService;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieOrder;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieProduct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * 线上订单分润单 Created by zhangwen on 16/11/05.
 */
@Service
public class OnLineOrderShareService {

  @Inject
  private OnLineOrderShareRepository orderShareRepository;

  @Inject
  private MerchantWalletOnlineService merchantWalletOnlineService;

  @Inject
  private PartnerWalletOnlineService partnerWalletOnlineService;

  @Inject
  private PartnerManagerWalletOnlineService partnerManagerOnlineService;

  @Inject
  private OrderService onlineOrderService;

  @Inject
  private MerchantScanPayWayService merchantScanPayWayService;

  @Inject
  private GrouponOrderService grouponOrderService;

  /**
   * 线上订单分润    16/11/05
   *
   * @param orderId 线上订单ID
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void onLineOrderShare(Long orderId) {

    OnLineOrder order = onlineOrderService.findOnLineOrderById(orderId);

    List<OrderDetail> list = order.getOrderDetails();
    Long toMerchant = 0L;
    Merchant merchant = null;
    MerchantWalletOnline merchantWalletOnline = null;
    Long toPartner = 0L;
    Partner partner = null;
    PartnerWalletOnline partnerWalletOnline = null;
    Long toLePlusLife = 0L;
    long type = 16001; //1代表app线上订单分润  2代表公众号线上订单分润
    PayOrigin payOrigin = order.getPayOrigin();
    if (payOrigin.getPayFrom() == 1) { //app
      type = 16006;
    }
    LeJiaUser user = order.getLeJiaUser();
    for (OrderDetail detail : list) {
      ProductSpec spec = detail.getProductSpec();
      toMerchant += spec.getToMerchant() == null ? 0 : spec.getToMerchant();
      toPartner += spec.getToPartner() == null ? 0 : spec.getToPartner();
    }
    //添加分润记录
    if (toMerchant + toPartner > 0) {
      OnLineOrderShare orderShare = new OnLineOrderShare();
      orderShare.setType(1);
      orderShare.setOrderSid(order.getOrderSid());

      if (user.getBindMerchant() != null && toMerchant > 0) {
        merchant = user.getBindMerchant();
        //分润给绑定商户
        orderShare.setToLockMerchant(toMerchant);
        if (merchant == null || merchant.getPartnership() == 2) {//如果是虚拟商户分润方式改变
          toPartner += toMerchant;
        } else {
          MerchantScanPayWay
              scanPayWay =
              merchantScanPayWayService.findByMerchantId(merchant.getId());
          if (scanPayWay != null && scanPayWay.getOpenOnLineShare() == 0) {
            //没开启线上分润
            toLePlusLife = toMerchant;
            toMerchant = 0L;
          } else {
            merchantWalletOnline =
                merchantWalletOnlineService.findByMercahnt(user.getBindMerchant());
            merchantWalletOnlineService.shareToMerchant(toMerchant, merchant, merchantWalletOnline,
                                                        order.getOrderSid(), type);
            orderShare.setLockMerchant(merchant);
          }
        }
      }
      //对用户绑定合伙人进行分润
      if (user.getBindPartner() != null && toPartner > 0) {
        partnerWalletOnline = partnerWalletOnlineService.findByPartner(user.getBindPartner());
        partner = partnerWalletOnline.getPartner();

        orderShare.setToLockPartner(toPartner);
        //分润给绑定合伙人
        partnerWalletOnlineService
            .shareToPartner(toPartner, partner, partnerWalletOnline, order.getOrderSid(),
                            type);
        orderShare.setLockPartner(partner);
      }
      orderShare.setToLePlusLife(toLePlusLife);
      orderShare.setShareMoney(toMerchant + toPartner + toLePlusLife);
      orderShareRepository.save(orderShare);
    }
  }

  /**
   * 电影院订单分润    17/04/28
   *
   * @param movieOrder 电影票订单
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void sMovieOrderShare(SMovieOrder movieOrder) {
    LeJiaUser leJiaUser = movieOrder.getLeJiaUser();
    SMovieProduct product = movieOrder.getsMovieProduct();
    OnLineOrderShare onLineOrderShare = new OnLineOrderShare();
    onLineOrderShare.setCreateDate(new Date());
    onLineOrderShare.setOrderSid(movieOrder.getOrderSid());
    onLineOrderShare
        .setToLockMerchant(product.getToMerchant() == null ? 0 : product.getToMerchant());
    onLineOrderShare.setToLockPartner(product.getToPartner() == null ? 0 : product.getToPartner());
    onLineOrderShare.setToLockPartnerManager(
        product.getToPartnerManager() == null ? 0 : product.getToPartnerManager());
    onLineOrderShare.setType(2);                                        //分润单来源   1=商城|2=电影票
    //添加分润记录
    if (onLineOrderShare.getShareMoney() > 0) {
      Long toPartner = product.getToPartner();

      if (leJiaUser.getBindMerchant() != null && product.getToMerchant() > 0) {
        Merchant merchant = leJiaUser.getBindMerchant();
        //分润给绑定商户
        onLineOrderShare.setLockMerchant(leJiaUser.getBindMerchant());
        if (merchant.getPartnership() == 2) {     //如果是虚拟商户分润方式改变
          toPartner += onLineOrderShare.getToLockMerchant();
          onLineOrderShare.setToLockMerchant(0L);
        } else {
          MerchantWalletOnline
              merchantWalletOnline =
              merchantWalletOnlineService.findByMercahnt(merchant);
          merchantWalletOnlineService
              .shareToMerchant(product.getToMerchant(), merchant, merchantWalletOnline,
                               movieOrder.getOrderSid(), 2L);    //type 1代表app线上订单分润  2代表公众号线上订单分润
          onLineOrderShare.setLockMerchant(leJiaUser.getBindMerchant());
        }
      }
      // 对用户绑定合伙人进行分润
      Partner bindPartner = leJiaUser.getBindPartner();
      if (bindPartner != null && toPartner > 0) {
        onLineOrderShare.setLockPartner(bindPartner);
        PartnerWalletOnline
            partnerWalletOnline =
            partnerWalletOnlineService.findByPartner(bindPartner);
        //分润给绑定合伙人
        partnerWalletOnlineService
            .shareToPartner(toPartner, bindPartner, partnerWalletOnline, movieOrder.getOrderSid(),
                            2L);
        onLineOrderShare.setToLockPartner(toPartner);
      }
      // 对用户绑定的合伙人管理员
      if (leJiaUser.getBindPartnerManager() != null && product.getToPartnerManager() > 0) {
        onLineOrderShare.setLockPartnerManager(leJiaUser.getBindPartnerManager());
        PartnerManagerWalletOnline
            partnerManagerWalletOnline =
            partnerManagerOnlineService.findByPartnerManager(leJiaUser.getBindPartnerManager());
        partnerManagerOnlineService
            .shareToPartnerManager(product.getToPartnerManager(), leJiaUser.getBindPartnerManager(),
                                   partnerManagerWalletOnline, movieOrder.getOrderSid(), 2L);
      }
      onLineOrderShare
          .setShareMoney(product.getToMerchant() + toPartner + product.getToPartnerManager());
      orderShareRepository.save(onLineOrderShare);
    }

  }

//  /**
//   * 团购订单分润  核销时分润   17/06/22
//   *
//   * @param grouponOrderId 团购订单ID
//   */
//  @Transactional(propagation = Propagation.REQUIRED)
//  public void grouponOrderShare(Long grouponOrderId) {
//    GrouponOrder order = grouponOrderService.findById(grouponOrderId);
//    LeJiaUser leJiaUser = order.getLeJiaUser();
//    GrouponCode code = order.getGrouponCodes().get(0);
//    int buyNum = order.getBuyNum();
//    long type = 1;
//    if (order.getPayOrigin() == 0) {
//      type = 2;
//    }
//    String orderSid = order.getOrderSid();
//    OnLineOrderShare onLineOrderShare = new OnLineOrderShare();
//    Date date = new Date();
//    onLineOrderShare.setCreateDate(date);
//    onLineOrderShare.setType(3);
//    onLineOrderShare.setOrderSid(orderSid);
//
//    onLineOrderShare.setShareMoney(code.getCommission() * buyNum);
//    onLineOrderShare
//        .setShareMoney((code.getShareToLockMerchant() + code.getShareToLockPartner() + code
//            .getShareToLockPartnerManager() + code.getShareToTradePartner() + code
//                            .getShareToTradePartnerManager()) * buyNum);
//    onLineOrderShare.setToLockMerchant(code.getShareToLockMerchant() * buyNum);
//    onLineOrderShare.setToLockPartner(code.getShareToLockPartner() * buyNum);
//    onLineOrderShare.setToLockPartnerManager(code.getShareToLockPartnerManager() * buyNum);
//    onLineOrderShare.setToTradePartner(code.getShareToTradePartner() * buyNum);
//    onLineOrderShare.setToTradePartnerManager(code.getShareToTradePartnerManager() * buyNum);
//    onLineOrderShare
//        .setToLePlusLife(code.getCommission() * buyNum - onLineOrderShare.getShareMoney());
//    //添加分润记录
//    if (onLineOrderShare.getShareMoney() > 0) {
//
//      if (leJiaUser.getBindMerchant() != null && code.getShareToLockMerchant() > 0) {
//        Merchant merchant = leJiaUser.getBindMerchant();
//        //分润给绑定商户
//        onLineOrderShare.setLockMerchant(merchant);
//        if (merchant.getPartnership() == 2) {     //如果是虚拟商户分润方式改变
//          onLineOrderShare.setToLockPartner(
//              onLineOrderShare.getToLockPartner() + onLineOrderShare.getToLockMerchant());
//          onLineOrderShare.setToLockMerchant(0L);
//        } else {
//          MerchantWalletOnline
//              merchantWalletOnline =
//              merchantWalletOnlineService.findByMercahnt(merchant);
//          merchantWalletOnlineService
//              .shareToMerchant(onLineOrderShare.getToLockMerchant(), merchant, merchantWalletOnline,
//                               orderSid, type);
//          onLineOrderShare.setLockMerchant(merchant);
//        }
//      }
//
//      // 对用户绑定合伙人进行分润
//      Partner bindPartner = leJiaUser.getBindPartner();
//      if (bindPartner != null && code.getShareToLockPartner() > 0) {
//        onLineOrderShare.setLockPartner(bindPartner);
//        PartnerWalletOnline
//            partnerWalletOnline =
//            partnerWalletOnlineService.findByPartner(bindPartner);
//        //分润给绑定合伙人
//        partnerWalletOnlineService
//            .shareToPartner(onLineOrderShare.getToLockPartner(), bindPartner, partnerWalletOnline,
//                            orderSid,
//                            type);
//      }
//
//      // 对用户绑定的合伙人管理员
//      PartnerManager bindPartnerManager = leJiaUser.getBindPartnerManager();
//      if (bindPartnerManager != null && code.getShareToLockPartnerManager() > 0) {
//        onLineOrderShare.setLockPartnerManager(bindPartnerManager);
//        PartnerManagerWalletOnline
//            partnerManagerWalletOnline =
//            partnerManagerOnlineService.findByPartnerManager(bindPartnerManager);
//        partnerManagerOnlineService
//            .shareToPartnerManager(onLineOrderShare.getToLockPartnerManager(), bindPartnerManager,
//                                   partnerManagerWalletOnline, orderSid, type);
//      }
//
//      //TODO:分润给交易商家和合伙人
//
//      orderShareRepository.save(onLineOrderShare);
//    }
//  }
}
