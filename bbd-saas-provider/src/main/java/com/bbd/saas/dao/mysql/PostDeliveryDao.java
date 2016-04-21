package com.bbd.saas.dao.mysql;

import org.apache.ibatis.annotations.Param;

import com.bbd.saas.models.PostDelivery;


/**
 * Description: 快递员派送运单信息
 * @author: liyanlei
 * 2016年4月20日下午2:31:03
 */
public interface PostDeliveryDao {
	
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
	int updatePostIdAndStaffId(@Param("mailNum") String mailNum, @Param("postManId") Integer postManId, @Param("staffId") String staffId);
	
	/**
	 * Description: 查询指定运单号的记录的条数
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月21日上午10:13:02
	 */
	int selectCountByMailNum(@Param("mailNum") String mailNum);

}
