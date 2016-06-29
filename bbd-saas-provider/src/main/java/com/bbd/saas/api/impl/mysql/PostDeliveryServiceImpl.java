package com.bbd.saas.api.impl.mysql;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbd.saas.api.mysql.PostDeliveryService;
import com.bbd.saas.dao.mysql.PostDeliveryDao;
import com.bbd.saas.models.PostDelivery;

/**
 * Description: 快递员派送运单信息Service实现
 * @author: liyanlei
 * 2016年4月20日下午3:57:47
 */
@Service("postDeliveryService")
@Transactional
public class PostDeliveryServiceImpl implements PostDeliveryService {
	@Resource
	private PostDeliveryDao postDeliveryDao;

	public PostDeliveryDao getPostDeliveryDao() {
		return postDeliveryDao;
	}

	public void setPostDeliveryDao(PostDeliveryDao postDeliveryDao) {
		this.postDeliveryDao = postDeliveryDao;
	}

	@Override
	public int insert(PostDelivery postDelivery) {
		return postDeliveryDao.insert(postDelivery);
	}

	@Override
	public int updatePostAndStatusAndCompany(String mailNum,Integer postManId, 
			String staffId, String status, String company_code) {
		if (mailNum == null) {
			return 0;
		}
		return postDeliveryDao.updatePostAndStatusAndCompany(mailNum,
				postManId, staffId, status, company_code, new Date());
	}

	@Override
	public int findCountByMailNum(String mailNum) {
		return postDeliveryDao.selectCountByMailNum(mailNum);
	}

	@Override 
	public int deleteByMailNum(String mailNum) {
		return postDeliveryDao.deleteByMailNum(mailNum);
	}

	/**
	 * 修改运单的状态
	 * @param mailNum
	 * @param sta //（[0:全部，服务器查询逻辑],1：未完成，2：已签收，3：已滞留，4：已拒绝，5：已退单 8：丢失
	 * @param remark
	 * @param resultMsg
	 */
	@Override
	public void updatePostDeliveryStatus(String mailNum, String sta,String remark,String resultMsg){
		postDeliveryDao.updatePostDeliveryStatus(mailNum,sta,remark,resultMsg);
	}
}
