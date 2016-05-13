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
	private String lat;//纬度
	private String lng;//经度
	private String deliveryArea;//配送范围
     
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getDeliveryArea() {
		return deliveryArea;
	}

	public void setDeliveryArea(String deliveryArea) {
		this.deliveryArea = deliveryArea;
	}
}
