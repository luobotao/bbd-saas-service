package com.bbd.saas.vo;

import java.io.Serializable;
import java.util.Date;

import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Site;
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
	public String areaName;//站点名称
	public String areaRemark;//站点地址
	public User user;//派件员
	//public String staffId;//派件员员工Id
	public Site site;//本站点编号
	public String returnReasonType;//退货原因类型
	public String returnReasonInfo;//退货原因详情--其他
	public OrderStatus orderStatus;//订单状态，-1全部, 0-未到站,1-未分派,2-已分派,3-滞留,4-拒收,5-已签收,6-已转其他快递,7-申请退货,8-退货完成;
	public Express express;//增加一条物流信息
	public ExpressStatus expressStatus; //物流状态
	private Date dateUpd;//修改时间
}
