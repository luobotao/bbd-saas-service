package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * 查询投诉的query对象
 * Created by liyanlei on 2016/09/27.
 */
public class ComplaintQueryVO implements Serializable{
	public Integer complaintStatus;		//投诉状态
	public Integer appealStatus;   //申诉状态
	public Integer reason;   //投诉理由
	public String between;//投诉时间
	public String mailNum;//运单号
	public String areaCode;         //站点编码
}
