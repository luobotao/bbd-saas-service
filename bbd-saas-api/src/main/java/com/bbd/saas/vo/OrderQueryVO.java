package com.bbd.saas.vo;

import com.bbd.saas.enums.OrderStatus;

import java.io.Serializable;
import java.util.List;

/**
 * 查询订单的query对象
 * Created by luobotao on 2016/4/8.
 */
public class OrderQueryVO implements Serializable{

	public String areaCode;//站点编码
	public Integer arriveStatus;//-1全部 0未到站 1已到站
	public String between;//预计到站时间
	public String parcelCode;//包裹号
	public String mailNum;//运单号
	public String userId;//派件员Id
	public Integer dispatchStatus;//运单分派状态，-1全部, 1-未分派,2-已分派
	public String arriveBetween;//到站时间
	public Integer abnormalStatus;//订单异常状态，-1全部 3-滞留,4-拒收
	public Integer orderStatus;//订单状态，-1全部, 0-未到站,1-未分派,2-已分派,3-滞留,4-拒收,5-已签收,6-已转其他快递,7-申请退货,8-退货完成;
	public List<String> areaCodeList;//公司Id
	public List<OrderStatus> orderStatusList;
}
