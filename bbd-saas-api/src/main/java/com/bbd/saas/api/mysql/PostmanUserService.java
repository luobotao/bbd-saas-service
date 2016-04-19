package com.bbd.saas.api.mysql;

import com.bbd.saas.models.Postcompany;
import com.bbd.saas.models.PostmanUser;

import java.util.List;

/**
 * 公司Service
 * Created by zuowenhai on 2016/4/16.
 */
public interface PostmanUserService {

	/**
     * 获取所有用户列表
     * @param 
     * @return
     */
    List<PostmanUser> selectAll();
    
    int insertUser(PostmanUser postmanUser);
}
