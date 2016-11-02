package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.PostDeliverySmsLog;
import org.apache.ibatis.annotations.Param;


/**
 * Description: 运单分派是发送的短信日志Dao
 * @author: liyanlei
 * 2016/11/2 15:15
 */
public interface PostDeliverySmsLogDao {
	/**
	 * 插入一条记录
	 * @param postDeliverySmsLog 短信日志实体
	 * @return 插入条数
	 */
	public int insert(PostDeliverySmsLog postDeliverySmsLog);

	/**
	 * 查询指定运单号的记录的条数
	 * @param mailnum 运单号
	 * @return 记录条数
	 */
	public int selectCountByMailNum(@Param("mailnum")String mailnum);

	/**
	 * 根据id查询
	 * @param id 编号
	 * @return 实体
	 */
	public PostDeliverySmsLog selectOneById(@Param("id")Integer id);

	/**
	 * 根据运单号查询
	 * @param mailnum 运单号
	 * @return 实体
	 */
	public PostDeliverySmsLog selectOneByMailNum(@Param("mailnum")String mailnum);

}
