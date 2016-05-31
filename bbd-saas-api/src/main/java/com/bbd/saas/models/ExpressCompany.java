package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huozhijie on 2016/5/30.
 */
public class ExpressCompany implements Serializable {
    private Integer id;
    private String companyname;
    private String companycode;
    private String sta;//状态 0未审核 1审核通过 2审核失败
    private Date dateNew;
    private Date dateUpd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
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
