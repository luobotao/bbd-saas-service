package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.PostmanUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


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
    PostmanUser selectPostmanUserByPhone(@Param("phone") String phone, @Param("id")Integer id);
    /**
     * 根据phone获取对应的postmanUser的id
     * @param phone
     * @return id
     */
    int selectIdByPhone(@Param("phone") String phone);
    /**
     * 根据phone获取对应的postmanUser
     * @param postmanUser
     * @return List<PostmanUser>
     */
    Integer insertUser(PostmanUser postmanUser);
    
    /**
     * 更新postmanUser
     * @param postmanUser phone =oldPhone; staffid = newPhone
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
    int updateById(@Param("sta") Integer sta, @Param("id") Integer id);
    
    /**
     * 更新postmanUser
     * @param nickname、id
     * @return 
     */
    int updatePostmanUserById(@Param("nickname") String nickname, @Param("id") Integer id);

    /**
     * 根据公司Id查询所有派件员的经纬度
     * @param companyId 公司Id
     * @return
     */
    public List<Map<String, Object>> selectLatAndLngByCompanyId(@Param("companyId") String companyId);

    /**
     * 根据Id集合查询所有派件员的经纬度
     * @param ids id的集合
     * @return
     */
    public List<Map<String, Object>> selectLatAndLngByIds(@Param("ids") List<Integer> ids);

    public List<Map<String, Object>> getIntegral(Map<String, Object> map);
}
