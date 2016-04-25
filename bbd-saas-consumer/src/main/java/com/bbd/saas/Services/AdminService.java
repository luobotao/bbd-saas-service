package com.bbd.saas.Services;

import com.bbd.saas.constants.Constants;
import com.bbd.saas.mongoModels.User;
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
    RedisTemplate<String, User> redisTemplate;


    public void put(User user) {
        redisTemplate.opsForHash().put(Constants.REDIS_ADMIN_ID, user.getId().toHexString(), user);
    }

    public void delete(User user) {
        redisTemplate.opsForHash().delete(Constants.REDIS_ADMIN_ID, user.getId());
    }

    public User get(Object key) {
        try {
            User user =  (User) redisTemplate.opsForHash().get(Constants.REDIS_ADMIN_ID, key);
            return user;
        }catch (Exception exception){
            return null;
        }

    }

}
