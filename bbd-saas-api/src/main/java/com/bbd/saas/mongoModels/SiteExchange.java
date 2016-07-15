package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.ExpressExchangeStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 站点编码转换
 */
@Entity("siteExchange")
public class SiteExchange implements Serializable {

    @Id
    private ObjectId id;

    public String areaCode;                             //saas系统站点编码
    public String exchangeType;                         //转站对象
    public String exchangeCode;                         //转站编码
    public String status;                               //状态 0 无效 1 有效
    public String operator;                             //操作人
    public String memo;                                 //备注
    public Date dateAdd;                                //添加时间      （初始必填）
    public Date dateUpd;                                //更新时间

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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