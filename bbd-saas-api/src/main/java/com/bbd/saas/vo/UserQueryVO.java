package com.bbd.saas.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 查询订单的query对象
 * Created by zuowenhai on 2016/4/14.
 */
public class UserQueryVO implements Serializable{
	
	public Integer status;//-1全O部 1已启用 0停用
	public String roleId;//SITEMASTER(0, "站长"),SENDMEM(1, "派件员"),COMPANY(2, "公司管理员"); UserRole
	public String keyword;//关键字：真实姓名或手机号
	public String companyId;//公司ID
	public List staffidList;
}
