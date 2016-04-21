package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * Description: 派件员信息
 * @author: liyanlei
 * 2016年4月12日上午11:21:34
 */
public class UserVO implements Serializable{
	
	private static final long serialVersionUID = -6129807696406731504L;
	private String id;
    private String loginName;
    private String realName;
    private String phone;
    private String staffId;
    
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
}