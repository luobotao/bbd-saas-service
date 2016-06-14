package com.bbd.saas.vo;

import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询订单的update对象
 * Created by liyanlei on 2016/4/16.
 */
public class ExpressExchangeVO implements Serializable{
	public String url;                                  //请求url
	public String operator;                             //操作人
	//public Order order;                                 //订单
	public Srcs src;									//来源
	public String typ;                                  //操作类型
	public String pushInfo;                             //推送内容，json字符串
	public String requestStr;                           //请求串
	public String responseStr;                          //返回结果串
	public String status;                               //操作状态
	public String memo;                                 //备注
	public String pushCount;                            //推送次数
	public String timeInterval;                         //间隔时间
	public String dateAdd;                              //添加时间
	public String dateUpd;                              //更新时间
}
