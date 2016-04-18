package com.bbd.saas.vo;

import java.io.Serializable;
import java.util.Date;

import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.User;

/**
 * 查询订单的update对象
 * Created by liyanlei on 2016/4/16.
 */
public class OrderUpdateVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2496543557609935953L;
	public String areaCode;//站点编码
	public User user;//派件员
	public String userId;//派件员Id
	public String selfAreaCode;//本站点编号
	public String returnReasonType;//退货原因类型
	public String returnReasonInfo;//退货原因详情--其他
	public OrderStatus orderStatus;//订单状态，-1全部, 0-未到站,1-未分派,2-已分派,3-滞留,4-拒收,5-已签收,6-已转其他快递,7-申请退货,8-退货完成;
	private Date dateUpd;//修改时间
}
