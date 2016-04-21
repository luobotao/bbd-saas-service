package com.bbd.saas.api.impl.mysql;

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
	public int updatePostIdAndStaffId(String mailNum, Integer postManId,
			String staffId) {
		if(mailNum == null){
			return 0;
		}
		return postDeliveryDao.updatePostIdAndStaffId(mailNum, postManId, staffId);
	}

	@Override
	public int findCountByMailNum(String mailNum) {
		return postDeliveryDao.selectCountByMailNum(mailNum);
	}

}