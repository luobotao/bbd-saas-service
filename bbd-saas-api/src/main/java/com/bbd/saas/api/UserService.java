package com.bbd.saas.api;

import java.util.List;

import org.mongodb.morphia.Key;

import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserVO;

/**
 * Created by luobotao on 2016/4/11.
 * 站点用户接口
 */
public interface UserService {


    /**
     * 根据用户名检索站点用户
     * @param loginName
     * @return
     */
    User findUserByLoginName(String loginName);
    
    /**
     * 根据用户名检索站点用户
     * @param loginName
     * @return
     */
    User findUserByRealName(String realName);
    
    /**
     * 根据用户id查找是否存在此用户
     * @param id
     * @return
     */
    User findUserById(String id); 
    
    /**
     * 保存用户对象信息
     * @param user
     * @return Key<User>
     */
    Key<User> save(User user);
    
    /**
     * 获取用户列表信息
     * @param PageModel<User>
     * @return PageModel<User>
     */
    public PageModel<User> findUserList(PageModel<User> pageModel);
    
    /**
     * Description: 获取指定站点下的所有用户
     * @param siteId
     * @return
     * @author: liyanlei
     * 2016年4月12日上午11:27:25
     */
    public List<UserVO> findUserListBySite(String siteId);
}
