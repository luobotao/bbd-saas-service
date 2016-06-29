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
	 * 根据公司Id和时间段进行统计查询  == 暂时没有用到
	 * @param companyId 公司Id
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> selectByCompanyIdAndTimeBetween(@Param("companyId") Integer companyId,
				@Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 * 根据站点编号和时间段进行查询 == 暂时没有用到
	 * @param areaCode 站点编号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> selectByAreaCodeAndTimeBetween(@Param("areaCode") String areaCode, @Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 *
	 * 根据站点编号集合和时间段进行查询 == 暂时没有用到
	 * @param areaCodeList 站点编号集合
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> selectByAreaCodeListAndTimeBetween(@Param("areaCodeList") List<String> areaCodeList,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 * 根据公司Id和时间进行分页统计查询
	 * @param companyId 公司ID
	 * @param tim 时间
	 * @param skip 跳过的记录的条数
	 * @param step 要查询的条数 == pageSize
     * @return
     */
	public List<ExpressStatStation> selectPageByCompanyIdAndTime(@Param("companyId") Integer companyId, @Param("tim") String tim, @Param("skip")int skip, @Param("step")int step);
	/**
	 * 根据公司Id和时间查询统计数据总数目
	 * @param companyId 公司ID
	 * @param tim 时间
	 * @return 统计数据总数目
	 */
	public int selectCountByCompanyIdAndTime(@Param("companyId") Integer companyId, @Param("tim") String tim);

	/**
	 * 根据站点编号和时间进行查询
	 * @param areaCode 站点编号
	 * @param tim 时间
     * @return 统计对象
     */
	public List<ExpressStatStation> selectByAreaCodeAndTime(@Param("areaCode") String areaCode, @Param("tim") String tim);

	/**
	 * 根据公司Id和时间查询各个站点的汇总信息
	 * @param companyId 公司ID
	 * @param tim 时间
	 * @return 各个站点的汇总信息
	 */
	public ExpressStatStation selectSummaryByCompanyIdAndTime(@Param("companyId") Integer companyId, @Param("tim") String tim);

}
