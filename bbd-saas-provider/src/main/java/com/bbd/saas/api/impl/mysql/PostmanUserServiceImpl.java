package com.bbd.saas.api.impl.mysql;

import javax.annotation.Resource;

import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.dao.mysql.PostcompanyDao;
import com.bbd.saas.dao.mysql.PostmanUserDao;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.models.PostmanUser;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公司Service实现
 * Created by zuowenhai on 2016/4/18.
 */
@Service("userMysqlService")
@Transactional
public class PostmanUserServiceImpl implements PostmanUserService {
	@Resource
	private PostmanUserDao postmanUserDao;

	public PostmanUserDao getPostmanUserDao() {
		return postmanUserDao;
	}

	public void setPostmanUserDao(PostmanUserDao postmanUserDao) {
		this.postmanUserDao = postmanUserDao;
	}


	/**
     * 获取所有用户列表
     * @param 
     * @return
     */
	@Override
	@Transactional(readOnly = true)
	public List<PostmanUser> selectAll() {
		return postmanUserDao.selectAll();
	}
	/**
     * 根据phone获取对应的postmanUser
     * @param phone
     * @return PostmanUser
     */
	public PostmanUser selectPostmanUserByPhone(String phone){
		return postmanUserDao.selectPostmanUserByPhone(phone);
	}
	/**
     * 保存postmanUser
     * @param postmanUser
     * @return ret 成功或失败
     */
	public int insertUser(PostmanUser postmanUser){
		return postmanUserDao.insertUser(postmanUser);
	}
}
