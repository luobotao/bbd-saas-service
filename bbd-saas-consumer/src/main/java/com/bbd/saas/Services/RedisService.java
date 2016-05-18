package com.bbd.saas.Services;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Set;

/**
 * adminUser with redis
 * Created by luobotao on 2016/5/5.
 */
@Service("redisService")
public class RedisService {
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    public boolean set(final byte[] key, final byte[] value,final long activeTime) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                boolean rs = true;
                connection.set(key, value);
                if (activeTime > 0) {
                    rs = connection.expire(key, activeTime);
                }
                return rs;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, long liveTime) {
        this.set(key.getBytes(), value.getBytes(), liveTime);
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        this.set(key, value, 0L);
    }


    /**
     * @param key
     * @return
     */
    public String get(String key) {
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] value = connection.get(key.getBytes());
                    return value == null ? "" : new String(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "";
            }
        });
    }

    public Set<String> matchKeys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    public boolean exists(final String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    public boolean flushDB() {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.flushDb();
                return true;
            }
        });
    }

    public long delete(final Collection<String> keys) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long result = 0;
                for (String key : keys) {
                    result = connection.del(key.getBytes());
                }
                return result;
            }
        });
    }

    public long delete(final String... keys) {
        Collection<String> cols = Lists.newArrayList();
        for (String key : keys) {
            cols.add(key);
        }
        return this.delete(cols);
    }





}
