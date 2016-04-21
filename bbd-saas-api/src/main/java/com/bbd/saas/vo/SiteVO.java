package com.bbd.saas.vo;

import java.io.Serializable;


/**
 * Description: 站点信息
 * @author: liyanlei
 * 2016年4月18日上午10:14:10
 */
public class SiteVO implements Serializable{
	
	private static final long serialVersionUID = -6846727221321119373L;
	private String id;
    private String areaCode;
    private String name;
     
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
   
}