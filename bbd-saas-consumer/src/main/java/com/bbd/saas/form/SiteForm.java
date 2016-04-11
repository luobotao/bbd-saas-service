package com.bbd.saas.form;

import org.hibernate.validator.constraints.NotEmpty;

public class SiteForm {
	
	@NotEmpty
	private String name;         //站点名称
	@NotEmpty
	private String responser;    //负责人
	private String phone;        //负责人手机号
	private String telephone;    //固定电话
	@NotEmpty
	private String email;        //邮箱
	private String siteProvince;     //省
	private String siteCity;         //市
	private String siteArea;         //区
	@NotEmpty
	private String siteAddress;      //详细地址
	private String licensePic;   //营业执照
	@NotEmpty
	private String username;     //账号
	@NotEmpty
	private String password;     //密码
	private String prePic;       //历史图片
}
