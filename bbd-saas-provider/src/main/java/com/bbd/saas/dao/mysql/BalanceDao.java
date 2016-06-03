package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.Balance;

/**
 * Created by huozhijie on 2016/6/2.
 */
public interface BalanceDao {
    /**
     * 插入一个balance
     * @param balance
     */
   int insert(Balance balance);

}
