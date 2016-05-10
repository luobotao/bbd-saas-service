package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.OrderLog;


/**
 * Description: 订单物流日志表dao接口
 * @author: liyanlei
 * 2016年4月20日下午2:31:03
 */
public interface OrderLogDao {
	/**
	 * 插入一条记录
	 * @param orderLog
	 * @return
     */
	int insert(OrderLog orderLog);

}
