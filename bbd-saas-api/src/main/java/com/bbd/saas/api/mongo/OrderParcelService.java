package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.ParcelStatus;
import com.bbd.saas.mongoModels.OrderParcel;

import java.util.List;

/**
 * Created by luobotao on 2016/4/13.
 * 包裹接口
 */
public interface OrderParcelService {
	/**
	 * 根据ID获取包裹信息
	 * @param id ID
	 * @return 包裹
	 */
	OrderParcel findById(String id);

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
	 * @param parceltyp 包裹类型 0：配件包裹（默认） 1：集包
	 * @return 包裹
	 */
	OrderParcel findOrderParcelByOrderIdAndParcelType(String orderId,String parceltyp);

	/**
	 * 根据订单的运单号查询该运单号所处的包裹
	 * @param mailNum
	 * @return
	 */
	OrderParcel findOrderParcelByMailNum(String mailNum);

	/**
	 * 根据站点编码和包裹状态获取包裹列表
	 * @param areaCode
	 * @param parcelStatus
	 * @return
	 */
	List<OrderParcel> findOrderParcelsByAreaCodeAndStatus(String areaCode, ParcelStatus parcelStatus);
	/**
	 * 根据来源、站点编码和包裹状态获取包裹列表
	 * @param areaCode
	 * @param src
	 * @param parcelStatus
	 * @return
	 */
	List<OrderParcel> findOrderParcelsByAreaCodeAndStatusAndSrc(String areaCode, String src,ParcelStatus parcelStatus);
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

	/**
	 * APP端获取包裹到站列表
	 * @param uid 站长id
	 * @param offset  跳过的条数
	 * @param pagesize 查询的数据条数
     * @return 包裹列表
     */
	List<OrderParcel> findStagionParcelList(String uid,int offset, int pagesize);
}
