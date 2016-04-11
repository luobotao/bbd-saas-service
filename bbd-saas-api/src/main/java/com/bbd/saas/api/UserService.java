package com.bbd.saas.api;

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
}
