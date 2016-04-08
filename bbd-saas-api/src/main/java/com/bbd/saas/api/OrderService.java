package com.bbd.saas.api;

import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.Order;

/**
 * Created by luobotao on 2016/4/8.
 * 管理员接口
 */
public interface OrderService {
    /**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    Order findAdminUserByUserName(String userName);
}
