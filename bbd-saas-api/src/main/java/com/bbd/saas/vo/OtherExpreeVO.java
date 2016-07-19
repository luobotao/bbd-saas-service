package com.bbd.saas.vo;

import org.mongodb.morphia.annotations.Embedded;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huozhijie on 2016/5/31.
 */
public class OtherExpreeVO implements Serializable {
    private Integer id;
    private String context;/*内容*/
    private String companyname;
    private String companycode;
    private String mailNum;//运单号
    private Date dateUpd;
    private String status;/*本数据元对应的签收状态。只有在开通签收状态服务
        且在订阅接口中提交resultv2标记后才会出现*/

    private String   areaCode;/*本数据元对应的行政区域的编码，只有在开通签收状态服务
       且在订阅接口中提交resultv2标记后才会出现*/

    private String  areaName;/*本数据元对应的行政区域的名称，开通签收状态服务
         且在订阅接口中提交resultv2标记后才会出现*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public String getMailNum() {
        return mailNum;
    }

    public void setMailNum(String mailNum) {
        this.mailNum = mailNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }
}
