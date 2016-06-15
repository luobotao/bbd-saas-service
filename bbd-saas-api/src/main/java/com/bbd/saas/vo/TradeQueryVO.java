package com.bbd.saas.vo;


import com.bbd.saas.enums.TradeStatus;
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
	public String dateAddBetween;//下单时间
	public Set tradeNoSet;//商户订单号
	public Integer tradeStatus;//商户订单状态

}
