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

    //分拣员ID
    private String sort_uid;
    //司机ID
    private String driver_uid;
    //站长ID
    private String station_uid;
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
    private Date dateAdd;
    private Date dateUpd;
    private Date dateParcel;
    //包裹类型 0：配件包裹（默认） 1：集包
    private String parceltyp;
    // 运单号
    private String trackNo;
    // 源区域码
    private String srcAreaCode;
    //订单企业来源
    private String src;
    //订单省份
    private String province;
    //订单城市
    private String city;
    //订单区域
    private String area;
    //包裹订单数量
    private Integer ordercnt;

    // 路线时间
    private String wayDate;

    // 路线名称
    private String wayname;

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

    public String getStation_uid() {
        return station_uid;
    }

    public void setStation_uid(String station_uid) {
        this.station_uid = station_uid;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getOrdercnt() {
        return ordercnt;
    }

    public void setOrdercnt(Integer ordercnt) {
        this.ordercnt = ordercnt;
    }

    public String getWayDate() {
        return wayDate;
    }

    public void setWayDate(String wayDate) {
        this.wayDate = wayDate;
    }

    public String getWayname() {
        return wayname;
    }

    public void setWayname(String wayname) {
        this.wayname = wayname;
    }

    public Date getDateParcel() {
        return dateParcel;
    }

    public void setDateParcel(Date dateParcel) {
        this.dateParcel = dateParcel;
    }
}