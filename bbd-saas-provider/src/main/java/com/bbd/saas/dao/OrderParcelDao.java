package com.bbd.saas.dao;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.utils.PageModel;
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
 * 包裹DAO
 */
@Repository
public class OrderParcelDao extends BaseDAO<OrderParcel, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(OrderParcelDao.class);

    OrderParcelDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    public String findParcelCodeByOrderId(String orderId) {
        OrderParcel orderParcel = findOne(createQuery().filter("orderList._id",new ObjectId(orderId)));
        if(orderParcel!=null)
            return orderParcel.getParcelCode();
        else
            return "";
    }

    public OrderParcel findOrderParcelByParcelCode(String areaCode,String parcelCode) {
        Query<OrderParcel> query = createQuery();
        if(StringUtils.isNotBlank(areaCode))
            query.filter("areaCode",areaCode);
        query.filter("parcelCode",parcelCode);
        return findOne(query);
    }
}
