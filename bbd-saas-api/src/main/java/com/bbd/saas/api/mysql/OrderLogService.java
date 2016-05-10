package com.bbd.saas.api.mysql;

import com.bbd.saas.models.OrderLog;

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

	
}
