package com.bbd.saas.api.mysql;

import com.bbd.saas.models.Balance;
import com.bbd.saas.models.Postcompany;

/**
 * Created by huozhijie on 2016/6/2.
 */
public interface BalanceService {

    /**
     * 插入一个Balance数据
     * @param balance
     */
    void insertBalance(Balance balance);
}
