package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.OrderLogService;
import com.bbd.saas.dao.mysql.OrderLogDao;
import com.bbd.saas.models.OrderLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Description: 快递员派送运单信息Service实现
 * @author: liyanlei
 * 2016年4月20日下午3:57:47
 */
@Service("orderLogService")
@Transactional
public class OrderLogServiceImpl implements OrderLogService {
	@Resource
	private OrderLogDao orderLogDao;

	public OrderLogDao getOrderLogDao() {
		return orderLogDao;
	}

	public void setOrderLogDao(OrderLogDao orderLogDao) {
		this.orderLogDao = orderLogDao;
	}

	@Override
	public int insert(OrderLog orderLog) {
		return orderLogDao.insert(orderLog);
	}
}
