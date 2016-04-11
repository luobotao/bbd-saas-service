package com.bbd.saas.api.impl;


import com.bbd.saas.api.AdminUserService;
import com.bbd.saas.api.OrderService;
import com.bbd.saas.dao.AdminUserDao;
import com.bbd.saas.dao.OrderDao;
import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;
import org.springframework.stereotype.Service;

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
