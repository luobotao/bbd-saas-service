package com.bbd.saas.api.mongo;

import java.util.List;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;
import com.bbd.saas.vo.UserVO;
import com.mongodb.WriteResult;
import org.mongodb.morphia.query.UpdateResults;

/**
 * Created by luobotao on 2016/4/11.
 * 站点用户接口
 */
public interface UserService {

	
    /**
     * Description: 根据用户id查询
     * @param userId 用户id
     * @return
     * @author: liyanlei
     * 2016年4月12日下午6:10:40
     */
    User findOne(String userId);

    /**
     * 根据用户名检索站点用户
     * @param loginName
     * @return
     */
    
    User findUserByLoginName(String loginName);
    
    /**
     * 根据user对象删除站点用户
     * @param user
     * @return WriteResult
     */
    public void delUser(User user);
    
    
    /**
     * 根据用户名检索站点用户
     * @param realName
     * @return
     */
    User findUserByRealName(String realName);
    
    /**
     * 保存用户对象信息
     * @param user
     * @return Key<User>
     */
    Key<User> save(User user);
    
    /**
     * 获取用户列表信息
     * @param pageModel
     * @return PageModel<User>
     */
    public PageModel<User> findUserList(PageModel<User> pageModel,UserQueryVO userQueryVO,Site site);
    
    /**
     * Description: 获取指定站点下的所有派件员
     * @param areaCode 站点编号
     * @return
     * @author: liyanlei
     * 2016年4月16日下午5:22:17
     */
    public List<UserVO> findUserListBySite(String areaCode);
    /**
     * Description: 获取指定站点下的所有派件员
     * @param site 站点
     * @return
     * @author: liyanlei
     * 2016年4月16日下午5:22:17
     */
    public List<UserVO> findUserListBySite(Site site);
    
    /**
     * 根据site和staffid查找是该staffid是否在该站点已存在
     * @param site、staffid
     * @return User
     */
    public User findOneBySiteByStaffid(Site site, String staffid);
    
    /**
     * 更新用户状态
     * @param loginName 、UserStatus
     * return UpdateResults
     */
    public void updateUserStatu(String loginName, UserStatus userStatus);

    /**
     * 根据用户名（手机号）去更新此用户的公司ID
     * @param companyId
     * @param phone
     */
    void updateCompanyIdByLoginName(int companyId, String phone);

    /**
     * 删除此站点下的所有用户
     * @param siteId
     */
    void delUsersBySiteId(String siteId);
}
