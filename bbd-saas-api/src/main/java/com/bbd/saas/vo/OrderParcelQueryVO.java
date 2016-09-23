package com.bbd.saas.vo;

import com.bbd.saas.enums.ParcelStatus;

import java.io.Serializable;

/**
 * 查询订单的query对象
 * Created by luobotao on 2016/4/8.
 */
public class OrderParcelQueryVO implements Serializable{

	public String station_uid;//站长ID
	public ParcelStatus status;//包裹状态


}
