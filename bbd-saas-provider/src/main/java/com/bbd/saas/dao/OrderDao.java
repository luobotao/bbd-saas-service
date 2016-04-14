package com.bbd.saas.dao;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by luobotao on 2016/4/1.
 * 订单DAO
 */
@Repository
public class OrderDao extends BaseDAO<Order, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(OrderDao.class);

    OrderDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    public PageModel<Order> findOrders(PageModel<Order> pageModel,OrderQueryVO orderQueryVO) {
        Query<Order> query = createQuery().order("-dateUpd");
        if(orderQueryVO!=null){
            if(StringUtils.isNotBlank(orderQueryVO.areaCode)){
                query.filter("areaCode", orderQueryVO.areaCode);
            }
            if(orderQueryVO.arriveStatus!=null && orderQueryVO.arriveStatus!=-1){
                if(orderQueryVO.arriveStatus==1){//已到站 即只要不是未到站，则全为已到站
                    query.filter("orderStatus <>", OrderStatus.status2Obj(0)).filter("orderStatus <>", null);
                }else{
                    query.or(query.criteria("orderStatus").equal(OrderStatus.status2Obj(0)),query.criteria("orderStatus").equal(null));
                }
            }
            if(StringUtils.isNotBlank(orderQueryVO.between)){//预计到站时间
                DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                query.filter("dateMayArrive >=",dateBetween.getStart());
                query.filter("dateMayArrive <=",dateBetween.getEnd());
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

    public OrderNumVO getOrderNumVO(String areaCode) {
        OrderNumVO orderNumVO = new OrderNumVO();
        Query<Order> query = createQuery().filter("areaCode",areaCode);
        Query<Order> queryArrive = createQuery().filter("areaCode",areaCode);
        query.or(query.criteria("orderStatus").equal(OrderStatus.status2Obj(0)),query.criteria("orderStatus").equal(null));
        orderNumVO.setNoArriveHis(count(query));//历史未到站
        query.filter("dateUpd <=",new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -1);
        query.filter("dateUpd >=",cal.getTime());
        orderNumVO.setNoArrive(count(query));//今天未到站
        queryArrive.filter("dateUpd <=",new Date()).filter("dateUpd >",cal.getTime()).filter("orderStatus <>", OrderStatus.status2Obj(0)).filter("orderStatus <>", null);
        orderNumVO.setArrived(count(queryArrive));//已到站
        return orderNumVO;
    }

    public UpdateResults updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew) {
        Query<Order> query = createQuery();
        query.filter("mailNum",mailNum);
        if(orderStatusOld!=null){//旧状态不为空，则需要加入旧状态的判断
            query.or(query.criteria("orderStatus").equal(orderStatusOld),query.criteria("orderStatus").equal(null));
        }
        return update(query,createUpdateOperations().set("orderStatus",orderStatusNew).set("dateUpd",new Date()));
    }

    public Order findOneByMailNum(String areaCode, String mailNum) {
        Query<Order> query = createQuery();
        if(StringUtils.isNotBlank(areaCode))
            query.filter("areaCode",areaCode);
        query.filter("mailNum",mailNum);
        return findOne(query);
    }
}
