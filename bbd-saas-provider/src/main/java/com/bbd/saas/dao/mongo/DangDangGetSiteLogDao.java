package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.DangDangGetSiteLog;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by liyanlei on 2016/11/09.
 * 当当运单获取目标站点的日志DAO
 */
@Repository
public class DangDangGetSiteLogDao extends BaseDAO<DangDangGetSiteLog, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(DangDangGetSiteLogDao.class);

    DangDangGetSiteLogDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    public List<DangDangGetSiteLog> selectListByOrderIds(String [] orderIds) {
        Query<DangDangGetSiteLog> query = createQuery();
        query.filter("orderId <>", null).filter("orderId <>", "");//运单号不能为空
        if (orderIds != null && orderIds.length > 0) {
            query.filter("orderId in", orderIds);
        }
        //query.retrievedFields(true, "mailNum", "reciever");
        return find(query).asList();
    }
}
