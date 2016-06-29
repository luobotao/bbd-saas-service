package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.TradeStatus;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.mongoModels.TradePush;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.TradeQueryVO;
import org.apache.commons.lang3.StringUtils;
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

    public TradePush findTradePushWithPostmanUserId(String tradeNo, Integer postmanUserId) {
        Query<TradePush> query = createQuery();
        query.filter("tradeNo", tradeNo);
        query.filter("postmanId", postmanUserId);
        return findOne(query);
    }
}
