package com.bbd.saas.vo;

import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;

import java.io.Serializable;
import java.util.List;

/**
 * 查询订单的query对象
 * Created by zuowenhai on 2016/4/14.
 */
public class UserQueryVO2 implements Serializable{
	
	public UserStatus userStatus;//-1全O部 1已启用 0停用
	public UserRole role;//SITEMASTER(0, "站长"),SENDMEM(1, "派件员"),COMPANY(2, "公司管理员"); UserRole
	public String companyId;//公司ID
	public List postManIdList;
}
