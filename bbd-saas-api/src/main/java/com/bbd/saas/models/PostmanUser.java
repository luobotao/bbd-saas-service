package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;


public class PostmanUser implements Serializable {

	private static final long serialVersionUID = -1619641047129597335L;
	private Integer id;
	private String staffid;
	private String nickname;
	private String phone;
	private String headicon;
	private String companyname;
	private String cardidno;
	private String substation;
	private Integer companyid;
	private String alipayAccount;
	private String token;//基础token
	private String bbttoken;
	private Double lat;// 纬度
	private Double lon;// 经度
	private Double height;// 高度
	private String addr;// 地址
	private String addrdes;// 地址描述
	private String shopurl;// 商铺描述
	private String sta;//1正常
	private String spreadticket;
	
	private Integer poststatus=0;//1开启

	private Integer postrole=0;//0:默认 1：分拣 2：司机 ……

	private Date dateNew;
	private Date dateUpd;
	private String siteid;
	private String src;
	private String areaCode;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStaffid() {
		return staffid;
	}

	public void setStaffid(String staffid) {
		this.staffid = staffid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHeadicon() {
		return headicon;
	}

	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCardidno() {
		return cardidno;
	}

	public void setCardidno(String cardidno) {
		this.cardidno = cardidno;
	}

	public String getSubstation() {
		return substation;
	}

	public void setSubstation(String substation) {
		this.substation = substation;
	}

	public Integer getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBbttoken() {
		return bbttoken;
	}

	public void setBbttoken(String bbttoken) {
		this.bbttoken = bbttoken;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddrdes() {
		return addrdes;
	}

	public void setAddrdes(String addrdes) {
		this.addrdes = addrdes;
	}

	public String getShopurl() {
		return shopurl;
	}

	public void setShopurl(String shopurl) {
		this.shopurl = shopurl;
	}

	public String getSta() {
		return sta;
	}

	public void setSta(String sta) {
		this.sta = sta;
	}

	public Integer getPoststatus() {
		return poststatus;
	}

	public void setPoststatus(Integer poststatus) {
		this.poststatus = poststatus;
	}

	public Integer getPostrole() {
		return postrole;
	}

	public void setPostrole(Integer postrole) {
		this.postrole = postrole;
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

	public String getSpreadticket() {
		return spreadticket;
	}

	public void setSpreadticket(String spreadticket) {
		this.spreadticket = spreadticket;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
