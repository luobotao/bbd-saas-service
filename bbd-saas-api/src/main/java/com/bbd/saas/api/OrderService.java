package com.bbd.saas.api;

import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;

/**
 * Created by luobotao on 2016/4/8.
 * 管理员接口
 */
public interface OrderService {

	/**
	 * Description: 根据运单号查询订单信息
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:38:38
	 */
	Order findOneByMailNum(String mailNum);
	
    PageModel<Order> findOrders(PageModel<Order> pageModel);
    
    /**
     * Description: 更新
     * @param order
     * @return
     * @author: liyanlei
     * 2016年4月12日下午5:59:03
     */
    Order update(Order order);
}
