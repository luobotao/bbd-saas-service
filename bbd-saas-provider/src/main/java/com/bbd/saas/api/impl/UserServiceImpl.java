package com.bbd.saas.api.impl;


import com.bbd.saas.api.UserService;
import com.bbd.saas.dao.UserDao;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;

import org.mongodb.morphia.Key;
import org.springframework.stereotype.Service;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Service("adminUserService")
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    public User findUserByLoginName(String loginName) {
        return userDao.findOne("loginName",loginName);
    }
    /**
     * 保存用户对象信息
     * @param user
     * @return Key<User>
     */
    public Key<User> save(User user){
    	return userDao.save(user);
    }
    /**
     * 获取用户列表信息
     * @param PageModel<User> 
     * @return PageModel<User> 
     */
    public PageModel<User> findUserList(PageModel<User> pageModel){
    	return userDao.findUserList(pageModel);
    }
}
