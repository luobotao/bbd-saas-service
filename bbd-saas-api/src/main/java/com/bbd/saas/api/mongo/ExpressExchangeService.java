package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.ExpressExchange;

import org.mongodb.morphia.Key;

/**
 * Created by huozhijie on 2016/6/15.
 */

public interface ExpressExchangeService {
    /**
     * 保存一个expressExchange对象
     * @param expressExchange
     * @return
     */
    Key<ExpressExchange> save(ExpressExchange expressExchange);
}
