package com.bbd.saas.api.impl.mysql;

import javax.annotation.Resource;

import com.bbd.saas.api.mysql.OpenUserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.dao.mysql.OpenUserDao;
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
@Service("openUserMysqlService")
@Transactional
public class OpenUserServiceImpl implements OpenUserService {
	@Resource
	private OpenUserDao openUserDao;


	public OpenUserDao getOpenUserDao() {
		return openUserDao;
	}



	public void setOpenUserDao(OpenUserDao openUserDao) {
		this.openUserDao = openUserDao;
	}



	/**
     * 根据code查找对应的token
     * @param code
     * @return token
     */
    public String selectTokenByCode(String code){
    	return openUserDao.selectTokenByCode(code);
    }

}
