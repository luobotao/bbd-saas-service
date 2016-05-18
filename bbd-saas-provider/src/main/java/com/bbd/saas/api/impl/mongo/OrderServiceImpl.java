package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.dao.mongo.OrderDao;
import com.bbd.saas.dao.mongo.OrderParcelDao;
import com.bbd.saas.dao.mongo.UserDao;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.OrderUpdateVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateResults;

import java.util.List;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public class OrderServiceImpl implements OrderService {
	
    private OrderDao orderDao;
    private OrderParcelDao orderParcelDao;
    private UserDao userDao;

    public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public OrderParcelDao getOrderParcelDao() {
		return orderParcelDao;
	}

	public void setOrderParcelDao(OrderParcelDao orderParcelDao) {
		this.orderParcelDao = orderParcelDao;
	}

	public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

	/**
	 * 带查询条件去检索订单
	 * @param pageModel
	 * @param orderQueryVO
	 * @return
	 */
	public PageModel<Order> findOrders( PageModel<Order> pageModel,OrderQueryVO orderQueryVO){
		if(orderQueryVO!=null){
			if(StringUtils.isNotBlank(orderQueryVO.mailNum)){
				return orderDao.findOrders(pageModel,orderQueryVO);
			}else{
				if(StringUtils.isNotBlank(orderQueryVO.parcelCode)){
					OrderParcel orderParcel = orderParcelDao.findOrderParcelByParcelCode(orderQueryVO.areaCode,orderQueryVO.parcelCode);
					if(orderParcel!=null){
						List<Order> orderList = Lists.newArrayList();
						for(Order order:orderParcel.getOrderList()){
							Order orderTemp = orderDao.findOneByMailNum(order.getAreaCode(),order.getMailNum());
							if(orderTemp!=null)
								orderList.add(orderTemp);
						}
						pageModel.setDatas(orderList);
						pageModel.setPageNo(0);
						pageModel.setPageSize(orderList.size());
						pageModel.setTotalCount(Long.valueOf(orderList.size()));
					}
					return pageModel;
				}
			}
		}
		return orderDao.findOrders(pageModel,orderQueryVO);
	}


    /**
	 * Description: 根据运单号查询订单信息
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:38:38
	 */
	@Override
	public Order findOneByMailNum(String areaCode,String mailNum) {
		return orderDao.findOneByMailNum(areaCode,mailNum);
	}

	/**
     * Description: 保存订单
     * @param order
     * @return
     * @author: liyanlei
     * 2016年4月12日下午5:59:03
     */
	@Override
	public Key<Order> save(Order order) {
		return orderDao.save(order);
	}


	/**
	 * 根据站点编码获取该站点订单数据
	 * @param areaCode
	 * @return
	 */
	@Override
	public OrderNumVO getOrderNumVO(String areaCode) {
		return orderDao.getOrderNumVO(areaCode);
	}

	/**
	 * 更新订单状态
	 * 此处需要再加上包裹下的订单的状态更新
	 * @param mailNum 运单号
	 * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
	 * @param orderStatusNew
     */
	@Override
	public void updateOrderOrderStatu(String mailNum,OrderStatus orderStatusOld, OrderStatus orderStatusNew){
		orderDao.updateOrderOrderStatu(mailNum,orderStatusOld,orderStatusNew);//修改订单表里的状态
		orderParcelDao.updateOrderOrderStatu(mailNum,orderStatusOld,orderStatusNew);//修改包裹表里的订单的状态
	}

	@Override
	public PageModel<Order> findPageOrders(Integer pageIndex, OrderQueryVO orderQueryVO) {
		if(orderQueryVO == null){
			return null;
		}
		PageModel<Order> pageModel = new PageModel<Order>();
		pageModel.setPageNo(pageIndex);
		return orderDao.findPageOrders(pageModel, orderQueryVO);
	}

	@Override
	public List<Order> findOrders(OrderQueryVO orderQueryVO) {
		return orderDao.findOrders(orderQueryVO);
	}

	@Override
	public int updateOrder(OrderUpdateVO orderUpdateVO,
			OrderQueryVO orderQueryVO) {
		//派件员员工Id
    	UpdateResults r = orderDao.updateOrder(orderUpdateVO, orderQueryVO);
    	if(r != null){
    		return r.getUpdatedCount();
    	}
		return 0;
	}

	@Override
	public long getDispatchedNums(String areaCode, String betweenTime) {
		return  orderDao.getDispatchedNums(areaCode, betweenTime);
	}

	@Override
	public List<Order> getTodayUpdateOrdersBySite(String areaCode) {
		return orderDao.getTodayUpdateOrdersByAreaCode(areaCode);
	}

	@Override
	public long getNoArriveHis(String areaCode) {
		return orderDao.getNoArriveHis(areaCode);
	}
}
