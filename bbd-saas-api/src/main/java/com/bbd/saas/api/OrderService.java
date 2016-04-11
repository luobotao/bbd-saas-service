package com.bbd.saas.api;

import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;

/**
 * Created by luobotao on 2016/4/8.
 * 管理员接口
 */
public interface OrderService {


    PageModel<Order> findOrders(PageModel<Order> pageModel);
}
