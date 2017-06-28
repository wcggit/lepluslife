package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponRefund;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponRefundCode;
import com.jifenke.lepluslive.groupon.repository.GrouponRefundRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * 团购退款申请
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponRefundService {

  @Inject
  private GrouponRefundRepository repository;

  @Inject
  private GrouponOrderService grouponOrderService;

  @Inject
  private GrouponCodeService grouponCodeService;

  @Inject
  private GrouponRefundCodeService grouponRefundCodeService;

  /**
   * 团购单退款申请  2017/6/20
   *
   * @param grouponOrderId 团购单ID
   * @param leJiaUser      用户
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Map<String, Object> refundApply(Long grouponOrderId, LeJiaUser leJiaUser)
      throws Exception {
    Map<String, Object> result = new HashMap<>();

    GrouponOrder grouponOrder = grouponOrderService.findById(grouponOrderId);
    if (grouponOrder != null && leJiaUser != null && grouponOrder.getLeJiaUser().getId()
        .equals(leJiaUser.getId())) { //确保只能操作自己的订单
      List<GrouponCode> codeList = grouponOrder.getGrouponCodes();
      if (codeList != null && codeList.size() > 0) {
        int count = 0; //退款码数量
        List<GrouponRefundCode> refundCodeList = new ArrayList<>(); //退款码
        List<GrouponCode> grouponCodeList = new ArrayList<>(); //需退款的团购码
        GrouponRefund refund = new GrouponRefund();
        for (GrouponCode code : codeList) {
          if (code.getState() == 0) { //未使用
            count++;
            grouponCodeList.add(code);
            GrouponRefundCode refundCode = new GrouponRefundCode();
            refundCode.setGrouponCode(code);
            refundCode.setGrouponRefund(refund);
            refundCodeList.add(refundCode);
          }
        }
        //如果可退款码不为0
        if (count > 0) {
          try {
            //修改 订单状态
            grouponOrder.setOrderState(2);
            grouponOrderService.saveGrouponOrder(grouponOrder);
            //修改 团购码状态
            for (GrouponCode code : grouponCodeList) {
              code.setState(2);
              grouponCodeService.saveGrouponCode(code);
            }
            //保存退款单
            refund.setGrouponOrder(grouponOrder);
            refund.setRefundNum(count);
            repository.save(refund);
            //保存退款单对应的团购码
            for (GrouponRefundCode refundCode : refundCodeList) {
              grouponRefundCodeService.saveGrouponRefundCode(refundCode);
            }
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
          }
          result.put("status", 200);
          return result;
        } else {
          result.put("status", 11001);
          return result;
        }
      }
    }

    result.put("status", 502);
    return result;
  }

}
