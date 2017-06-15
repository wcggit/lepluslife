package com.jifenke.lepluslive.partner.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 合伙人管理员线上分润钱包变动明细
 * Created by xf on 17/04/26.
 */
@Entity
@Table(name = "PARTNER_MANAGER_WALLET_ONLINE_LOG")
public class PartnerManagerWalletOnlineLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderSid;

    private Long partnerManagerId;

    private Long beforeChangeMoney; //商户钱包改变前金额

    private Long afterChangeMoney; //改变后的金额

    private Long changeMoney;   //线上钱包改变金额 理论=beforeChangeMoney-afterChangeMoney

    private Long type; //如果为1代表app线上订单分润  2代表公众号线上订单分润

    private Date createDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public Long getPartnerManagerId() {
        return partnerManagerId;
    }

    public void setPartnerManagerId(Long partnerManagerId) {
        this.partnerManagerId = partnerManagerId;
    }

    public Long getBeforeChangeMoney() {
        return beforeChangeMoney;
    }

    public void setBeforeChangeMoney(Long beforeChangeMoney) {
        this.beforeChangeMoney = beforeChangeMoney;
    }

    public Long getAfterChangeMoney() {
        return afterChangeMoney;
    }

    public void setAfterChangeMoney(Long afterChangeMoney) {
        this.afterChangeMoney = afterChangeMoney;
    }

    public Long getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(Long changeMoney) {
        this.changeMoney = changeMoney;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
