package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.ParcelStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.utils.PageModel;
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
        if(orderStatusOld==OrderStatus.NOTARR){//若是做到站操作，需要更新下站点入库时间
            ops.set("orderList.$.dateArrived",new Date());
        }
        return update(query,ops);
    }

    /**
     * 根据订单ID获取此订单所处的包裹信息
     * @param orderId
     * @param parceltyp 包裹类型 0：配件包裹（默认） 1：集包
     * @return
     */
    public OrderParcel findOrderParcelByOrderIdAndParcelType(String orderId,String parceltyp) {
        return findOne(createQuery().filter("orderList._id",new ObjectId(orderId)).filter("parceltyp",parceltyp));
    }
    /**
     * 根据订单的运单号查询该运单号所处的包裹
     * @param mailNum
     * @return
     */
    public OrderParcel findOrderParcelByMailNum(String mailNum) {
        return findOne(createQuery().filter("orderList.mailNum",mailNum));
    }
    /**
     * 根据运单号查询所有的包裹
     * @param trackNo
     * @return
     */
    public List<OrderParcel> findOrderParcelListByTrackCode(String trackNo) {
        Query<OrderParcel> query = createQuery();
        query.filter("trackNo",trackNo);
        return find(query).asList();
    }

    /**
     * 查找是否有满足指定条件的包裹
     * 指定来源src,指定站点areaCode,指定状态，待打包
     * @param order
     * @return
     */
    public OrderParcel findByOrderInfo(Order order) {
        Query<OrderParcel> query = createQuery();
        query.filter("areaCode",order.getAreaCode());
        query.filter("src",order.getSrc());
        query.filter("status", ParcelStatus.Suspense);
        return findOne(query);
    }

    /**
     * 根据站点编码和包裹状态获取包裹列表
     * @param areaCode
     * @param parcelStatus
     * @return
     */
    public List<OrderParcel> findOrderParcelsByAreaCodeAndStatus(String areaCode, ParcelStatus parcelStatus) {
        Query<OrderParcel> query = createQuery();
        query.filter("areaCode",areaCode);
        query.filter("status",parcelStatus);
        return find(query).asList();
    }
    /**
     * APP端获取包裹到站列表
     * @param uid 站长id
     * @param offset  跳过的条数
     * @param pagesize 查询的数据条数
     * @return 包裹列表
     */
    public List<OrderParcel> findStagionParcelList(String uid, int offset, int pagesize){
        Query<OrderParcel> query = createQuery().order("-dateUpd");
        query.filter("station_uid", uid);
        query.filter("status <>", ParcelStatus.ArriveStation);
        if(offset > -1){
            //分页信息
            query.offset(offset).limit(pagesize);
        }
        return find(query).asList();
    }

    PageModel<OrderParcel> findStagionParcelPage(String uid, int offset, int pagesize){
        PageModel<OrderParcel> pageModel = new PageModel<OrderParcel>();
        Query<OrderParcel> query = createQuery().order("-dateUpd");
        query.filter("station_uid", uid);
        query.filter("status <>", ParcelStatus.ArriveStation);
        //分页信息
        query.offset(offset).limit(pagesize);
        pageModel.setDatas(find(query).asList());
        pageModel.setTotalCount(count(query));
        return pageModel;
    }

    /**
     * 根据来源、站点编码和包裹状态获取包裹列表
     * @param areaCode
     * @param src
     * @param parcelStatus
     * @return
     */
    public List<OrderParcel> findOrderParcelsByAreaCodeAndStatusAndSrc(String areaCode, String src, ParcelStatus parcelStatus) {
        Query<OrderParcel> query = createQuery();
        query.filter("src",src);
        query.filter("areaCode",areaCode);
        query.filter("status",parcelStatus);
        return find(query).asList();
    }
}
