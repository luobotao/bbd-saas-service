package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;


/**
 * 公司实体类
 *
 * @author luobotao
 */
public class Postcompany implements Serializable {
    private static final long serialVersionUID = -3645677630568952615L;
    private Integer id;
    private String companyname;
    private String companycode;
    private String logo;
    private String sta;//状态 0未审核 1审核通过 2审核失败
    private String ishot;//是否热门
    private int nsort;//排序
    private String deliveryflag;//是否可以进行派单 0不可派单 1可派单
    private String appid;//支付宝的appid
    private String privatekey;//支付宝的privatekey
    private String alipaypublickey;//支付宝的alipaypublickey

    private String licensePic;   //营业执照
    private String email;        //邮箱
    private String province;     //省
    private String city;         //市
    private String area;         //区
    private String address;      //详细地址

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public int getNsort() {
        return nsort;
    }

    public void setNsort(int nsort) {
        this.nsort = nsort;
    }

    public String getDeliveryflag() {
        return deliveryflag;
    }

    public void setDeliveryflag(String deliveryflag) {
        this.deliveryflag = deliveryflag;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getAlipaypublickey() {
        return alipaypublickey;
    }

    public void setAlipaypublickey(String alipaypublickey) {
        this.alipaypublickey = alipaypublickey;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
