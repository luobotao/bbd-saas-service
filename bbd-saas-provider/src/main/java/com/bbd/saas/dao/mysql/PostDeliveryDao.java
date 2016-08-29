package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.PostDelivery;
import com.bbd.saas.vo.PostDeliveryQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


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
	/**
	 * 修改运单的状态
	 * @param mailNum
	 * @param sta //（[0:全部，服务器查询逻辑],1：未完成，2：已签收，3：已滞留，4：已拒绝，5：已退单 8：丢失
	 * @param resultMsg
	 */
	void updatePostDeliveryStatus(@Param("mailNum")String mailNum,@Param("sta") String sta,@Param("remark") String remark,@Param("resultMsg") String resultMsg);

	/**
	 * 根据运单号查询
	 * @param mailNum 运单号
	 * @return 记录集合
     */
	List<PostDelivery> selectListByMailNum(@Param("mailNum")String mailNum);
	/**
	 * 根据id查询
	 * @param id 编号
	 * @return 实体
	 */
	public PostDelivery selectOneById(@Param("id")Integer id);

	/**
	 * Description: 更新一条记录
	 * @param postDelivery 快递配送实体类
	 * @author: liyanlei
	 */
	void updateOne(PostDelivery postDelivery);

	/**
	 * 根据站点和时间查询记录条数
	 * @param siteId 站点id
	 * @param tim 日期
     * @return 符合条件的记录数
     */
	public int selectCountBySiteIdAndTim(@Param("siteId")String siteId, @Param("tim")String tim);

	/**
	 * 根据查询条件查询数据
	 * @param postDeliveryQueryVO 查询条件
	 * @return 符合条件的数据
	 */
	public List<Map<String, Object>> selectListByQuery(PostDeliveryQueryVO postDeliveryQueryVO);

	/**
	 * 根据查询条件查询数据条数
	 * @param postDeliveryQueryVO 查询条件
	 * @return 符合条件的数据条数
	 */
	public int selectCountByQuery(PostDeliveryQueryVO postDeliveryQueryVO);

	/**
	 * 根据查询条件分页查询数据
	 * @param postDeliveryQueryVO 查询条件
	 * @param startNum  跳过的条数
	 * @param pageSize 查询的条数
	 * @return 分页数据
	 */
	public List<PostDelivery> selectPageByQuery(@Param("queryVo")PostDeliveryQueryVO postDeliveryQueryVO, @Param("startNum")Integer startNum, @Param("pageSize")Integer pageSize) throws Exception;


}
