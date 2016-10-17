package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * Description: 快递配送实体类查询
 * @author: liyanlei
 * 2016年4月12日上午11:21:34
 */
public class PostDeliveryQueryVO implements Serializable{
	
	private static final long serialVersionUID = -6129807696406731504L;
	public String companyid;
	public String siteId;
	public String subStation;
	public String time;
	public Integer postman_id;//派送员唯一 ID
	public String dateUpd;
	public String dateUpd_min;//
	public String dateUpd_max;
}
