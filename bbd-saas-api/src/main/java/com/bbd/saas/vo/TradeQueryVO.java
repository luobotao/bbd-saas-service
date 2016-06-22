package com.bbd.saas.vo;


import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Set;

/**
 * 查询Trade的query对象
 * Created by liyanlei on 2016/6/13.
 */
public class TradeQueryVO implements Serializable {
	public ObjectId uId;//用户ID
	public Integer tradeStatus;//商户订单状态
	public String tradeNo;//商户订单号
	public String tradeNoLike;//商户订单号包含tradeNoLike
	public String noLike;//商户订单号或者运单号包含noLike
	public String dateAddStart; //下单时间范围 -- 开始
	public String dateAddEnd; //下单时间范围 -- 结束
	public String rcvKeyword; //订单的运单中收件人手机号、姓名、地址中包含rcvKeyword的订单
	public Set tradeNoSet;//商户订单号


}
