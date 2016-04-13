package com.bbd.saas.dao;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.utils.PageModel;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by luobotao on 2016/4/1.
 * 包裹DAO
 */
@Repository
public class OrderParcelDao extends BaseDAO<OrderParcel, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(OrderParcelDao.class);

    OrderParcelDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    public String findParcelCodeByOrderId(String orderId) {
        orderId = "56f12a44a276ee6916146edc";
//        ObjectId a = new ObjectId(orderId);
//        logger.info(a.toHexString()+"======");
//        Order order = super.getDatastore().createQuery(Order.class).filter("mailNum","BBT186866451").get();

//        Order order = super.getDatastore().find(Order.class).filter("_id",new ObjectId(orderId));
        Query<OrderParcel> query = createQuery().filter("orderList elem",new BasicDBObject().put("_id",new ObjectId(orderId)));
//        Query<OrderParcel> query = createQuery().filter("orderList elem", BasicDBObjectBuilder.start().add("_id",new ObjectId(orderId)));

        //DBObject object = BasicDBObjectBuilder.start().append("_id",new ObjectId(orderId)).get();
//        DBObject object = new BasicDBObject();
//        object.put("_id",new ObjectId(orderId));
//        Query<OrderParcel> query  = createQuery().field("orderList.id").hasThisElement(object );
        return findOne(query).getParcelCode();
//        return findOne("orderList elem",new BasicDBObject().put("_id",new ObjectId(orderId))).getParcelCode();
    }


    public String findParcelCodeByOrderId(Order order) {
    //    orderId = "56f12a44a276ee6916146edc";
//        Query<OrderParcel> query = createQuery().filter("orderList elem","{_id:"+new ObjectId(orderId)+"}");
//        Query<OrderParcel> query = createQuery().filter("orderList elem", BasicDBObjectBuilder.start().add("_id",new ObjectId(orderId)));
        Query<OrderParcel> query  = createQuery().field("orderList").hasThisElement(order);
        return findOne(query).getParcelCode();
//        return findOne("orderList elem",new BasicDBObject().put("_id",new ObjectId(orderId))).getParcelCode();
    }
}
