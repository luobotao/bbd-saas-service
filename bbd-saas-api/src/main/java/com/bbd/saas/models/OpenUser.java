package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;


public class OpenUser implements Serializable {

	private static final long serialVersionUID = -1619641047129597335L;
	private Integer id;
	private Integer typ;
	private String token;
	private String code;
	private String name;
	private String address;
	private String custId;
	private String regionCode;
	private String contact;
	private String phone;//基础token
	private String email;

	private String devUrl;// 地址
	private String prodUrl;// 地址描述

	private Date dateNew;
	private Date dateUpd;
	
	private String getOrderUrlDev;// 地址
	private String getOrderUrl_pro;// 地址描述

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTyp() {
		return typ;
	}

	public void setTyp(Integer typ) {
		this.typ = typ;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDevUrl() {
		return devUrl;
	}

	public void setDevUrl(String devUrl) {
		this.devUrl = devUrl;
	}

	public String getProdUrl() {
		return prodUrl;
	}

	public void setProdUrl(String prodUrl) {
		this.prodUrl = prodUrl;
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

	public String getGetOrderUrlDev() {
		return getOrderUrlDev;
	}

	public void setGetOrderUrlDev(String getOrderUrlDev) {
		this.getOrderUrlDev = getOrderUrlDev;
	}

	public String getGetOrderUrl_pro() {
		return getOrderUrl_pro;
	}

	public void setGetOrderUrl_pro(String getOrderUrl_pro) {
		this.getOrderUrl_pro = getOrderUrl_pro;
	}
}
