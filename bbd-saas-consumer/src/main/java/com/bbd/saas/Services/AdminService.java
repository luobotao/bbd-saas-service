package com.bbd.saas.Services;

import com.bbd.saas.constants.Constants;
import com.bbd.saas.mongoModels.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * adminUser with redis
 * Created by luobotao on 2016/4/8.
 */
@Service("adminService")
public class AdminService {

    @Autowired
    RedisTemplate<String, AdminUser> redisTemplate;


    public void put(AdminUser adminUser) {
        redisTemplate.opsForHash().put(Constants.REDIS_ADMIN_ID, adminUser.getId().toHexString(), adminUser);
    }

    public void delete(AdminUser adminUser) {
        redisTemplate.opsForHash().delete(Constants.REDIS_ADMIN_ID, adminUser.getId());
    }

    public AdminUser get(Object key) {
        return (AdminUser) redisTemplate.opsForHash().get(Constants.REDIS_ADMIN_ID, key);
    }
}
