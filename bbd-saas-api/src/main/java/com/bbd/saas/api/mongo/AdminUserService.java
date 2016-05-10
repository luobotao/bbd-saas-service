package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.AdminUser;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public interface AdminUserService {
    /**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    AdminUser findAdminUserByUserName(String userName);


    /**
     * 根据商户
     * @param id
     * @return
     */
    AdminUser findOne(String id);
}
