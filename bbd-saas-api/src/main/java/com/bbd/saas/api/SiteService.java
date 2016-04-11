package com.bbd.saas.api;

import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;

/**
 * Created by luobotao on 2016/4/11.
 * 站点接口
 */
public interface SiteService {


    PageModel<Order> findOrders(PageModel<Order> pageModel);
}
