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
	public String tradeNo;//商户订单号
	public String tradeNoLike;//商户订单号包含tradeNoLike
	public String dateAddStart; //下单时间范围 -- 开始
	public String dateAddEnd; //下单时间范围 -- 结束
	public Set tradeNoSet;//商户订单号
	public Integer tradeStatus;//商户订单状态

}
