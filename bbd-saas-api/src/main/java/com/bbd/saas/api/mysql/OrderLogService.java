package com.bbd.saas.api.mysql;

import com.bbd.saas.models.OrderLog;
import com.bbd.saas.vo.OrderMonitorVO;

/**
 * Description: 订单物流状态日志操作接口
 * @author: liyanlei
 * 2016年5月9日下午5:53:06
 */
public interface OrderLogService {
	/**
	 * 插入一条记录
	 * @param OrderLog
	 * @return
     */
	int insert(OrderLog OrderLog);
	/**
	 * 统计每天不同状态的订单数
	 * @param areaCode 站点编号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public OrderMonitorVO statisticOrderNum(String areaCode, String startDate, String endDate);
	
}
