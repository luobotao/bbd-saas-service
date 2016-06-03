package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.AdminUser;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public interface AdminUserService {
    /**
     * 根据用户名查找是否存在此管理员
     * @param userName 用户名称
     * @return 管理员对象
     */
    AdminUser findAdminUserByUserName(String userName);


    /**
     * 根据_id查询管理员
     * @param id mongodb自动生成的_id
     * @return 管理员对象
     */
    AdminUser findOne(String id);
}
