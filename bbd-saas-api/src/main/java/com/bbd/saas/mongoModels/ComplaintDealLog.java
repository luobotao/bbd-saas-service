package com.bbd.saas.mongoModels;

import java.util.Date;

/**
 *
 *@auhor ctt
 *@date 2016/8/24
 *@since V1.0
 * 投诉处理记录
 */
public class ComplaintDealLog {
    public String dealTyp;
    public String dealReason;
    public String operator;
    public String username;
    public Date dateAdd;


    public String getDealTyp() {
        return dealTyp;
    }

    public void setDealTyp(String dealTyp) {
        this.dealTyp = dealTyp;
    }

    public String getDealReason() {
        return dealReason;
    }

    public void setDealReason(String dealReason) {
        this.dealReason = dealReason;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }
}
