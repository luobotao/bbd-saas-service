package com.bbd.saas.api.mysql;

import com.bbd.saas.models.PostDelivery;

/**
 * Description: 快递员派送运单信息接口
 * @author: liyanlei
 * 2016年4月20日下午3:53:06
 */
public interface PostDeliveryService {
	/**
	 * Description: 插入一条记录
	 * @param postDelivery 快递配送实体类
	 * @return
	 * @author: liyanlei
	 * 2016年4月20日下午2:36:25
	 */
	int insert(PostDelivery postDelivery);
	
	/**
	 * Description: 通过运单号更新派件员信息
	 * @param mailNum 运单号
	 * @param postManId 派件员Id
	 * @param staffId 派件员员工Id(staffId用于查询postManId,postManId有值，staffId可以不存)
	 * @return
	 * @author: liyanlei
	 * 2016年4月20日下午2:38:35
	 */
	
	/**
	 * Description: 通过运单号更新派件员信息、运单状态、站点公司号
	 * @param mailNum 运单号
	 * @param postManId 派件员Id
	 * @param staffId 派件员员工Id
	 * @param status 运单状态
	 * @param company_code 站点公司号
	 * @return
	 * @author liyanlei
	 * 2016年4月22日上午11:56:46
	 */
	int updatePostAndStatusAndCompany(String mailNum, Integer postManId, String staffId, String status, String company_code);
	
	/**
	 * Description: 查询指定运单号的记录的条数
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月21日上午10:39:51
	 */
	int findCountByMailNum(String mailNum);
	
	/**
	 * Description: 根据运单号删除记录
	 * @param mailNum 运单号
	 * @return
	 * @author liyanlei
	 * 2016年4月23日上午11:18:12
	 */
	int deleteByMailNum(String mailNum); 
	
}
