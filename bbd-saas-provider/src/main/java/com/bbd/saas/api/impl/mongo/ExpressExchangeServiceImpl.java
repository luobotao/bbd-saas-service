package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.ExpressExchangeService;
import com.bbd.saas.dao.mongo.ExpressExchangeDao;
import com.bbd.saas.mongoModels.ExpressExchange;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huozhijie on 2016/6/15.
 */
public class ExpressExchangeServiceImpl implements ExpressExchangeService {
    public static final Logger logger = LoggerFactory.getLogger(ExpressExchangeServiceImpl.class);

    private ExpressExchangeDao expressExchangeDao;


    public ExpressExchangeDao getExpressExchangeDao() {
        return expressExchangeDao;
    }

    public void setExpressExchangeDao(ExpressExchangeDao expressExchangeDao) {
        this.expressExchangeDao = expressExchangeDao;
    }

    @Override
    public Key<ExpressExchange> save(ExpressExchange expressExchange) {
        return expressExchangeDao.save(expressExchange);
    }
}


