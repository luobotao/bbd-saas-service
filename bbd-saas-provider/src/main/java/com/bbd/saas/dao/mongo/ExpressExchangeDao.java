package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.ExpressExchange;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

/**
 * Created by huozhijie on 2016/6/15.
 */
@Repository
public class ExpressExchangeDao  extends BaseDAO<ExpressExchange, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(ExpressExchangeDao.class);

    ExpressExchangeDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

}
