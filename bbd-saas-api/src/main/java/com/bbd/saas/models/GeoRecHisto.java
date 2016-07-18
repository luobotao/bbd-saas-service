package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单物流状态日志实体类
 * @author liyanlei
 *
 */
public class GeoRecHisto implements Serializable {
	private static final long serialVersionUID = -289981235096107728L;
	public Integer id;
	public String orderNo;      //订单号
	public String src;          //来源
	public String province;		//
	public String city;			//
	public String area;			//
	public String address;      //收件人地址
	public double lat;          //纬度
	public double lng;          //经度
	public String geoStr;       //geoHash处理后的内容
	public String dateAdd;        //添加时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getGeoStr() {
		return geoStr;
	}

	public void setGeoStr(String geoStr) {
		this.geoStr = geoStr;
	}

	public String getDateAdd() {
		return dateAdd;
	}

	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	public GeoRecHisto(){

	}

	public GeoRecHisto(Integer id, String orderNo, String src, String province, String city, String area, String address, double lat, double lng, String geoStr, String dateAdd) {
		this.id = id;
		this.orderNo = orderNo;
		this.src = src;
		this.province = province;
		this.city = city;
		this.area = area;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.geoStr = geoStr;
		this.dateAdd = dateAdd;
	}


}
