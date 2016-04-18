package com.bbd.saas.api.impl.mysql;

import javax.annotation.Resource;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.dao.mysql.PostcompanyDao;
import com.bbd.saas.models.Postcompany;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公司Service实现
 * Created by luobotao on 2016/4/16.
 */
@Service("orderMysqlService")
@Transactional
public class PostcompanyServiceImpl implements PostcompanyService {
	@Resource
	private PostcompanyDao postcompanyDao;

	public PostcompanyDao getPostcompanyDao() {
		return postcompanyDao;
	}

	public void setPostcompanyDao(PostcompanyDao postcompanyDao) {
		this.postcompanyDao = postcompanyDao;
	}

	/**
	 * 获取所有公司列表
	 * @return
     */
	@Override
	@Transactional(readOnly = true)
	public List<Postcompany> selectAll() {
		return postcompanyDao.selectAll();
	}
}
