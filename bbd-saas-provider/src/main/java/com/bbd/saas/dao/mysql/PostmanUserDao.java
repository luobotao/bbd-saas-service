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
     * 根据用户ID获取此用户
     * @param id
     * @return
     */
    PostmanUser findById(@Param("id")Integer id);
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
    int updateByPhone(@Param("pm")PostmanUser postmanUser, @Param("oldPhone")String oldPhone);
    
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
     * 更新派件员的角色（权限）
     * @param phone 派件员手机号
     * @param postrole postrole
     * @return 更新条数
     */
    int updateRoleByPhone(@Param("phone") String phone, @Param("postrole") Integer postrole);
    
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
     List<Map<String, Object>> selectLatAndLngByCompanyId(@Param("companyId") String companyId);

    /**
     * 根据Id集合查询所有派件员的经纬度
     * @param ids id的集合
     * @return
     */
     List<Map<String, Object>> selectLatAndLngByIds(@Param("ids") List<Integer> ids);

     List<Map<String, Object>> getIntegral(Map<String, Object> map);

    /**
     * 删除电话为phone,id不为id的postmanUser
     * @param phone
     * @param id
     */
     int deleteByPhoneAndId(@Param("phone")String phone, @Param("id")int id);
    /**
     * 根据站点Id更新站点名称（substation）
     * @param siteid 站点id
     * @param siteName 站点名称
     * @return
     */
    int updateSubstationBySiteId(@Param("siteid") String siteid, @Param("siteName") String siteName);

    /**
     * 根据用户token获取此用户
     * @param token
     * @return
     */
    PostmanUser findByToken(@Param("token") String token);
    List<PostmanUser> findPostmanUsers(String sql);

    void pushBbdTrade(Map<String, Object> map);

    List<PostmanUser> findAllByAreaCode(@Param("areaCode") String areaCode);
 }
