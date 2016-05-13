package com.bbd.saas.form;

import org.hibernate.validator.constraints.NotEmpty;

public class SiteForm {
	
	@NotEmpty
	private String phone;    //用户的登录手机号
	@NotEmpty
	private String name;         //站点名称
	private String password;         //密码

	private String companyId;    //公司ID
	private String companyName;    //公司名称
	private String areaCode;    //区域码
	@NotEmpty
	private String responser;    //负责人
	@NotEmpty
	private String email;        //邮箱
	private String province;     //省
	private String city;         //市
	private String area;         //区
	@NotEmpty
	private String address;      //详细地址

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getResponser() {
		return responser;
	}

	public void setResponser(String responser) {
		this.responser = responser;
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
