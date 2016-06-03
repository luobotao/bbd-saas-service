package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.ReturnReason;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;


/**
 * Created by liyanlei on 2016/6/1.
 * 退货原因DAO
 */
@Repository
public class ReturnReasonDao extends BaseDAO<ReturnReason, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(ReturnReasonDao.class);

    ReturnReasonDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }
}
