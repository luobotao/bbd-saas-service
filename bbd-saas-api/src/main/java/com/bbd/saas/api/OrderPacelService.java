package com.bbd.saas.api;

import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.utils.PageModel;

/**
 * Created by luobotao on 2016/4/13.
 * 包裹接口
 */
public interface OrderPacelService {


	/**
	 * 根据订单ID获取此订单所处的包裹号
	 * @param orderId
	 * @return
     */
    String findParcelCodeByOrderId(String orderId);

	/**
	 * 根据站点编码和包裹号获取包裹
	 * @param areaCode 站点编码
	 * @param parcelCode
	 * @return
     */
	OrderParcel findOrderParcelByParcelCode(String areaCode,String parcelCode);
}
