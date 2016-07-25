package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.OrderUpdateVO;
import com.mongodb.BasicDBList;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;

import java.util.Date;
import java.util.List;

/**
 * Created by luobotao on 2016/4/8.
 * 订单接口
 */
public interface OrderService {

	/**
	 * 根据站点和运单号查询单个订单信息
	 * @param areaCode 站点编码（可为空，为空则所有站点）
	 * @param mailNum 运单号
	 * @return 订单
	 * @author: liyanlei
	 * 2016年4月12日下午3:38:38
	 */
	Order findOneByMailNum(String areaCode,String mailNum);

	/**
	 * 根据其他快递的运单号查询订单
	 * @param newMailNum
	 * @return
	 */
	List<Order> findOneByNewMailNum(String newMailNum);

	/**
	 * 根据订单号查询订单
	 * @param orderNO
	 * @return
	 */
	Order findByOrderNo(String orderNO);
	/**
	 * 根据订单号查询订单
	 * @param orderNO
	 * @return
	 */
	Order findByOrderNoAndSrc(String orderNO,Srcs srcs);

	/**
	 * 带查询条件去检索订单
	 * @param pageModel 分页对象
	 * @param orderQueryVO 查询条件
	 * @return 分页对象（分页信息和数据）
	 */
	PageModel<Order> findOrders(PageModel<Order> pageModel,OrderQueryVO orderQueryVO);

	/**
	 * 按照查询条件分页检索订单
	 * @param pageIndex 当前页
	 * @param orderQueryVO 查询条件
	 * @return 分页对象（分页信息和数据）
	 * @author: liyanlei
	 * 2016年4月15日下午1:30:21
	 */
	PageModel<Order> findPageOrders(Integer pageIndex, OrderQueryVO orderQueryVO);

	/**
	 * 按照查询条件检索订单-不分页
	 * @param orderQueryVO 检索条件
	 * @return 订单集合
	 * @author: liyanlei
	 * 2016年4月15日下午4:28:49
	 */
	List<Order> findOrders(OrderQueryVO orderQueryVO);

	/**
	 * 保存或者更新订单
	 * @param order 订单对象
	 * @return 保存返回结果
	 */
	Key<Order> save(Order order);

	/**
	 * 根据站点编码获取该站点订单数据
	 * @param areaCode 站点编码
	 * @return 订单数据（今日未到站、历史未到站、今日已到站）
	 */
	OrderNumVO getOrderNumVO(String areaCode);

	/**
	 * 根据运单号更新订单的订单状态
	 * 同时会修改该订单所处包裹里的状态
	 * @param mailNum 运单号
	 * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
	 * @param orderStatusNew 订单新状态
	 */
	void updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew);

	/**
	 * 按给定条件更新指定字段 -- 暂时没有用到
	 * @param orderUpdateVO	待更新字段和值
	 * @param orderQueryVO 检索条件
	 * @return 更新的记录数
	 * @author: liyanlei
	 * 2016年4月16日下午1:23:37
	 */
	int updateOrder(OrderUpdateVO orderUpdateVO, OrderQueryVO orderQueryVO);

	/**
	 * 根据站点编码和到站时间获取该站点已分派的订单数 -- 暂时没有用到（接口实现还有问题）
	 * @param areaCode 站点编号
	 * @param betweenTime 查询时间范围
	 * @return 订单条数
	 */
	public long getDispatchedNums(String areaCode, String betweenTime);

	/**
	 * 得到指定站点当天(昨天)更新的所有订单 -- 把物流信息同步到mysql中时用到
	 * @param areaCode 站点编号
	 * @return 订单集合
	 */
	public List<Order> getTodayUpdateOrdersBySite(String areaCode);

	/**
	 * 查询指定mailNum集合的订单中物流状态不为expressStatus的订单的条数
	 * @param mailNumList mailNum集合
	 * @param expressStatus 物流状态
	 * @return 订单的条数
	 */
	public long getCounByMailNumsAndExpressStatus(BasicDBList mailNumList, ExpressStatus expressStatus);

	/**
	 * 分页查询运单号/手机号/姓名/地址四个字段中包含关键字（keyword）的运单
	 * @param pageIndex 当前页
	 * @param tradeNo 订单号
	 * @param uId 发件人
	 * @param keyword 搜索关键字
     * @return 分页对象（分页信息和数据）
     */
	public PageModel<Order> findPageOrders(Integer pageIndex, String tradeNo, ObjectId uId, String keyword);

	/**
	 * 根据商品订单号查询该订单下包含的运单数目
	 * @param tradeNo //商户订单号(我们自己生成的支付订单号)
	 * @return 订单下包含的运单数目
     */
	public long findCountByTradeNo(String tradeNo);


	/**
	 * 订单导入完成，生成区域码和运单号
	 * @param order
	 * @return
     */
	public Order afterImportDealWithOrder(Order order);
	public Order reduceMailNumWithOrder(Order order);
	public Order reduceAreaCodeWithOrder(Order order);

	public List<Order> findAllByTradeNo(String tradeNo);

	/**
	 * 查询指定mailNum集合的订单中物流状态不为expressStatus的订单的条数
	 * @param mailNumList mailNum集合
	 * @param orderStatusList 订单状态集合
	 * @return 订单的条数
	 */
	public long getCounByMailNumsAndOrderStatusList(BasicDBList mailNumList, List<OrderStatus> orderStatusList);

	String findBestSiteWithAddress(String address);

	public Site getSiteWithAddress(String address);

	String updateParcelWithOrder(Order order);

	List<Order> findByDateAdd(Date date);

	String findWayNameBySite(Site site);

	/**
	 * 根据orderNo或者mailNum查询订单
	 * @param keyword
	 * @return
     */
	Order findByOrderNoOrMailNum(String keyword);
}
