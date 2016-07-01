package com.bbd.saas.api.mysql;

import com.bbd.saas.models.ExpressStatStation;
import com.bbd.saas.utils.PageModel;

import java.util.List;
import java.util.Map;

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
	 * 根据公司Id和时间段进行统计查询 == 暂时没有用到
	 * @param companyId 公司Id
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> findByCompanyIdAndTimeBetween(String companyId, String startDate, String endDate);

	/**
	 * 根据站点编号和时间段进行统计查询 == 暂时没有用到
	 * @param areaCode 站点编号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> findByAreaCodeAndTimeBetween(String areaCode, String startDate, String endDate);

	/**
	 * 根据站点编号集合和时间段进行查询 == 暂时没有用到
	 * @param areaCodeList 站点编号集合
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<ExpressStatStation> findByAreaCodeListAndTimeBetween(List<String> areaCodeList,
			String startDate, String endDate);

	/**
	 * 根据公司Id和时间进行分页统计查询
	 * @param companyId 公司Id
	 * @param tim 时间
	 * @return 统计查询分页对象（分页信息 + 数据）
	 */
	public PageModel<ExpressStatStation> findPageByCompanyIdAndTime(String companyId, String tim, PageModel<ExpressStatStation> pageModel);

	/**
	 * 根据站点编号和时间进行查询
	 * @param areaCode 站点编号
	 * @param tim 时间
     * @return 统计查询集合
     */
	public List<ExpressStatStation> findByAreaCodeAndTime(String areaCode, String tim);

	/**
	 * 根据公司Id和时间进行统计查询 -- 用于导出
	 * @param companyId 公司id
	 * @param tim 时间
	 * @return 统计数据集合
	 */
	public List<ExpressStatStation> findByCompanyIdAndTime(String companyId, String tim);

	/**
	 * 根据公司Id和时间进行统计查询 -- 用于导出
	 * @param companyId 公司id
	 * @param tim 时间
	 * @return 统计数据集合
	 */
	public ExpressStatStation findSummaryByCompanyIdAndTime(String companyId, String tim);

	/**
	 * 根据公司Id和时间进行查询 == 根据站点进行分页
	 * @param pageIndex
	 * @param companyId
	 * @param time
     * @return
     */
	public PageModel<ExpressStatStation> findPageByCompanyIdAndTime(Integer pageIndex, String companyId, String time);

	/**
	 * 根据公司Id和时间进行统计查询 -- 用于导出
	 * @param companyId 公司id
	 * @param tim 时间
	 * @return 统计数据集合
	 */
	public Map<String, ExpressStatStation> findMapByCompanyIdAndTime(String companyId, String tim);
}
