package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.OrderUpdateVO;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 * Created by luobotao on 2016/4/8.
 * 订单接口
 */
public interface OrderService {

	/**
	 * Description: 根据运单号查询订单信息
	 * @param areaCode 站点编码（可为空，为空则所有站点）
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:38:38
	 */
	Order findOneByMailNum(String areaCode,String mailNum);

	/**
	 * 带查询条件去检索订单
	 * @param pageModel
	 * @param orderQueryVO
     * @return
     */
    PageModel<Order> findOrders(PageModel<Order> pageModel,OrderQueryVO orderQueryVO);
    
    /**
     * Description: 按照查询条件分页检索订单
     * @param pageIndex 当前页
     * @param orderQueryVO 查询条件
     * @return
     * @author: liyanlei
     * 2016年4月15日下午1:30:21
     */
    PageModel<Order> findPageOrders(Integer pageIndex, OrderQueryVO orderQueryVO);
    
    /**
     * Description: 按照查询条件检索订单-不分页
     * @param orderQueryVO 检索条件
     * @return
     * @author: liyanlei
     * 2016年4月15日下午4:28:49
     */
    List<Order> findOrders(OrderQueryVO orderQueryVO);
    
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
	 * 同时会修改该订单所处包裹里的状态
	 * @param mailNum 运单号
	 * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
	 * @param orderStatusNew
     * @return
     */
	void updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew);
	
	/**
     * Description: 按给定条件更新指定字段
     * @param orderUpdateVO	待更新字段和值
     * @param orderQueryVO 检索条件
     * @return 更新的记录数
     * @author: liyanlei
     * 2016年4月16日下午1:23:37
     */
    int updateOrder(OrderUpdateVO orderUpdateVO, OrderQueryVO orderQueryVO);


	/**
	 * 根据站点编码和时间获取该站点已分派的订单数
	 * @param areaCode 站点编号
	 * @param betweenTime 查询时间范围
	 * @return
	 */
	public long getDispatchedNums(String areaCode, String betweenTime);

	/**
	 * 得到指定站点当天更新的所有订单
	 * @param areaCode 站点编号
	 * @return 订单集合
	 */
	public List<Order> getTodayUpdateOrdersBySite(String areaCode);

}
