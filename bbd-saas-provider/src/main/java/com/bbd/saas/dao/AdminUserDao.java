package com.bbd.saas.dao;


import com.bbd.saas.mongoModels.AdminUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Repository
public class AdminUserDao {
    public static final Logger logger = LoggerFactory.getLogger(AdminUserDao.class);


    /**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    public AdminUser findAdminUserByUserName(String userName){
        AdminUser adminUser = new AdminUser();
        adminUser.setAppkey("123123");
        adminUser.setPassWord("test");


        return adminUser;
    }
}
