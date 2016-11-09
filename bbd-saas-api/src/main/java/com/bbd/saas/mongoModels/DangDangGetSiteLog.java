package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 当当运单获取目标站点的日志表
 * Created by liyanlei on 2016/10/14.
 */
@Entity("dangDangGetSiteLog")
@Indexes(
        @Index(value = "mechId", fields = @Field("mechId"))
)
public class DangDangGetSiteLog implements Serializable {
    private static final long serialVersionUID = 7742560057056014887L;
    @Id
    private ObjectId id;
    private String siteNo;         //站点areaCode
    private String siteName;         //站点名称
    private String orderId;         //order mailNum
    private String responseMap;         //响应信息
    private Date dateAdd;      //录入时间
    private Date dateUpd;      //更新时间

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(String responseMap) {
        this.responseMap = responseMap;
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

