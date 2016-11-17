package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.SiteStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 接驳点表
 * Created by liyanlei on 2016/11/17.
 */
@Entity("interchange_station")
public class InterchangeStation implements Serializable {
    @Id
    private ObjectId id;
    private String name;         //站点
    private String telephone;         //固定电话
    private String province;         //省
    private String city;         //市
    private String area;         //区
    private String address;         //具体地址
    private String username;         //站点登录用户名
    private String password;         //站点登录密码
    private String interchangeCode;         //接驳点编码
    private SiteStatus status;         //站点状态
    private String lat;         //纬度
    private String lng;         //经度
    private Date dateAdd;      //录入时间
    private Date dateUpd;      //更新时间

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInterchangeCode() {
        return interchangeCode;
    }

    public void setInterchangeCode(String interchangeCode) {
        this.interchangeCode = interchangeCode;
    }

    public SiteStatus getStatus() {
        return status;
    }

    public void setStatus(SiteStatus status) {
        this.status = status;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
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

