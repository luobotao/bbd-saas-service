package com.bbd.saas.api.mysql;

import com.bbd.saas.models.ExpressStatStation;

import java.util.List;

/**
 * Description: 订单统计操作接口
 * @author: liyanlei
 * 2016年5月9日下午5:53:06
 */
public interface ExpressStatStationService {
	/**
	 * 插入一条记录
	 * @param expressStatTotal 订单统计
	 * @return 插入条数
	 */
	int insert(ExpressStatStation expressStatTotal);
	/**
	 * 根据站点编号和时间进行统计查询
	 * @param areaCode 站点编号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> findByAreaCodeAndTime(String areaCode, String startDate, String endDate);

}
