package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.Postcompany;
import com.bbd.saas.models.PostmanUser;

import java.util.List;


public interface PostmanUserDao {
    
	
	/**
     * 获取所有用户列表
     * @param 
     * @return
     */
    List<PostmanUser> selectAll();
    
    Integer insert(PostmanUser postmanUser);
}
