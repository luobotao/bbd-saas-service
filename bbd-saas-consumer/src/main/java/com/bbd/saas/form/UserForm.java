package com.bbd.saas.form;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.samples.mvc.convert.MaskFormat;
import org.springframework.samples.mvc.form.InquiryType;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class UserForm {
	
	@NotEmpty
	private String roleId;
	@NotEmpty
	private String realName;
	@NotEmpty
	private String phone;
	@NotEmpty
	private String loginName;
	@NotEmpty
	private String loginPass;
	@NotEmpty
	private String loginNameTemp;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPass() {
		return loginPass;
	}
	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}
	public String getLoginNameTemp() {
		return loginNameTemp;
	}
	public void setLoginNameTemp(String loginNameTemp) {
		this.loginNameTemp = loginNameTemp;
	}


}
