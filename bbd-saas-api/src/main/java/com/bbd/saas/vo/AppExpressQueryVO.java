package com.bbd.saas.vo;

import com.bbd.saas.enums.ExpressStatus;

import java.io.Serializable;

/**
 * app端订单到站后的流转操作的query对象
 * Created by liyanlei on 2016/8/25.
 */
public class AppExpressQueryVO implements Serializable{
	public String areaCode;	//站点编码
	public String mailNum;		//运单号
	public Integer errorStatus;//订单异常状态，-1全部 8-滞留,9-拒收
	public String userId;		//派件员Id
	public ExpressStatus expressStatus;	//物流状态

}
