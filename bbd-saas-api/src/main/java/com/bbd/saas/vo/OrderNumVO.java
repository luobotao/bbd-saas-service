package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * 查询订单数量的VO对象
 * Created by luobotao on 2016/4/14.
 */
public class OrderNumVO implements Serializable{
	
    private long noArrive;//今日未到站
	private long noArriveHis;//历史未到站
	private long arrived;//今日已到站
	private long totalDispatched;//所有已分派的

	public long getNoArrive() {
		return noArrive;
	}

	public void setNoArrive(long noArrive) {
		this.noArrive = noArrive;
	}

	public long getNoArriveHis() {
		return noArriveHis;
	}

	public void setNoArriveHis(long noArriveHis) {
		this.noArriveHis = noArriveHis;
	}

	public long getArrived() {
		return arrived;
	}

	public void setArrived(long arrived) {
		this.arrived = arrived;
	}

	public long getTotalDispatched() {
		return totalDispatched;
	}

	public void setTotalDispatched(long totalDispatched) {
		this.totalDispatched = totalDispatched;
	}
}
