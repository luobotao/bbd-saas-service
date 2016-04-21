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
    /**
     * 保存postmanUser
     * @param postmanUser
     * @return ret 成功或失败
     */
    int insertUser(PostmanUser postmanUser);
    /**
     * 根据phone获取对应的postmanUser
     * @param phone
     * @return PostmanUser
     */
    PostmanUser selectPostmanUserByPhone(String phone);
    
    /**
     * 更新postmanUser
     * @param postmanUser
     * @return 
     */
    int updateByPhone(PostmanUser postmanUser);
    
    /**
     * 删除postmanUser
     * @param id
     * @return 
     */
    public void deleteById(Integer id);
}
