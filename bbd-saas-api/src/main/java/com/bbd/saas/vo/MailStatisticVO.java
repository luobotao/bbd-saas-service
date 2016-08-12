package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * 查询订单数量的VO对象
 * Created by luobotao on 2016/4/14.
 */
public class MailStatisticVO implements Serializable{
	private String siteName; //站点名称
	private long noArrive;	//未到站订单数
	private long arrived;	//已到站订单数
	private long noDispatch;	//未分派
	private long dispatched;	//正在派送的订单数目
	private long totalDispatched;	//所有已经分派过的
	private long signed;	//签收
	private long retention;	//滞留
	private long rejection;	//拒收
	private long toOtherSite;	//转站
	private long toOtherExpress;	//转其他快递

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public long getNoArrive() {
		return noArrive;
	}

	public void setNoArrive(long noArrive) {
		this.noArrive = noArrive;
	}

	public long getArrived() {
		return arrived;
	}

	public void setArrived(long arrived) {
		this.arrived = arrived;
	}

	public long getNoDispatch() {
		return noDispatch;
	}

	public void setNoDispatch(long noDispatch) {
		this.noDispatch = noDispatch;
	}

	public long getDispatched() {
		return dispatched;
	}

	public void setDispatched(long dispatched) {
		this.dispatched = dispatched;
	}

	public long getSigned() {
		return signed;
	}

	public void setSigned(long signed) {
		this.signed = signed;
	}

	public long getRetention() {
		return retention;
	}

	public void setRetention(long retention) {
		this.retention = retention;
	}

	public long getRejection() {
		return rejection;
	}

	public void setRejection(long rejection) {
		this.rejection = rejection;
	}

	public long getToOtherSite() {
		return toOtherSite;
	}

	public void setToOtherSite(long toOtherSite) {
		this.toOtherSite = toOtherSite;
	}

	public long getToOtherExpress() {
		return toOtherExpress;
	}

	public void setToOtherExpress(long toOtherExpress) {
		this.toOtherExpress = toOtherExpress;
	}

	public long getTotalDispatched() {
		return totalDispatched;
	}

	public void setTotalDispatched(long totalDispatched) {
		this.totalDispatched = totalDispatched;
	}
}
