package com.bbd.saas.api.impl;


import com.bbd.saas.api.UserService;
import com.bbd.saas.dao.UserDao;
import com.bbd.saas.mongoModels.User;
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

    @Override
    public User findUserByLoginName(String loginName) {
        return userDao.findOne("loginName",loginName);
    }
}
