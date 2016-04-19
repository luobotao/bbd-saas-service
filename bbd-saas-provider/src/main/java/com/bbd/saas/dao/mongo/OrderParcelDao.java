package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import org.apache.commons.lang.StringUtils;
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

    /**
     * 修改包裹表里的订单的状态
     * @param mailNum
     * @param orderStatusOld
     * @param orderStatusNew
     */
    public UpdateResults updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew) {
        Query<OrderParcel> query = createQuery();
        query.filter("orderList.mailNum",mailNum);
        if(orderStatusOld!=null){//旧状态不为空，则需要加入旧状态的判断
            query.or(query.criteria("orderList.orderStatus").equal(orderStatusOld),query.criteria("orderList.orderStatus").equal(null));
        }
        UpdateOperations<OrderParcel> ops = createUpdateOperations().set("orderList.$.orderStatus",orderStatusNew).enableValidation().set("orderList.$.dateUpd",new Date());
        if(orderStatusOld==OrderStatus.NOTARR){//若是做到站操作，需要更新下到站时间
            ops.set("orderList.$.dateArrived",new Date());
        }
        return update(query,ops);
    }
}
