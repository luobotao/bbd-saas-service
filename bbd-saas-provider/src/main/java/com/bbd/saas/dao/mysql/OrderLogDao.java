package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.OrderLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * Description: 订单物流日志表dao接口
 * @author: liyanlei
 * 2016年4月20日下午2:31:03
 */
public interface OrderLogDao {
	/**
	 * 插入一条记录
	 * @param orderLog
	 * @return
     */
	int insert(OrderLog orderLog);
	/**
	 * 根据站点和时间统计不同状态的订单数目
	 * @param areaCode 站点编号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<Map<String, Object>> selectCountByAreaCodeAndTime(@Param("areaCode") String areaCode, @Param("startDate") String startDate, @Param("endDate") String endDate);

}
