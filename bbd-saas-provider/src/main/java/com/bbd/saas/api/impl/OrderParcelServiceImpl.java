package com.bbd.saas.api.impl;


import com.bbd.saas.api.OrderPacelService;
import com.bbd.saas.api.OrderService;
import com.bbd.saas.dao.OrderDao;
import com.bbd.saas.dao.OrderParcelDao;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.utils.PageModel;
import org.springframework.stereotype.Service;

/**
 * Created by luobotao on 2016/4/13.
 * 包裹接口
 */
@Service("orderParcelService")
public class OrderParcelServiceImpl implements OrderPacelService {
	
    private OrderParcelDao orderParcelDao;

	public OrderParcelDao getOrderParcelDao() {
		return orderParcelDao;
	}

	public void setOrderParcelDao(OrderParcelDao orderParcelDao) {
		this.orderParcelDao = orderParcelDao;
	}

	public String findParcelCodeByOrderId(String orderId){
      return orderParcelDao.findParcelCodeByOrderId(orderId);
    }
    

}
