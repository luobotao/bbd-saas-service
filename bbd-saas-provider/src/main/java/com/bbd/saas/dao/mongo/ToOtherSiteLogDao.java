package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.ToOtherSiteLog;
import com.bbd.saas.utils.DateBetween;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;


/**
 * Created by luobotao on 2016/4/1.
 * 站点DAO
 */
@Repository
public class ToOtherSiteLogDao extends BaseDAO<ToOtherSiteLog, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(ToOtherSiteLogDao.class);

    ToOtherSiteLogDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    /**
     * 转站运单数统计
     * @return long 转站运单数
     */
    public long countByFromAreaCodeAndTime(String fromAreaCode, String between) {
    	Query<ToOtherSiteLog> query = createQuery();
    	query.filter("fromAreaCode", fromAreaCode);
        if(StringUtils.isNotBlank(between)){//预计到站时间
            DateBetween dateBetween = new DateBetween(between);
            query.filter("operTime >=",dateBetween.getStart());
            query.filter("operTime <=",dateBetween.getEnd());
        }
    	return  count(query);
    }

}
