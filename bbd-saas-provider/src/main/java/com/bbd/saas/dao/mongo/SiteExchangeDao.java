package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.SiteExchange;
import com.bbd.saas.utils.StringUtil;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

/**
 * 编码转换
 * Created by ctt on 2016/6/15.
 */
@Repository
public class SiteExchangeDao extends BaseDAO<SiteExchange, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(SiteExchangeDao.class);

    SiteExchangeDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    public SiteExchange findSiteExchangeByCodeAndTyp(String code, String typ) {
        Query<SiteExchange> query = createQuery();
        if(StringUtil.isNotEmpty(code)){//旧状态不为空，则需要加入旧状态的判断
            query.or(query.criteria("areaCode").equal(code),query.criteria("exchangeCode").equal(code));
        }
        if(StringUtil.isNotEmpty(typ)){//类型
            query.filter("exchangeType", typ);
        }
        query.filter("status", "1");
        return findOne(query);
    }
    /**
     * 根据areaCode查找一个expressExchange对象
     * @param areaCode 站点编号
     * @return expressExchange对象
     */
    public SiteExchange selectOneByAreaCode(String areaCode){
        Query<SiteExchange> query = createQuery();
        if(StringUtil.isNotEmpty(areaCode)){
            query.filter("areaCode", areaCode);
        }
        return findOne(query);
    }
}
