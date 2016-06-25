package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Constant;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;


/**
 * Created by liyanlei on 2016/6/13.
 * tradeDao
 */
@Repository
public class ConstantDao extends BaseDAO<Constant, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(ConstantDao.class);

    ConstantDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }
    /**
     * 根据常量名称删除
     * @param name 常量名称
     */
    public WriteResult deleteByName(String name) {
        Query<Constant> query = createQuery();
        query.filter("name", name);
        return deleteByQuery(query);
    }
}
