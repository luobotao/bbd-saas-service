package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.OrderParcel;

import java.util.List;

/**
 * Created by luobotao on 2016/4/13.
 * 包裹接口
 */
public interface OrderParcelService {


	/**
	 * 根据订单ID获取此订单所处的包裹号
	 * @param orderId 订单ID
	 * @return parcelCode包裹号
	 */
    String findParcelCodeByOrderId(String orderId);

	/**
	 * 根据站点编码和包裹号获取包裹
	 * @param areaCode 站点编码
	 * @param parcelCode 包裹号
	 * @return 包裹
	 */
	OrderParcel findOrderParcelByParcelCode(String areaCode,String parcelCode);

	/**
	 * 根据订单ID获取此订单所处的包裹信息
	 * @param orderId 订单ID
	 * @return 包裹
	 */
	OrderParcel findOrderParcelByOrderId(String orderId);


	/**
	 * 保存包裹
	 * @param orderParcel 包裹对象
	 */
	void saveOrderParcel(OrderParcel orderParcel);

	/**
	 * 根据运单号查询所有的包裹 -- 用于根据运单号获取所有关联的站点
	 * @param trackNo 运单号
	 * @return 包裹集合
	 */
	List<OrderParcel> findOrderParcelListByTrackCode(String trackNo);


}
