package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.Complaint;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.ComplaintQueryVO;
import com.bbd.saas.vo.entity.ComplaintVO;

/**
 * Created by liyanlei on 2016/9/27.
 * 投诉接口
 */
public interface ComplaintService {
	/**
	 * 带查询条件去检索订单
	 * @param pageModel 分页对象
	 * @param complaintQueryVO 查询条件
	 * @return 分页对象（分页信息和数据）
	 */
	PageModel<ComplaintVO> findPageDatas(PageModel<Complaint> pageModel, ComplaintQueryVO complaintQueryVO);
}
