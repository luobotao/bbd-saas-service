package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.OrderParcelService;
import com.bbd.saas.dao.mongo.OrderParcelDao;
import com.bbd.saas.mongoModels.OrderParcel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luobotao on 2016/4/13.
 * 包裹接口
 */
@Service("orderParcelService")
public class OrderParcelServiceImpl implements OrderParcelService {
	
    private OrderParcelDao orderParcelDao;

	public OrderParcelDao getOrderParcelDao() {
		return orderParcelDao;
	}

	public void setOrderParcelDao(OrderParcelDao orderParcelDao) {
		this.orderParcelDao = orderParcelDao;
	}

	/**
	 * 根据订单ID获取此订单所处的包裹号
	 * @param orderId
	 * @return
     */
	@Override
	public String findParcelCodeByOrderId(String orderId){
      return orderParcelDao.findParcelCodeByOrderId(orderId);
    }

	/**
	 * 根据站点编码和包裹号获取包裹
	 * @param areaCode 站点编码
	 * @param parcelCode
     * @return
     */
	@Override
	public OrderParcel findOrderParcelByParcelCode(String areaCode,String parcelCode) {
		return orderParcelDao.findOrderParcelByParcelCode(areaCode,parcelCode);
	}

	/**
	 * 根据订单ID获取此订单所处的包裹信息
	 * @param orderId
	 * @return
     */
	@Override
	public OrderParcel findOrderParcelByOrderId(String orderId) {
		return orderParcelDao.findOrderParcelByOrderId(orderId);
	}

	/**
	 * 保存包裹
	 * @param orderParcel
     */
	@Override
	public void saveOrderParcel(OrderParcel orderParcel) {
		orderParcelDao.save(orderParcel);
	}

	/**
	 * 根据运单号获取所有的站点集合
	 * @param trackNo
	 * @return
     */
	@Override
	public List<OrderParcel> findOrderParcelListByTrackCode(String trackNo) {
		List<OrderParcel> orderParcelList =  orderParcelDao.findOrderParcelListByTrackCode(trackNo);
		return orderParcelList;
	}
}
