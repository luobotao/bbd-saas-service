package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单物流状态日志实体类
 * @author liyanlei
 *
 */
public class OrderLog implements Serializable {
	private static final long serialVersionUID = -289981235096107728L;
	private Integer id;
	private String areaCode;//站点号
	private String orderNo;//订单号
	private String mailNum;//运单号
	private Date operTime;//日期
	//0-未到达站点，1-已到达站点，2-正在派送，3-已签收，4-已滞留，5-已拒收，6-转站, 7-已丢失,8-已取消
	private Integer status;//状态


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

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

	public String getMailNum() {
		return mailNum;
	}

	public void setMailNum(String mailNum) {
		this.mailNum = mailNum;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
