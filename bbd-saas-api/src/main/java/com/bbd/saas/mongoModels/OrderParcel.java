package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.ParcelStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 包裹表
 * Created by luobotao on 2016/4/10.
 */
@Entity("orders_parcel")
@Indexes(
        @Index(value = "parcelCode", fields = @Field("parcelCode"))
)
public class OrderParcel implements Serializable {

    private static final long serialVersionUID = 1179453397236160495L;
    @Id
    private ObjectId id;
    // 包裹号
    private String parcelCode;
    // 运单号
    private String trackNo;
    //分拣员ID
    private String sort_uid;
    //司机ID
    private String driver_uid;
    //包裹状态
    private ParcelStatus status;
    //区域（站点）地址
    private String areaRemark;
    //区域（站点）编码
    private String areaCode;
    //区域（站点）名称
    private String areaName;
    //区域（站点）详细地址
    private String station_address;
    //订单列表
    private List<Order> orderList;
    //包裹类型 0：配件包裹（默认） 1：集包
    private String parceltyp;

    private String srcAreaCode; //源区域码
    private Date dateAdd;
    private Date dateUpd;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getParcelCode() {
        return parcelCode;
    }

    public void setParcelCode(String parcelCode) {
        this.parcelCode = parcelCode;
    }

    public String getSort_uid() {
        return sort_uid;
    }

    public void setSort_uid(String sort_uid) {
        this.sort_uid = sort_uid;
    }

    public String getDriver_uid() {
        return driver_uid;
    }

    public void setDriver_uid(String driver_uid) {
        this.driver_uid = driver_uid;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public void setStatus(ParcelStatus status) {
        this.status = status;
    }

    public String getAreaRemark() {
        return areaRemark;
    }

    public void setAreaRemark(String areaRemark) {
        this.areaRemark = areaRemark;
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

    public String getStation_address() {
        return station_address;
    }

    public void setStation_address(String station_address) {
        this.station_address = station_address;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
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

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public String getParceltyp() {
        return parceltyp;
    }

    public void setParceltyp(String parceltyp) {
        this.parceltyp = parceltyp;
    }

    public String getSrcAreaCode() {
        return srcAreaCode;
    }

    public void setSrcAreaCode(String srcAreaCode) {
        this.srcAreaCode = srcAreaCode;
    }
}