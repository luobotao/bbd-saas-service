package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * ToOtherSiteLog
 * 转站日志表
 * Created by luobotao on 2016/4/10.
 */
@Entity("toOtherSiteLog")
@Indexes(
        @Index(value = "fromAreaCode", fields = @Field("fromAreaCode"))
)
public class ToOtherSiteLog implements Serializable {

    private static final long serialVersionUID = 1913463928658291197L;
    @Id
    private ObjectId id;
    private String mailNum;//运单号
    private String fromAreaCode;//转站源站点编码
    private String toAreaCode;//转站目的站点编码
    private Date dateArrived;//到站时间
    private Date operTime;//转站时间

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMailNum() {
        return mailNum;
    }

    public void setMailNum(String mailNum) {
        this.mailNum = mailNum;
    }

    public String getFromAreaCode() {
        return fromAreaCode;
    }

    public void setFromAreaCode(String fromAreaCode) {
        this.fromAreaCode = fromAreaCode;
    }

    public String getToAreaCode() {
        return toAreaCode;
    }

    public void setToAreaCode(String toAreaCode) {
        this.toAreaCode = toAreaCode;
    }

    public Date getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(Date dateArrived) {
        this.dateArrived = dateArrived;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }
}
