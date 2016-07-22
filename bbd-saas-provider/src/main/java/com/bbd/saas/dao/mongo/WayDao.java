package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Way;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by liyanlei on 2016/7/22.
 * 路线DAO
 */
@Repository
public class WayDao extends BaseDAO<Way, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(WayDao.class);

    WayDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }
    /**
     * 查询经过指定站点的路线的条数
     * @param siteId 站点Id
     * @return 路线的条数
     */
    public long selectWayBySiteId(String siteId){
        Query<Way> query = createQuery();
        if(StringUtils.isNotBlank(siteId)){
            query.filter("sites.siteId", siteId);
            return count(query);
        }
        return  0L;
    }

    /**
     * 查找一个站点下的所有路线
     * @param siteId
     * @return
     */
    public List<Way> findAllWayBySiteId(String siteId) {
        Query<Way> query = createQuery();
        if(StringUtils.isNotBlank(siteId)){
            query.filter("sites.siteId", siteId);
            return find(query).asList();
        }
        return null;
    }
}
