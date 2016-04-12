package com.bbd.saas.vo;

import java.io.Serializable;

import org.bson.types.ObjectId;

/**
 * Description: 派件员信息
 * @author: liyanlei
 * 2016年4月12日上午11:21:34
 */
public class UserVO implements Serializable{
	
	private String id;
    private String loginName;
    private String realName;
    private String phone;
    
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
}
