package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.Postcompany;
import com.bbd.saas.models.PostmanUser;

import java.util.List;

import org.apache.ibatis.annotations.Param;


public interface OpenUserDao {
    
	
	/**
     * 根据code查找对应的token
     * @param code
     * @return token
     */
    public String selectTokenByCode(@Param("code") String code);
}
