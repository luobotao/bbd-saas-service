package com.bbd.saas.api;

import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderQueryVO;
import org.mongodb.morphia.Key;

/**
 * Created by luobotao on 2016/4/8.
 * 订单接口
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

	/**
	 * 带查询条件去检索订单
	 * @param pageModel
	 * @param orderQueryVO
     * @return
     */
    PageModel<Order> findOrders(PageModel<Order> pageModel,OrderQueryVO orderQueryVO);
    
    /**
     * Description: 更新
     * @param order
     * @return
     * @author: liyanlei
     * 2016年4月12日下午5:59:03
     */
    Key<Order> save(Order order);

}
