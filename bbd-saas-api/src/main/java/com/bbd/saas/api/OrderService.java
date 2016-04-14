package com.bbd.saas.api;

import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateResults;

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

	/**
	 * 根据站点编码获取该站点订单数据
	 * @param areaCode
	 * @return
     */
	OrderNumVO getOrderNumVO(String areaCode);

	/**
	 * 根据运单号更新订单的订单状态
	 * @param mailNum 运单号
	 * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
	 * @param orderStatusNew
     * @return
     */
	void updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew);
}
