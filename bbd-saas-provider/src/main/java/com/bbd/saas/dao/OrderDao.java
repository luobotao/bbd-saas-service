package com.bbd.saas.dao;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderQueryVO;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
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

    public PageModel<Order> findOrders(PageModel<Order> pageModel,OrderQueryVO orderQueryVO) {
        Query<Order> query = createQuery().order("dateUpd");
        if(orderQueryVO!=null){
            if(orderQueryVO.arriveStatus!=null && orderQueryVO.arriveStatus!=-1){
                if(orderQueryVO.arriveStatus==1){//已到站 即只要不是未到站，则全为已到站
                    query.filter("orderStatus <>", OrderStatus.status2Obj(0)).filter("orderStatus <>", null);
                }else{
                    query.or(query.criteria("orderStatus").equal(OrderStatus.status2Obj(0)),query.criteria("orderStatus").equal(null));
                }
            }
            if(StringUtils.isNotBlank(orderQueryVO.mailNum)){
                query.filter("mailNum", orderQueryVO.mailNum);
            }
            if(StringUtils.isNotBlank(orderQueryVO.parcelCode)){
                query.filter("parcelCode", orderQueryVO.parcelCode);
            }
        }
        List<Order> orderList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();

        pageModel.setDatas(orderList);
        pageModel.setTotalCount(count(query));
        return pageModel;
    }
}
