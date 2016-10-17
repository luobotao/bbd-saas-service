package com.bbd.saas.dao.mysql;

import java.util.Map;


public interface PushDao {

    /**
     * 订单已完成push消息
     */
    void tradePush( Map<String, Object> parms );
    /**
     * 揽件员收益
     * @param parms
     */
    void courierAdd(Map<String, Object> parms );
}
