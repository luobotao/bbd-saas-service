package com.bbd.saas.api.impl;


import com.bbd.saas.api.AdminUserService;
import com.bbd.saas.dao.AdminUserDao;
import com.bbd.saas.mongoModels.AdminUser;
import org.springframework.stereotype.Service;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {
    private AdminUserDao adminUserDao;
    /**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    public AdminUser findAdminUserByUserName(String userName){
        return adminUserDao.findOne("userName",userName);
    }


    public AdminUserDao getAdminUserDao() {
        return adminUserDao;
    }

    public void setAdminUserDao(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }
}
