package com.jifenke.lepluslive.partner.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Created by xf on 2017/4/28.
 * 合伙人管理员线上钱包
 */
@Entity
@Table(name = "PARTNER_MANAGER_WALLET_ONLINE")
public class PartnerManagerWalletOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long availableBalance = 0L; //可用佣金余额

    private Long totalMoney = 0L; //获得佣金总额

    private Long totalWithdrawals = 0L;//已经提现总额

    private Date createDate = new Date();

    private Date lastUpdate;  //最后更新时间

    @OneToOne
    private PartnerManager partnerManager;

    @Version
    private Long version = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Long availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Long getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(Long totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public PartnerManager getPartnerManager() {
        return partnerManager;
    }

    public void setPartnerManager(PartnerManager partnerManager) {
        this.partnerManager = partnerManager;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
