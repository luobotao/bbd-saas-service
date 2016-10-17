package com.bbd.saas.vo;

import com.bbd.saas.enums.AuditStatus;
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
    private int otherExpsAmount;//转其他快递金额
    private AuditStatus auditStatus;//转其他快递金额审核状态
    private String turnDownReason; //驳回理由
    private String status;/*本数据元对应的签收状态。只有在开通签收状态服务
        且在订阅接口中提交resultv2标记后才会出现*/

    private String   areaCode;/*本数据元对应的行政区域的编码，只有在开通签收状态服务
       且在订阅接口中提交resultv2标记后才会出现*/

    private String  areaName;/*本数据元对应的行政区域的名称，开通签收状态服务
         且在订阅接口中提交resultv2标记后才会出现*/
    private Date dateAdd;
    private Date dateUpd;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOtherExpsAmount() {
        return otherExpsAmount;
    }

    public void setOtherExpsAmount(int otherExpsAmount) {
        this.otherExpsAmount = otherExpsAmount;
    }

    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
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

    public String getTurnDownReason() {
        return turnDownReason;
    }

    public void setTurnDownReason(String turnDownReason) {
        this.turnDownReason = turnDownReason;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }
}
