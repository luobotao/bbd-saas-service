package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.ExpressStatStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Description: 订单统计dao接口
 * @author: liyanlei
 */
public interface ExpressStatStationDao {
	/**
	 * 插入一条记录
	 * @param orderLog
	 * @return
     */
	int insert(ExpressStatStation orderLog);
	/**
	 * 根据站点编号和时间进行查询
	 * @param areaCode 站点编号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> selectByAreaCodeAndTime(@Param("areaCode") String areaCode, @Param("startDate") String startDate, @Param("endDate") String endDate);

}
