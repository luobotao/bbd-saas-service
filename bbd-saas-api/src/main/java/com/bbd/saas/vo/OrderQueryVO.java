package com.bbd.saas.vo;

import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.User;

import java.io.Serializable;
import java.util.List;

/**
 * 查询订单的query对象
 * Created by luobotao on 2016/4/8.
 */
public class OrderQueryVO implements Serializable{

	public String areaCode;//站点编码
	public Integer arriveStatus;//-1全部 0未到站 1已到站
	public String between;//预计站点入库时间
	public String parcelCode;//包裹号
	public String mailNum;//运单号
	public String userId;//派件员Id
	public String embraceId;//揽件员Id
	public String type;//揽件入库状态 0：今日成功接单数；1：历史未入库；2：今日已入库；3：今日未入库
	public User user;//站长
	public Integer dispatchStatus;//运单分派状态，-1全部, 1-未分派,2-已分派
	public String arriveBetween;//站点入库时间
	public Integer abnormalStatus;//订单异常状态，-1全部 3-滞留,4-拒收
	public Integer orderStatus;//订单状态，-1全部, 0-未到站(特别处理下),1-未分派,2-已分派,3-滞留,4-拒收,5-已签收,6-已转其他快递,7-申请退货,8-退货完成;
	public List<String> areaCodeList;//公司Id
	public List<OrderStatus> orderStatusList;
	public String orderSetStatus;//运单集包状态
	public String tradeNo;//商户订单号(我们自己生成的支付订单号)
	public String tradeOrMailNoLike;//商户订单号（tradeNo）或者运单号(mailnum)
	public String dateArrived;//到达时间
}
