package com.bbd.saas.dao.mysql;

import java.util.Date;

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
	 * Description: 更新运单状态和派件员
	 * @param mailNum 查询指定运单
	 * @param postManId 派件员id
	 * @param staffId 派件员员工id
	 * @param status 运单状态
	 * @param company_code 站点公司号 ,=null:不更新此字段；="":更新此字段为""。
	 * @param dateUpd 更新时间
	 * @return
	 * @author liyanlei
	 * 2016年4月22日上午11:23:54
	 */
	int updatePostAndStatusAndCompany(@Param("mailNum") String mailNum,
			@Param("postManId") Integer postManId,
			@Param("staffId") String staffId, @Param("status") String status,
			@Param("company_code") String company_code,
			@Param("dateUpd") Date dateUpd);

	/**
	 * Description: 查询指定运单号的记录的条数
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月21日上午10:13:02
	 */
	int selectCountByMailNum(@Param("mailNum") String mailNum);
	
	/**
	 * Description: 根据运单号删除记录
	 * @param mailNum 运单号 
	 * @return
	 * @author liyanlei
	 * 2016年4月23日上午11:18:12
	 */
	int deleteByMailNum(@Param("mailNum") String mailNum);

}
