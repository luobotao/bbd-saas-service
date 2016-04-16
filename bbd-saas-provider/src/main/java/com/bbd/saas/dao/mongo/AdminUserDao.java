package com.bbd.saas.dao.mongo;


import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.AdminUser;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;


/**
 * Created by luobotao on 2016/4/1.
 */
@Repository
public class AdminUserDao extends BaseDAO<AdminUser, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(AdminUserDao.class);

    AdminUserDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

}
