package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.PostDeliverySmsLogService;
import com.bbd.saas.dao.mysql.PostDeliverySmsLogDao;
import com.bbd.saas.models.PostDeliverySmsLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Description: 运单分派是发送的短信日志接口实现
 * @author: liyanlei
 * 2016/11/2 15:15
 */
@Service("PostDeliverySmsLogService")
@Transactional
public class PostDeliverySmsLogServiceImpl implements PostDeliverySmsLogService {
	@Resource
	private PostDeliverySmsLogDao postDeliverySmsLogDao;

	public PostDeliverySmsLogDao getPostDeliverySmsLogDao() {
		return postDeliverySmsLogDao;
	}

	public void setPostDeliverySmsLogDao(PostDeliverySmsLogDao postDeliverySmsLogDao) {
		this.postDeliverySmsLogDao = postDeliverySmsLogDao;
	}

	@Override
	public int add(PostDeliverySmsLog postDeliverySmsLog) {
		return this.postDeliverySmsLogDao.insert(postDeliverySmsLog);
	}

	@Override
	public int findCountByMailNum(String mailnum) {
		return this.postDeliverySmsLogDao.selectCountByMailNum(mailnum);
	}

	@Override
	public PostDeliverySmsLog findOneById(Integer id) {
		return this.postDeliverySmsLogDao.selectOneById(id);
	}

	@Override
	public PostDeliverySmsLog findOneByMailNum(String mailnum) {
		return this.postDeliverySmsLogDao.selectOneByMailNum(mailnum);
	}
}
