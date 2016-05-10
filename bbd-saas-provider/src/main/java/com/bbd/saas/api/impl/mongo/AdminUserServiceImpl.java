package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.AdminUserService;
import com.bbd.saas.dao.mongo.AdminUserDao;
import com.bbd.saas.mongoModels.AdminUser;
import org.bson.types.ObjectId;
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

    @Override
    public AdminUser findOne(String id) {
        return adminUserDao.findOne("id",new ObjectId(id));
    }


    public AdminUserDao getAdminUserDao() {
        return adminUserDao;
    }

    public void setAdminUserDao(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }
}
