package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.ToOtherSiteLog;
import com.bbd.saas.utils.Dates;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


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
     * 根据转站站点和转站时间统计转站运单数
     * @param fromAreaCode 转站源站点
     * @param between 转站时间范围
     * @return long 转站运单数
     */
    public long countByFromAreaCodeAndTime(String fromAreaCode, String between) {
    	Query<ToOtherSiteLog> query = createQuery();
    	query.filter("fromAreaCode", fromAreaCode);
        return  selectCountByQuery(query, between);
    }

    /**
     * 转站运单数统计
     * @return long 转站运单数
     */
    /**
     * 根据转站站点编号集合和转站时间统计转站运单数
     * @param fromAreaCodeList 转站源站点编号集合
     * @param between 转站时间范围
     * @return long 转站运单数
     */
    public long countByFromAreaCodesAndTime(List<String> fromAreaCodeList, String between){
        Query<ToOtherSiteLog> query = createQuery();
        query.filter("fromAreaCode in", fromAreaCodeList);
        return  selectCountByQuery(query, between);
    }
    private long selectCountByQuery(Query<ToOtherSiteLog> query, String between){
        if(StringUtils.isNotBlank(between)){//预计到站时间
            Date date = Dates.parseDate(between);
            Date startDate = Dates.getBeginOfDay(date);
            Date endDate = Dates.getEndOfDay(date);
            query.filter("operTime >=", startDate).filter("operTime <=", endDate);
        }
        return  count(query);
    }

}
