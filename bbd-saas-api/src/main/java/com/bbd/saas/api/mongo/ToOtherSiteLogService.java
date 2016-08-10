package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.ToOtherSiteLog;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 * Created by liyanlei on 2016/5/6.
 * 转站接口
 */
public interface ToOtherSiteLogService {
    /**
     * 保存转站日志
     * @param toOtherSiteLog 转站日志
     * @return 保存结果
     */
    Key<ToOtherSiteLog> save(ToOtherSiteLog toOtherSiteLog);

    /**
     * 根据转站站点和转站时间统计转站运单数
     * @param fromAreaCode 转站源站点
     * @param between 转站时间范围
     * @return long 转站运单数
     */
    public long countByFromAreaCodeAndTime(String fromAreaCode, String between);

    /**
     * 根据转站站点编号集合和转站时间统计转站运单数
     * @param fromAreaCodeList 转站源站点编号集合
     * @param between 转站时间范围
     * @return long 转站运单数
     */
    public long countByFromAreaCodesAndTime(List<String> fromAreaCodeList, String between);

}
