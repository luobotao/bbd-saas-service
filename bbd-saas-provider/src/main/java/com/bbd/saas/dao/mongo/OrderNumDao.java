package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.PrintStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderNum;
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

/**
 * Created by luobotao on 2016/4/1.
 * 订单DAO
 */
@Repository
public class OrderNumDao extends BaseDAO<OrderNum, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(OrderNumDao.class);

    OrderNumDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    public OrderNum findOrderNum() {
        Query<OrderNum> query = createQuery();
        OrderNum orderNum =  findOne(query);
        return orderNum;
    }

    public UpdateResults updateOrderNum(String type, String num) {
        Query<OrderNum> query = createQuery();
        UpdateOperations<OrderNum> ops = createUpdateOperations();
        ops.set(type,num);
        return update(query,ops);
    }
}
