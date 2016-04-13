package com.bbd.saas.api.impl;


import com.bbd.saas.dao.OrderParcelDao;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.vo.OrderQueryVO;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.Key;
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
    private OrderParcelDao orderParcelDao;

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

	public PageModel<Order> findOrders( PageModel<Order> pageModel,OrderQueryVO orderQueryVO){
		if(orderQueryVO!=null){
			if(StringUtils.isNotBlank(orderQueryVO.mailNum)){
				return orderDao.findOrders(pageModel,orderQueryVO);
			}else{
				if(StringUtils.isNotBlank(orderQueryVO.parcelCode)){
					OrderParcel orderParcel = orderParcelDao.findOrderParcelByParcelCode(orderQueryVO.parcelCode);
					if(orderParcel!=null){
						pageModel.setDatas(orderParcel.getOrderList());
						pageModel.setPageNo(0);
						pageModel.setPageSize(orderParcel.getOrderList().size());
						pageModel.setTotalCount(Long.valueOf(orderParcel.getOrderList().size()));
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
	public Order findOneByMailNum(String mailNum) {
		return orderDao.findOne("mailNum", mailNum);
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
}
