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
	 * @return 插入条数
	 * @author: liyanlei
	 * 2016年4月20日下午2:36:25
	 */
	public int insert(PostDelivery postDelivery);

	/**
	 * 通过运单号更新派件员信息、运单状态、站点公司号
	 * @param mailNum 运单号
	 * @param postManId 派件员Id
	 * @param staffId 派件员员工Id
	 * @param status 运单状态
	 * @param company_code 站点公司号
	 * @return 更新记录数目
	 * @author liyanlei
	 * 2016年4月22日上午11:56:46
	 */
	public int updatePostAndStatusAndCompany(String mailNum, Integer postManId, String staffId, String status, String company_code);

	/**
	 * 查询指定运单号的记录的条数
	 * @param mailNum 运单号
	 * @return 记录条数
	 * @author: liyanlei
	 * 2016年4月21日上午10:39:51
	 */
	public int findCountByMailNum(String mailNum);

	/**
	 * 根据运单号删除记录
	 * @param mailNum 运单号
	 * @return 删除记录数目
	 * @author liyanlei
	 * 2016年4月23日上午11:18:12
	 */
	public int deleteByMailNum(String mailNum);

	/**
	 * 修改运单的状态
	 * @param mailNum
	 * @param sta //（[0:全部，服务器查询逻辑],1：未完成，2：已签收，3：已滞留，4：已拒绝，5：已退单 8：丢失
	 * @param remark
	 * @param resultMsg
	 */
	public void updatePostDeliveryStatus(String mailNum, String sta,String remark,String resultMsg);

	/**
	 * 根据id查询
	 * @param id 编号
	 * @return 实体
     */
	public PostDelivery findOneById(Integer id);

	/**
	 * 更新
	 * @param postDelivery  快递员派送运单
	 * @return  快递员派送运单
	 */
	public PostDelivery updateOne(PostDelivery postDelivery);
	/**
	 * 数据详情
	 * @param siteId,tim
	 * @return
	 */
	public int getDeliveryCnt(String siteId, String tim);
}
