package com.bbd.saas.dao;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by luobotao on 2016/4/1.
 * 站点DAO
 */
@Repository
public class SiteDao extends BaseDAO<Site, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(SiteDao.class);

    SiteDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }


}
