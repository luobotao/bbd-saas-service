package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.ToOtherSiteLog;
import org.mongodb.morphia.Key;

/**
 * Created by liyanlei on 2016/5/6.
 * 转站接口
 */
public interface ToOtherSiteLogService {
    /**
     * 保存转站日志
     * @param toOtherSiteLog 转站日志
     * @return
     */
    Key<ToOtherSiteLog> save(ToOtherSiteLog toOtherSiteLog);

    /**
     * 转站运单数统计
     * @param fromAreaCode 转站源站点
     * @param between 时间范围
     * @return long 转站运单数
     */
    public long countByFromAreaCodeAndTime(String fromAreaCode, String between);

}
