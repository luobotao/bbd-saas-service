package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Relation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

/**
 * Created by liyanlei on 2016/10/14.
 * 商户揽件员关连Dao
 */
@Repository
public class RelationDao extends BaseDAO<Relation, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(RelationDao.class);

    RelationDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

}
