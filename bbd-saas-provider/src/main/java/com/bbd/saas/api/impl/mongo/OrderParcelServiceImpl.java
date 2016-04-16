package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.OrderPacelService;
import com.bbd.saas.dao.mongo.OrderParcelDao;
import com.bbd.saas.mongoModels.OrderParcel;
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

	@Override
	public OrderParcel findOrderParcelByParcelCode(String areaCode,String parcelCode) {
		return orderParcelDao.findOrderParcelByParcelCode(areaCode,parcelCode);
	}


}
