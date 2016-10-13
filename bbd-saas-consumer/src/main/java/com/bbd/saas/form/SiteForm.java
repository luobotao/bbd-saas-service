package com.bbd.saas.form;

import com.bbd.saas.enums.SiteSrc;
import com.bbd.saas.enums.SiteType;
import org.hibernate.validator.constraints.NotEmpty;

public class SiteForm {
	private SiteType sitetype;
	@NotEmpty
	private String phone;    //用户的登录手机号
	@NotEmpty
	private String name;         //站点名称
	private String password;         //密码
	private SiteSrc siteSrc = SiteSrc.PUBLIC;// 站点来源
	private String companyId;    //公司ID
	private String companyName;    //公司名称
	private String areaCode;    //区域码
	private String from;    //1注册
	@NotEmpty
	private String responser;    //负责人
	@NotEmpty
	private String email;        //邮箱
	private String province;     //省
	private String city;         //市
	private String area;         //区
	@NotEmpty
	private String address;      //详细地址

	private Integer upperlimit;     //上线单量,必须为整数
	private Integer lowerlimit;     //下线单量，必须为整数

	public SiteType getSitetype() {
		return sitetype;
	}

	public void setSitetype(SiteType sitetype) {
		this.sitetype = sitetype;
	}

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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Integer getUpperlimit() {
		return upperlimit;
	}

	public void setUpperlimit(Integer upperlimit) {
		this.upperlimit = upperlimit;
	}

	public Integer getLowerlimit() {
		return lowerlimit;
	}

	public void setLowerlimit(Integer lowerlimit) {
		this.lowerlimit = lowerlimit;
	}

	public SiteSrc getSiteSrc() {
		return siteSrc;
	}

	public void setSiteSrc(SiteSrc siteSrc) {
		this.siteSrc = siteSrc;
	}
}
