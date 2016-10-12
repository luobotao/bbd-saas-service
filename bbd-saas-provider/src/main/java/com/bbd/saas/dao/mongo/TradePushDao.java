package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.TradePush;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by ctt on 2016/6/28.
 * tradeDao
 */
@Repository
public class TradePushDao extends BaseDAO<TradePush, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(TradePushDao.class);

    TradePushDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }


    public List<TradePush> getAllTradePushWithTradeNo(String tradeNo) {
        Query<TradePush> query = createQuery();
        query.filter("tradeNo", tradeNo);
        return find(query).asList();
    }

    /**
     * 获得订单最新的一条推送信息
     * @param tradeNo
     * @return
     */
    public TradePush selectLatestOneByTradeNo(String tradeNo) {
        Query<TradePush> query = createQuery();
        query.filter("tradeNo", tradeNo);
        query.order("-dateAdd");
        return findOne(query);
    }

    public TradePush findTradePushWithPostmanUserId(String tradeNo, Integer postmanUserId) {
        Query<TradePush> query = createQuery();
        query.filter("tradeNo", tradeNo);
        query.filter("postmanId", postmanUserId);
        return findOne(query);
    }
    public long selectCountByPMIdAndFlag(Integer postmanUserId) {
        Query<TradePush> query = createQuery();
        query.filter("postmanId", postmanUserId);
        query.filter("flag", 1);//有未处理订单
        return count(query);
    }
    public UpdateResults updateFlagByTradeNo(String tradeNo) {
        Query<TradePush> updateQuery = createQuery();
        updateQuery.filter("tradeNo", tradeNo);
        UpdateOperations<TradePush> ops = this.getDatastore().createUpdateOperations(TradePush.class).set("flag", 0).set("dateUpd", new Date());
        return this.update(updateQuery, ops);
        /*UpdateResults ur = this.update(updateQuery, ops);
        if(ur != null){
            return ur.getUpdatedCount();
        }
        return 0;*/
    }
}
