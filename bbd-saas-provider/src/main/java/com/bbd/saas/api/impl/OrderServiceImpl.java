package com.bbd.saas.api.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.saas.api.OrderService;
import com.bbd.saas.dao.OrderDao;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {
	
    private OrderDao orderDao;

    public PageModel<Order> findOrders( PageModel<Order> pageModel){
      return orderDao.findOrders(pageModel);
    }
    
    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

	
    /**
	 * Description: 根据运单号查询订单信息
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:38:38
	 */
	@Override
	public Order findOneByMailNum(String mailNum) {
		
		return null;
	}
}
