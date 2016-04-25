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

}
