package com.bbd.saas.dao.mongo;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.OrderUpdateVO;


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
        query.filter("dateMayArrive <=",new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        query.filter("dateMayArrive >=",cal.getTime());
        orderNumVO.setNoArrive(count(query));//今天未到站
        queryArrive.filter("dateMayArrive <=",new Date()).filter("dateMayArrive >",cal.getTime()).filter("orderStatus <>", OrderStatus.status2Obj(0)).filter("orderStatus <>", null);
        orderNumVO.setArrived(count(queryArrive));//已到站
        return orderNumVO;
    }

    public UpdateResults updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew) {
        Query<Order> query = createQuery();
        query.filter("mailNum",mailNum);
        if(orderStatusOld!=null){//旧状态不为空，则需要加入旧状态的判断
            query.or(query.criteria("orderStatus").equal(orderStatusOld),query.criteria("orderStatus").equal(null));
        }
        UpdateOperations<Order> ops = createUpdateOperations().set("orderStatus",orderStatusNew).set("dateUpd",new Date());
        if(orderStatusOld==OrderStatus.NOTARR){//若是做到站操作，需要更新下到站时间
            ops.set("dateArrived",new Date());
        }
        return update(query,ops);
    }

    public Order findOneByMailNum(String areaCode, String mailNum) {
        Query<Order> query = createQuery();
        if(StringUtils.isNotBlank(areaCode))
            query.filter("areaCode",areaCode);
        query.filter("mailNum",mailNum);
        return findOne(query);
    }
    private Query<Order> getQuery(OrderQueryVO orderQueryVO){
    	Query<Order> query = createQuery();
    	if(orderQueryVO != null){
    		//站点查询
            if(StringUtils.isNotBlank(orderQueryVO.areaCode)){
                query.filter("areaCode", orderQueryVO.areaCode);
            }
            //到站状态
            if(orderQueryVO.arriveStatus != null && orderQueryVO.arriveStatus != -1){
                if(orderQueryVO.arriveStatus == 1){//已到站 即只要不是未到站，则全为已到站
                    query.filter("orderStatus <>", OrderStatus.status2Obj(0)).filter("orderStatus <>", null);
                }else{
                    query.or(query.criteria("orderStatus").equal(OrderStatus.status2Obj(0)),query.criteria("orderStatus").equal(null));
                }
            }
            //预计到站时间
            if(StringUtils.isNotBlank(orderQueryVO.between)){
                DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                query.filter("dateMayArrive >=",dateBetween.getStart());
                query.filter("dateMayArrive <=",dateBetween.getEnd());
            }
            //运单号
            if(StringUtils.isNotBlank(orderQueryVO.mailNum)){
                query.filter("mailNum", orderQueryVO.mailNum);
            }
            //包裹号
            if(StringUtils.isNotBlank(orderQueryVO.parcelCode)){
                query.filter("parcelCode", orderQueryVO.parcelCode);
            }
            //包裹分派状态
            if(orderQueryVO.dispatchStatus != null){
            	if(orderQueryVO.dispatchStatus == -1){//全部（1-未分派，2-已分派）
                	query.or(query.criteria("orderStatus").equal(OrderStatus.status2Obj(1)), query.criteria("orderStatus").equal(OrderStatus.status2Obj(2)));
                }else{
                	query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.dispatchStatus));
                }
            }
        	//派件员
            if(StringUtils.isNotBlank(orderQueryVO.userId)){
                query.filter("user._id", new ObjectId(orderQueryVO.userId));
            }
        	//到站时间
            if(StringUtils.isNotBlank(orderQueryVO.arriveBetween)){
                DateBetween dateBetween = new DateBetween(orderQueryVO.arriveBetween);
                query.filter("dateArrived >=",dateBetween.getStart());
                query.filter("dateArrived <=",dateBetween.getEnd());
            }
            //异常状态
            if(orderQueryVO.abnormalStatus != null){
            	if(orderQueryVO.abnormalStatus == -1){//全部（3-滞留，4-拒收）
                	query.or(query.criteria("orderStatus").equal(OrderStatus.status2Obj(3)), query.criteria("orderStatus").equal(OrderStatus.status2Obj(4)));
                }else{
                	query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.abnormalStatus));
                }
            }
            //订单状态--用于数据查询页面(-1未全部状态，就相当于不需要按状态字段查询)
            if(orderQueryVO.orderStatus != null && orderQueryVO.orderStatus != -1){
            	if(orderQueryVO.orderStatus == OrderStatus.NOTARR.getStatus()){//未到站--OrderStatus=0或者OrderStatus=null
            		query.or(query.criteria("orderStatus").equal(OrderStatus.status2Obj(0)),query.criteria("orderStatus").equal(null));
            	}else{
            		query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.orderStatus));
            	}
            }
        }
    	return query;
    }
    public PageModel<Order> findPageOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO) {
        //设置查询条件
    	Query<Order> query = getQuery(orderQueryVO);
    	//设置排序
    	query.order("-dateUpd");
        //分页信息
        query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize());
        //查询数据
        List<Order> orderList = find(query).asList();
        pageModel.setDatas(orderList);
        pageModel.setTotalCount(count(query));
        return pageModel;
    }
    public List<Order> findOrders(OrderQueryVO orderQueryVO) {
    	//设置查询条件,并按照更新时间倒叙
    	Query<Order> query = getQuery(orderQueryVO);
    	//设置排序
    	query.order("-dateUpd");
        //查询数据
        List<Order> orderList = find(query).asList();
        return orderList;
    }
    public UpdateResults updateOrder(OrderUpdateVO orderUpdateVO, OrderQueryVO orderQueryVO) {
    	Query<Order> query = getQuery(orderQueryVO);
    	UpdateOperations<Order> op = getUpdateOpers(orderUpdateVO);
    	return update(query, op);
    }
    private UpdateOperations<Order> getUpdateOpers(OrderUpdateVO orderUpdateVO){
    	UpdateOperations<Order> ops = createUpdateOperations().set("dateUpd",new Date());
    	//站点编码
    	if(orderUpdateVO.areaCode != null){
            ops.set("areaCode",orderUpdateVO.areaCode);
        }
    	//派件员
    	if(orderUpdateVO.user != null){
            ops.set("user",orderUpdateVO.user);
        }
    	/*//退货原因类型
    	if(orderUpdateVO.returnReasonType != null){
            ops.set("returnReasonType",orderUpdateVO.returnReasonType);
        }
    	//退货原因详情--其他
    	if(orderUpdateVO.returnReasonInfo != null){
            ops.set("returnReasonInfo",orderUpdateVO.returnReasonInfo);
        }*/
    	//订单状态，-1全部, 0-未到站,1-未分派,2-已分派,3-滞留,4-拒收,5-已签收,6-已转其他快递,7-申请退货,8-退货完成;
    	if(orderUpdateVO.orderStatus != null){
            ops.set("orderStatus",orderUpdateVO.orderStatus);
        }
    	return ops;
    }

}
