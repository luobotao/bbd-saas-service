package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * 查询订单的query对象
 * Created by zuowenhai on 2016/4/14.
 */
public class UserQueryVO implements Serializable{
	
	public Integer status;//-1全部 1已启用 0停用
	public Integer roleId;//-1全部0站长1派件员
	public String keyword;//关键字：真实姓名或手机号
}
