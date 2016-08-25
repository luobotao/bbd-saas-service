package com.bbd.saas.vo;

import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.PrintStatus;

import java.io.Serializable;

/**
 * 查询订单的query对象
 * Created by luobotao on 2016/4/8.
 */
public class AppOrderQueryVO implements Serializable{
	public String areaCode;	//站点编码
	public String mailNum;		//运单号
	public String userId;		//派件员Id
	public ExpressStatus expressStatus;	//物流状态
	public PrintStatus printStatus;	//打印状态
	public int isRemoved;	//是否被移除？ 0：未被移除； 1：被移除
	public int isArrived;	//是否到站
	public String dateArrived_max;	//到站时间_最大值
	public String dateArrived_min;	//到站时间_最小值
	public String dateUpd_max;	//更新时间_最大值
	public String dateUpd_min;	//更新时间_最小值
}
