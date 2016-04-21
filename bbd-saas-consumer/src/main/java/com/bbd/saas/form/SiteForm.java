package com.bbd.saas.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class SiteForm {
	
	@NotEmpty
	private String name;         //站点名称
	@NotEmpty
	private String companyId;    //公司ID
	private String companyName;    //公司名称
	@NotEmpty
	private String responser;    //负责人
	private String telephone;    //固定电话
	@NotEmpty
	private String email;        //邮箱
	private String province;     //省
	private String city;         //市
	private String area;         //区
	@NotEmpty
	private String address;      //详细地址
	@NotEmpty
	private String username;     //账号
	@NotEmpty
	private String password;     //密码
	private String prePic;       //历史图片

	private String lng;			//经度
	private String lat;			//纬度

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResponser() {
		return responser;
	}

	public void setResponser(String responser) {
		this.responser = responser;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public String getPrePic() {
		return prePic;
	}

	public void setPrePic(String prePic) {
		this.prePic = prePic;
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
}
