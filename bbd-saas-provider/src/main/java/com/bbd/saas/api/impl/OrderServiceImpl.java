package com.bbd.saas.api.impl;


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
}
