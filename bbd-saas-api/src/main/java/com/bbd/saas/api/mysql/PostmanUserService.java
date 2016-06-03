package com.bbd.saas.api.mysql;

import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.vo.UserVO;

import java.util.List;

/**
 * 公司Service
 * Created by zuowenhai on 2016/4/16.
 */
public interface PostmanUserService {

    /**
     * 获取所有派件员列表
     * @return 派件员列表
     */
    List<PostmanUser> selectAll();
    /**
     * 保存postmanUser
     * @param postmanUser 派件员对象
     * @return  派件员对象
     */
    PostmanUser insertUser(PostmanUser postmanUser);
    /**
     * 根据phone获取对应的postmanUser
     * @param phone 手机号
     * @return PostmanUser
     */
    PostmanUser selectPostmanUserByPhone(String phone);

    /**
     * 根据phone获取对应的postmanUser的id
     * @param phone 手机号
     * @return id
     */
    int selectIdByPhone(String phone);

    /**
     * 更新postmanUser
     * @param postmanUser 派件员
     * @return 更新条数
     */
    int updateByPhone(PostmanUser postmanUser);

    /**
     * 删除postmanUser
     * @param id 编号
     */
    public void deleteById(Integer id);

    /**
     * 根据id更新postmanUser的状态
     * @param status 状态
     * @param id id
     * @return 更新条数
     */
    int updateById(Integer status,Integer id);

    /**
     * 根据id更新postmanUser的昵称
     * @param nickname 昵称
     * @param id id
     * @return 更新条数
     */
    int updatePostmanUserById(String nickname,Integer id);
    /**
     * 根据公司Id查询所有派件员的经纬度
     * @param companyId 公司Id
     * @return 派件员VO集合
     */
    public List<UserVO> findLatAndLngByCompanyId(String companyId);

    /**
     * 根据派件员id集合查询所有派件员的经纬度
     * @param ids 派件员id集合
     * @return 派件员VO集合
     */
    public List<UserVO> findLatAndLngByIds(List<Integer> ids);

}
