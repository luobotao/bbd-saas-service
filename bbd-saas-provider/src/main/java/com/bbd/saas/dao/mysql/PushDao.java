package com.bbd.saas.dao.mysql;

import java.util.Map;


public interface PushDao {

    /**
     * 订单已完成push消息
     */
    void tradePush( Map<String, Object> parms );
}
