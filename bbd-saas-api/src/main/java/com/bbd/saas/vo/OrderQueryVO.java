package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * 查询订单的query对象
 * Created by luobotao on 2016/4/8.
 */
public class OrderQueryVO implements Serializable{

	public String areaCode;//站点编码
	public Integer arriveStatus;//-1全部 0未到站 1已到站
	public String between;
	public String parcelCode;//包裹号
	public String mailNum;//运单号
}
