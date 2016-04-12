package com.bbd.saas.api;

import org.mongodb.morphia.Key;

import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;

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
}
