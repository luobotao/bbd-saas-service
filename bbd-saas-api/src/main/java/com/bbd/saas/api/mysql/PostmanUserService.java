package com.bbd.saas.api.mysql;

import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.UserVO;

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
    PostmanUser insertUser(PostmanUser postmanUser);
    /**
     * 根据phone获取对应的postmanUser
     * @param phone
     * @return PostmanUser
     */
    PostmanUser selectPostmanUserByPhone(String phone);
    
    /**
     * 根据phone获取对应的postmanUser的id
     * @param phone
     * @return id
     */
    int selectIdByPhone(String phone);
    
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
    
    /**
     * 更新postmanUser
     * @param sta、id
     * @return 
     */
    int updateById(Integer status,Integer id);
    
    /**
     * 更新postmanUser
     * @param nickname、id
     * @return 
     */
    int updatePostmanUserById(String nickname,Integer id);
    /**
     * 根据公司Id查询所有派件员的经纬度
     * @param companyId 公司Id
     * @return
     */
    public List<UserVO> findLatAndLngByCompanyId(String companyId);

    /**
     * 根据派件员id集合查询所有派件员的经纬度
     * @param ids 派件员id(id1,id2,id3---)
     * @return
     */
    public List<UserVO> findLatAndLngByIds(List<Integer> ids);

}
