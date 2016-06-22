package com.bbd.saas.api.mysql;

import com.bbd.saas.models.Balance;

/**
 * Created by huozhijie on 2016/6/2.
 */
public interface BalanceService {

    /**
     * 插入一个Balance数据
     * @param balance
     */
    void insertBalance(Balance balance);

    /**
     * 根据用户ID获取该用户的资金信息
     * @param uid
     * @return
     */
    Balance findBalanceByUid(int uid);
}
