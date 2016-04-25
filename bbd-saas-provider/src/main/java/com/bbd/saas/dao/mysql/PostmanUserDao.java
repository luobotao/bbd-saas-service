package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.Postcompany;
import com.bbd.saas.models.PostmanUser;

import java.util.List;

import org.apache.ibatis.annotations.Param;


public interface PostmanUserDao {
    
	
	/**
     * 获取所有用户列表
     * @param 
     * @return
     */
    List<PostmanUser> selectAll();
    /**
     * 根据phone获取对应的postmanUser
     * @param phone
     * @return PostmanUser
     */
    PostmanUser selectPostmanUserByPhone(@Param("phone") String phone);
    /**
     * 根据phone获取对应的postmanUser的id
     * @param phone
     * @return id
     */
    int selectIdByPhone(@Param("phone") String phone);
    /**
     * 根据phone获取对应的postmanUser
     * @param phone
     * @return List<PostmanUser>
     */
    Integer insertUser(PostmanUser postmanUser);
    
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
    void deleteById(@Param("id") Integer id);
    
    /**
     * 更新postmanUser
     * @param sta、id
     * @return 
     */
    int updateById(@Param("sta") Integer sta,@Param("id") Integer id);
    
    /**
     * 更新postmanUser
     * @param nickname、id
     * @return 
     */
    int updatePostmanUserById(@Param("nickname") String nickname,@Param("id") Integer id);
}
