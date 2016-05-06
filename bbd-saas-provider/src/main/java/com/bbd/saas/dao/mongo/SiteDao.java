package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Site;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
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
    public List<Site> selectByCompanyCode(String companycode) {
    	Query<Site> query = createQuery();
    	query.filter("companycode", companycode);
    	return  find(query).asList();
    }
    public List<Site> selectByCompanyId(String companyId) {
        Query<Site> query = createQuery();
        query.filter("companyId", companyId);
        return  find(query).asList();
    }
}
