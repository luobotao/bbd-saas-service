package com.bbd.saas.vo;

import java.io.Serializable;
import java.util.Date;


/**
 * Description: 站点信息
 * @author: liyanlei
 * 2016年4月18日上午10:14:10
 */
public class SiteKeywordVO implements Serializable{
	
	private static final long serialVersionUID = -6846727221321119373L;
	private String id;	//关键词Id
	private String siteId; //站点序号
	private String siteName;//站点名称
	private Date createAt;
	private String province;//省
	private String city;//市
	private String distict;//区
	private String keyword;//关键词

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public String getDistict() {
		return distict;
	}

	public void setDistict(String distict) {
		this.distict = distict;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
