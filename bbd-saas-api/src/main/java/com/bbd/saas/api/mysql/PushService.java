package com.bbd.saas.api.mysql;


/**
 * 返现接口
 * Created by luobotao on 2016/5/25.
 */
public interface PushService {

    /**
     * 订单已完成push消息
     * @param uid 小件员Id
     * @param typ 类型 2
     * @param tradeNo 订单号
     */
    void tradePush(int uid, String typ, String tradeNo);

    /**
     * 揽件员收益
     * @param uid
     * @param mailNo
     */
    void courierAdd(int uid, String mailNo);


}
