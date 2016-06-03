package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huozhijie on 2016/6/2.
 */
public class Balance implements Serializable {
    private Integer id;
    private Integer uId;
    private String phone;
    private Integer balance;
    private Integer canuse;
    private Integer withdraw;
    private String remark;
    private Date dateNew;
    private Date dateUpd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getCanuse() {
        return canuse;
    }

    public void setCanuse(Integer canuse) {
        this.canuse = canuse;
    }

    public Integer getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Integer withdraw) {
        this.withdraw = withdraw;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDateNew() {
        return dateNew;
    }

    public void setDateNew(Date dateNew) {
        this.dateNew = dateNew;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }
}
