package com.bbd.saas.dao;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Repository
public class OrderDao extends BaseDAO<Order, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(OrderDao.class);

    OrderDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    public PageModel<Order> findOrders(PageModel<Order> pageModel) {
        List<Order> orderList = find(createQuery().filter("adminUserId", new ObjectId("56d013f156f6c3ba9fe959cb")).order("dateUpd").offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();
        pageModel.setDatas(orderList);
        pageModel.setTotalPages(12);
        return pageModel;
    }
}
