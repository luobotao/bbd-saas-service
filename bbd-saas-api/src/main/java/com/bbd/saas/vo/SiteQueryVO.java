package com.bbd.saas.vo;

import com.bbd.saas.enums.SiteStatus;

import java.io.Serializable;


/**
 * Description: 站点信息
 * @author: liyanlei
 * 2016年4月18日上午10:14:10
 */
public class SiteQueryVO implements Serializable{
	
	private static final long serialVersionUID = -6846727221321119373L;
	public String id;
	public String areaCode;
	public String name;
	public String companyId;//公司Id
	public String companyCode;//公司code
	public SiteStatus status;//状态
	public String responser;
	public String keyword;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
