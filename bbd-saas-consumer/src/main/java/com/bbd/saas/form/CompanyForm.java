package com.bbd.saas.form;

import org.hibernate.validator.constraints.NotEmpty;

public class CompanyForm {
	
	@NotEmpty
	private String companyname;    //公司名称
	@NotEmpty
	private String responser;    //负责人
	@NotEmpty
	private String phone;    //用户的登录手机号
	@NotEmpty
	private String email;        //邮箱
	private String province;     //省
	private String city;         //市
	private String area;         //区
	@NotEmpty
	private String address;      //详细地址

	private String prePic;       //历史图片

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getResponser() {
		return responser;
	}

	public void setResponser(String responser) {
		this.responser = responser;
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

	public String getPrePic() {
		return prePic;
	}

	public void setPrePic(String prePic) {
		this.prePic = prePic;
	}
}
