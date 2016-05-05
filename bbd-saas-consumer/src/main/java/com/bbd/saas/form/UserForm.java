package com.bbd.saas.form;

import org.hibernate.validator.constraints.NotEmpty;

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
	@NotEmpty
	private String staffid;
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
	public String getStaffid() {
		return staffid;
	}
	public void setStaffid(String staffid) {
		this.staffid = staffid;
	}


}
