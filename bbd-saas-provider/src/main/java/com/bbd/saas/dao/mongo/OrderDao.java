package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.*;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderGroup;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.*;
import com.google.common.collect.Lists;
import com.bbd.saas.utils.Constants;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.*;
import com.mongodb.BasicDBList;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.aggregation.Sort;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.mongodb.morphia.aggregation.Group.*;


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

    /**
     * 带查询条件去检索订单
     *
     * @param pageModel
     * @param orderQueryVO
     * @return
     */
    public PageModel<Order> findOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO) {
        Query<Order> query = createQuery().order("-dateUpd");
        if (orderQueryVO != null) {
            query.filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
            if (StringUtils.isNotBlank(orderQueryVO.areaCode)) {
                query.filter("areaCode", orderQueryVO.areaCode);
            }
            if (orderQueryVO.arriveStatus != null && orderQueryVO.arriveStatus != -1) {
                if (orderQueryVO.arriveStatus == 1) {//已到站 即只要不是未到站,待揽件,已揽件，则全为已到站
                    query.filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null).filter("expressStatus <>", ExpressStatus.Suspense).filter("expressStatus <>", ExpressStatus.Separating).filter("expressStatus <>", ExpressStatus.Packed);;
                    query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.ARRIVED), query.criteria("orderSetStatus").equal(null));

                    if (StringUtils.isNotBlank(orderQueryVO.between)) {//到站时间
                        DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                        query.filter("dateArrived >=", dateBetween.getStart());
                        query.filter("dateArrived <=", dateBetween.getEnd());
                    }
                } else {//未到站
                    query.filter("orderStatus", OrderStatus.NOTARR).filter("expressStatus <>", ExpressStatus.Suspense).filter("expressStatus <>", ExpressStatus.Separating).filter("expressStatus <>", ExpressStatus.Packed);;
                    query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED), query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING), query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN), query.criteria("orderSetStatus").equal(OrderSetStatus.ARRIVED), query.criteria("orderSetStatus").equal(null));

                    if (StringUtils.isNotBlank(orderQueryVO.between)) {//预计到站时间
                        DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                        query.filter("dateMayArrive >=", dateBetween.getStart());
                        query.filter("dateMayArrive <=", dateBetween.getEnd());
                    }
                }
            } else {//全部（已到站||未到站）
                query.filter("orderStatus <>", null).filter("expressStatus <>", ExpressStatus.Suspense).filter("expressStatus <>", ExpressStatus.Separating).filter("expressStatus <>", ExpressStatus.Packed);
                query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED), query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING), query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN), query.criteria("orderSetStatus").equal(OrderSetStatus.ARRIVED), query.criteria("orderSetStatus").equal(null));
                if (StringUtils.isNotBlank(orderQueryVO.between)) {//预计到站时间
                    DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                    query.filter("dateMayArrive >=", dateBetween.getStart());
                    query.filter("dateMayArrive <=", dateBetween.getEnd());
                }
            }

            if (StringUtils.isNotBlank(orderQueryVO.mailNum)) {
                query.filter("mailNum", orderQueryVO.mailNum);
            }
            if (StringUtils.isNotBlank(orderQueryVO.parcelCode)) {
                query.filter("parcelCode", orderQueryVO.parcelCode);
            }
        }
        List<Order> orderList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();

        pageModel.setDatas(orderList);
        pageModel.setTotalCount(count(query));
        return pageModel;
    }

    /**
     * 根据站点编码获取该站点订单数据
     *
     * @param areaCode
     * @return
     */
    public OrderNumVO getOrderNumVO(String areaCode) {
        OrderNumVO orderNumVO = new OrderNumVO();
        Query<Order> query = createQuery().filter("areaCode", areaCode).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        query.filter("orderStatus", OrderStatus.NOTARR).filter("expressStatus <>", ExpressStatus.Suspense).filter("expressStatus <>", ExpressStatus.Separating).filter("expressStatus <>", ExpressStatus.Packed);
        query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED), query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING), query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN), query.criteria("orderSetStatus").equal(OrderSetStatus.ARRIVED), query.criteria("orderSetStatus").equal(null));

        orderNumVO.setNoArriveHis(count(query));//历史未到站

        Query<Order> queryArrive = createQuery().filter("areaCode", areaCode).filter("mailNum <>", null).filter("mailNum <>", "").filter("expressStatus <>", ExpressStatus.Suspense).filter("expressStatus <>", ExpressStatus.Separating).filter("expressStatus <>", ExpressStatus.Packed);//运单号不能为空
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        query.filter("dateMayArrive >=", cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, 1);
        query.filter("dateMayArrive <=", cal.getTime());
        orderNumVO.setNoArrive(count(query));//今天未到站
        cal.add(Calendar.DAY_OF_YEAR, -1);
        queryArrive.filter("dateArrived <=", new Date()).filter("dateArrived >", cal.getTime()).filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null);
        orderNumVO.setArrived(count(queryArrive));//已到站
        return orderNumVO;
    }

    /**
     * 更新订单状态
     * 此处需要再加上包裹下的订单的状态更新
     *
     * @param mailNum        运单号
     * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
     * @param orderStatusNew
     */
    public UpdateResults updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew) {
        Query<Order> query = createQuery();
        query.filter("mailNum", mailNum);
        if (orderStatusOld != null) {//旧状态不为空，则需要加入旧状态的判断
            query.or(query.criteria("orderStatus").equal(orderStatusOld), query.criteria("orderStatus").equal(null));
            //query.filter("orderStatus",orderStatusOld);
        }
        UpdateOperations<Order> ops = createUpdateOperations().set("orderStatus", orderStatusNew).set("dateUpd", new Date());
        if (orderStatusOld == OrderStatus.NOTARR) {//若是做到站操作，需要更新下到站时间
            ops.set("dateArrived", new Date());
        }
        return update(query, ops);
    }

    /**
     * Description: 根据运单号查询订单信息
     *
     * @param mailNum 运单号
     * @return
     */
    public Order findOneByMailNum(String areaCode, String mailNum) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(areaCode))
            query.filter("areaCode", areaCode);
        query.filter("mailNum", mailNum);
        return findOne(query);
    }


    private Query<Order> getQuery(OrderQueryVO orderQueryVO) {
        Query<Order> query = createQuery();
        query.filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        if (orderQueryVO != null) {
            //公司查询 -- 一个公司下的所有站点的areaCode集合
            if (orderQueryVO.areaCodeList != null && orderQueryVO.areaCodeList.size() > 0) {
                query.filter("areaCode in", orderQueryVO.areaCodeList);
            }
            //站点查询
            if (StringUtils.isNotBlank(orderQueryVO.areaCode)) {
                query.filter("areaCode", orderQueryVO.areaCode);
            }
            //到站状态
            if (orderQueryVO.arriveStatus != null) {
                if (orderQueryVO.arriveStatus == -1) {//全部（未到站||已到站）
                    query.filter("orderStatus <>", null);
                    query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED), query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING), query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN), query.criteria("orderSetStatus").equal(OrderSetStatus.ARRIVED), query.criteria("orderSetStatus").equal(null));
                } else if (orderQueryVO.arriveStatus == 1) {//已到站 即只要不是未到站，则全为已到站
                    query.filter("orderStatus <>", null).filter("orderStatus <>", OrderStatus.NOTARR);
                } else {//未到站
                    query.filter("orderStatus", OrderStatus.NOTARR);
                    query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED), query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING), query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN), query.criteria("orderSetStatus").equal(null));

                }
            } else {
                query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED), query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING), query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN), query.criteria("orderSetStatus").equal(OrderSetStatus.ARRIVED), query.criteria("orderSetStatus").equal(null));

            }
            //预计到站时间
            if (StringUtils.isNotBlank(orderQueryVO.between)) {
                DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                query.filter("dateMayArrive >=", dateBetween.getStart());
                query.filter("dateMayArrive <=", dateBetween.getEnd());
            }
            //运单号--模糊查询
            if (StringUtils.isNotBlank(orderQueryVO.mailNum)) {
                query.and(query.criteria("mailNum").containsIgnoreCase(orderQueryVO.mailNum));
            }
            //商户订单号（tradeNo）或者运单号(mailnum)--模糊查询
            if (StringUtils.isNotBlank(orderQueryVO.tradeOrMailNoLike)) {
                query.or(query.criteria("mailNum").containsIgnoreCase(orderQueryVO.tradeOrMailNoLike),
                        query.criteria("tradeNo").containsIgnoreCase(orderQueryVO.tradeOrMailNoLike));
            }
            //包裹号
            if (StringUtils.isNotBlank(orderQueryVO.parcelCode)) {
                query.filter("parcelCode", orderQueryVO.parcelCode);
            }
            //派件员
            if (StringUtils.isNotBlank(orderQueryVO.userId)) {
                query.filter("userId", orderQueryVO.userId);//只查询已经分派的
            } else {
                //包裹分派状态
                if (orderQueryVO.dispatchStatus != null) {
                    if (orderQueryVO.dispatchStatus == -1) {//全部（1-未分派，2-已分派）
                        query.or(query.criteria("orderStatus").equal(OrderStatus.NOTDISPATCH), query.criteria("orderStatus").equal(OrderStatus.DISPATCHED));
                    } else {
                        query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.dispatchStatus));
                    }
                }
            }

            //到站时间和状态
            if (StringUtils.isNotBlank(orderQueryVO.arriveBetween)) {//根据到站时间查询
                if (orderQueryVO.orderStatus != null) {//单个状态
                    if (orderQueryVO.orderStatus == OrderStatus.NOTARR.getStatus()) {//未到站--OrderStatus=0
                        query.filter("orderStatus", OrderStatus.NOTARR);
                    } else {//已到站
                        this.getOrderStatusQuery(query, orderQueryVO);//单个状态
                        //到站时间，只有已到站的订单才会有到站时间
                        DateBetween dateBetween = new DateBetween(orderQueryVO.arriveBetween);
                        query.filter("dateArrived >=", dateBetween.getStart());
                        query.filter("dateArrived <=", dateBetween.getEnd());
                    }
                } else if (orderQueryVO.orderStatusList != null) {//查询多个状态
                    if (orderQueryVO.orderStatusList.contains(OrderStatus.NOTARR)) {
                        //未到站的记录(未到站的运单，到站时间为空)
                        Criteria notArriveCS = query.criteria("orderStatus").equal(OrderStatus.NOTARR);
                        //按照时间查询--已到站的记录
                        DateBetween dateBetween = new DateBetween(orderQueryVO.arriveBetween);
                        Criteria startC = query.criteria("dateArrived").greaterThanOrEq(dateBetween.getStart());
                        Criteria endC = query.criteria("dateArrived").lessThanOrEq(dateBetween.getEnd());
                        orderQueryVO.orderStatusList.remove(OrderStatus.NOTARR);//移除未到站状态
                        //多个状态
                        Criteria arrivedCS = null;
                        if (orderQueryVO.orderStatusList.contains(OrderStatus.SIGNED) && !orderQueryVO.orderStatusList.contains(OrderStatus.TO_OTHER_EXPRESS)) {//app到站
                            Criteria statusC = query.criteria("orderStatus").hasAnyOf(orderQueryVO.orderStatusList);
                            Criteria otherExpC = query.or(query.criteria("otherExprees").equal(null), query.criteria("otherExprees").sizeEq(0));
                            arrivedCS = query.and(statusC, otherExpC);
                        } else if (orderQueryVO.orderStatusList.contains(OrderStatus.TO_OTHER_EXPRESS) && !orderQueryVO.orderStatusList.contains(OrderStatus.SIGNED)) {
                            arrivedCS = query.or(query.criteria("orderStatus").hasAnyOf(orderQueryVO.orderStatusList), query.criteria("otherExprees").notEqual(null));
                        } else {//查询多个状态
                            arrivedCS = query.criteria("orderStatus").hasAnyOf(orderQueryVO.orderStatusList);
                        }
                        Criteria arrivedC = query.and(startC, endC, arrivedCS);
                        query.or(notArriveCS, arrivedC);
                    } else {//已到站
                        this.getOrderStatusListQuery(query, orderQueryVO);//多个状态
                        //到站时间，只有已到站的订单才会有到站时间
                    }
                }
                //包裹分派状态
                if (orderQueryVO.dispatchStatus != null) {
                    if (orderQueryVO.dispatchStatus == -1) {//全部（1-未分派，2-已分派）
                        query.or(query.criteria("orderStatus").equal(OrderStatus.NOTDISPATCH), query.criteria("orderStatus").equal(OrderStatus.DISPATCHED));
                    } else {
                        query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.dispatchStatus));
                    }
                }
                //派件员
                if (StringUtils.isNotBlank(orderQueryVO.userId)) {
                    query.filter("userId", orderQueryVO.userId);
                }
                //异常状态
                if (orderQueryVO.abnormalStatus != null) {
                    if (orderQueryVO.abnormalStatus == -1) {//全部（3-滞留，4-拒收）
                        query.or(query.criteria("orderStatus").equal(OrderStatus.RETENTION), query.criteria("orderStatus").equal(OrderStatus.REJECTION));
                    } else {
                        query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.abnormalStatus));
                    }
                }
                //订单状态和到站时间
                if (orderQueryVO.orderStatus == null) {//除了数据查询页面之外的其他的页面的到站时间查询
                    //预计到站时间
                    if (StringUtils.isNotBlank(orderQueryVO.arriveBetween)) {
                        DateBetween dateBetween = new DateBetween(orderQueryVO.arriveBetween);
                        query.filter("dateArrived >=", dateBetween.getStart());
                        query.filter("dateArrived <=", dateBetween.getEnd());
                    }
                } else if (orderQueryVO.orderStatus == -1) {//数据查询页面===查询全部，就相当于不需要按状态字段查询,并且包含到站时间为空（未到站）的记录
                    //未到站||已到站
                    query.filter("orderStatus <>", null);
                    //到站的运单，根据时间查询；未到站，时间为空
                    if (StringUtils.isNotBlank(orderQueryVO.arriveBetween)) {
                        //按照时间查询--已到站的记录
                        Query<Order> timeQuery = createQuery();
                        DateBetween dateBetween = new DateBetween(orderQueryVO.arriveBetween);
                        Criteria startC = timeQuery.criteria("dateArrived").greaterThanOrEq(dateBetween.getStart());
                        Criteria endC = timeQuery.criteria("dateArrived").lessThanOrEq(dateBetween.getEnd());
                        Criteria timeC = timeQuery.and(startC, endC);
                        //时间为空query--未到站的记录(未到站的运单，到站时间为空)
                        Query<Order> timeNullQuery = createQuery();
                        Criteria notArriveC = timeNullQuery.criteria("orderStatus").equal(OrderStatus.NOTARR);
                        query.or(timeC, notArriveC);
                    }
                } else if (orderQueryVO.orderStatus == OrderStatus.NOTARR.getStatus()) {//未到站--OrderStatus=0
                    query.filter("orderStatus", OrderStatus.NOTARR);
                }
                //到站的运单，根据时间查询；未到站，时间为空
                if (orderQueryVO.arriveStatus != null && orderQueryVO.arriveStatus.intValue() == 1) {//到站的订单
                    //到站时间，只有已到站的订单才会有到站时间
                    DateBetween dateBetween = new DateBetween(orderQueryVO.arriveBetween);
                    query.filter("dateArrived >=", dateBetween.getStart());
                    query.filter("dateArrived <=", dateBetween.getEnd());
                } else {
                    this.setQueryDateArrived(query, orderQueryVO.arriveBetween);
                }
            }
        } else {//不根据到站时间查询
            this.getOrderStatusQuery(query, orderQueryVO);//单个状态
            this.getOrderStatusListQuery(query, orderQueryVO);//多个状态
        }
        return query;
    }

    public void selectByQuery(OrderQueryVO orderQueryVO) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(orderQueryVO.areaCode)) {
            query.filter("areaCode", orderQueryVO.areaCode);
        }
        this.getOrderStatusQuery(query, orderQueryVO);
        System.out.println(count(query));
        //List<Order> orderList = find(query).asList();
    }

    /**
     * 查询单个状态的Query
     *
     * @param query
     * @param orderQueryVO
     * @return
     */
    public Query<Order> getOrderStatusQuery(Query<Order> query, OrderQueryVO orderQueryVO) {

        if(orderQueryVO.orderStatus != null){
            if(orderQueryVO.orderStatus == 0){
                query.or(query.criteria("orderStatus").equal(null), query.criteria("orderStatus").equal(OrderStatus.status2Obj(orderQueryVO.orderStatus)));
                return query;
            }
            if(orderQueryVO.orderStatus == OrderStatus.SIGNED.getStatus()){//已签收--OrderStatus=5
                query.filter("orderStatus", OrderStatus.SIGNED);
                // otherExprees == null || Size(otherExprees)==0
                query.or(query.criteria("otherExprees").equal(null), query.criteria("otherExprees").sizeEq(0));
            }else if(orderQueryVO.orderStatus == OrderStatus.TO_OTHER_EXPRESS.getStatus()){//转其他快递--OrderStatus=6
                query.filter("otherExprees <>", null);
            }else{//单个状态
                query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.orderStatus));
            }
        }
        return query;
    }

    /**
     * 查询多个状态的Query
     *
     * @param query
     * @param orderQueryVO
     * @return
     */
    private Query<Order> getOrderStatusListQuery(Query<Order> query, OrderQueryVO orderQueryVO) {
        if (orderQueryVO.orderStatusList != null && orderQueryVO.orderStatusList.size() > 0) {
            if (orderQueryVO.orderStatusList.contains(OrderStatus.SIGNED) && !orderQueryVO.orderStatusList.contains(OrderStatus.TO_OTHER_EXPRESS)) {
                query.filter("orderStatus in", orderQueryVO.orderStatusList);
                query.or(query.criteria("otherExprees").equal(null), query.criteria("otherExprees").sizeEq(0));
            } else if (orderQueryVO.orderStatusList.contains(OrderStatus.TO_OTHER_EXPRESS) && !orderQueryVO.orderStatusList.contains(OrderStatus.SIGNED)) {
                query.or(query.criteria("orderStatus").hasAnyOf(orderQueryVO.orderStatusList), query.criteria("otherExprees").notEqual(null));
            } else {//查询多个状态
                query.filter("orderStatus in", orderQueryVO.orderStatusList);
            }
        }

        return query;
    }

    /**
     * 未到站，时间为空 || 到站的运单，根据时间查询；
     *
     * @param query
     * @param arriveBetween
     */
    private void setQueryDateArrived(Query<Order> query, String arriveBetween) {
        if (StringUtils.isNotBlank(arriveBetween)) {
            //时间为空query--未到站的记录(未到站的运单，到站时间为空)
            Criteria notArriveC = query.criteria("orderStatus").equal(OrderStatus.NOTARR);
            //按照时间查询--已到站的记录
            DateBetween dateBetween = new DateBetween(arriveBetween);
            Criteria startC = query.criteria("dateArrived").greaterThanOrEq(dateBetween.getStart());
            Criteria endC = query.criteria("dateArrived").lessThanOrEq(dateBetween.getEnd());
            Criteria timeC = query.and(startC, endC);
            query.or(timeC, notArriveC);
        }
    }

    public PageModel<Order> findPageOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO) {
        //设置查询条件
        Query<Order> query = getQuery(orderQueryVO);
        if (orderQueryVO.dispatchStatus != null) {//运单分派
            //设置排序
            query.order("-orderStatus,-dateUpd");
        } else {//其他页面
            //设置排序
            query.order("-dateUpd");
        }
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

    private UpdateOperations<Order> getUpdateOpers(OrderUpdateVO orderUpdateVO) {
        UpdateOperations<Order> ops = createUpdateOperations().set("dateUpd", new Date());
        //站点编码
        if (orderUpdateVO.areaCode != null) {
            ops.set("areaCode", orderUpdateVO.areaCode);
        }
        //派件员
        if (orderUpdateVO.user != null) {
            ops.set("user", orderUpdateVO.user);
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
        if (orderUpdateVO.orderStatus != null) {
            ops.set("orderStatus", orderUpdateVO.orderStatus);
        }
        //物流状态
        if (orderUpdateVO.expressStatus != null) {
            ops.set("expressStatus", orderUpdateVO.expressStatus);
        }
        //增加一条物流信息====ExpressStatus
        if (orderUpdateVO.express != null) {
            ops.add("expresses", orderUpdateVO.express);
        }
        return ops;
    }

    /**
     * 根据站点编码获取该站点历史未到站订单数||某天已到站
     *
     * @param areaCode    站点编号
     * @param dateArrived 到站日期
     * @return
     */
    public OrderNumVO selectHistoryNoArrivedAndArrivedNums(String areaCode, String dateArrived) {
        OrderNumVO orderNumVO = new OrderNumVO();
        Query<Order> query = createQuery().filter("areaCode", areaCode).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        query.filter("orderStatus", OrderStatus.NOTARR);
        orderNumVO.setNoArriveHis(count(query));//历史未到站
        Query<Order> queryArrive = createQuery().filter("areaCode", areaCode).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        orderNumVO.setArrived(selectArrivedByQuery(queryArrive, dateArrived));//已到站
        queryArrive.filter("userId <>", null).filter("userId <>", "");//已分派的
        orderNumVO.setTotalDispatched(count(queryArrive));//所有已分派的
        return orderNumVO;
    }

    /**
     * 根据站点编码获取该站点历史未到站订单数||某天已到站∂ç
     *
     * @param areaCodeList 站点编号集合
     * @param dateArrived  到站日期
     * @return 订单条数
     */
    public OrderNumVO selectHistoryNoArrivedAndArrivedNums(List<String> areaCodeList, String dateArrived) {
        OrderNumVO orderNumVO = new OrderNumVO();
        Query<Order> query = createQuery().filter("areaCode in", areaCodeList).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        query.filter("orderStatus", OrderStatus.NOTARR);
        orderNumVO.setNoArriveHis(count(query));//历史未到站
        Query<Order> queryArrive = createQuery().filter("areaCode in", areaCodeList).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        orderNumVO.setArrived(selectArrivedByQuery(queryArrive, dateArrived));//已到站
        queryArrive.filter("userId <>", null).filter("userId <>", "");//已分派的
        orderNumVO.setTotalDispatched(count(queryArrive));//所有已分派的
        return orderNumVO;
    }

    //查询已到站的订单数目
    private long selectArrivedByQuery(Query<Order> query, String dateArrived) {
        Date date = Dates.parseDate(dateArrived);
        Date startDate = Dates.getBeginOfDay(date);
        Date endDate = Dates.getEndOfDay(date);
        query.filter("dateArrived >=", startDate).filter("dateArrived <=", endDate);
        query.filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null);
        return count(query);
    }

    /**
     * 得到指定站点当天更新的所有订单
     *
     * @param areaCode 站点编号
     * @return 订单集合
     */
    public List<Order> getTodayUpdateOrdersByAreaCode(String areaCode) {
        Query<Order> query = createQuery().filter("areaCode", areaCode).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        //昨天0:0:0:0 - 23:59:59:999
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        query.filter("dateUpd >=", cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        query.filter("dateUpd <=", cal.getTime());
        //查询数据
        return find(query).asList();
    }

    /**
     * 根据其他快递的运单号查询订单
     *
     * @param newMailNum
     * @return
     */
    public List<Order> findOneByNewMailNum(String newMailNum) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(newMailNum))
            query.filter("otherExprees.mailNum", newMailNum);
        return find(query).asList();
    }

    /**
     * 根据其他快递的运单号查询订单数目
     * @param newMailNum 新运单号
     * @return 订单数目
     */
    public long selectCountByNewMailNum(String newMailNum) {
        Query<Order> query = createQuery();
        if(StringUtils.isNotBlank(newMailNum))
            query.filter("otherExprees.mailNum",newMailNum);
        return count(query);
    }

    /**
     * 查询指定mailNum集合的订单中物流状态不为expressStatus的订单的条数
     *
     * @param mailNumList   mailNum集合
     * @param expressStatus 物流状态
     * @return 订单的条数
     */
    public long selectCountByMailNumsAndExpressStatus(BasicDBList mailNumList, ExpressStatus expressStatus) {
        Query<Order> query = createQuery();
        if (mailNumList != null && mailNumList.size() > 0) {
            query.filter("mailNum in", mailNumList);
        }
        if (expressStatus != null) {
            query.filter("expressStatus <>", expressStatus);
        }
        return count(query);
    }

    public Order findByOrderNo(String orderNo) {
        Query<Order> query = createQuery();
        query.filter("orderNo", orderNo);
        return findOne(query);
    }

    public UpdateResults updateOrderWithAreaCode(String orderNo, String areaCode, String areaRemark, PrintStatus printStatus) {
        Query<Order> query = createQuery();
        query.filter("orderNo", orderNo);
        UpdateOperations<Order> ops = createUpdateOperations().set("areaCode", areaCode).set("areaRemark", areaRemark).set("printStatus", printStatus);
        ops.set("dateUpd", new Date());
        return update(query, ops);
    }

    public UpdateResults updateOrderWithMailNum(Order order) {
        Query<Order> query = createQuery();
        query.filter("orderNo", order.getOrderNo());
        UpdateOperations<Order> ops = createUpdateOperations().set("mailNum", order.getMailNum());
        ops.set("dateUpd", new Date());
        return update(query, ops);
    }

    /**
     * 分页查询运单号/手机号/姓名/地址四个字段中包含关键字（keyword）的运单
     *
     * @param pageModel 分页对象（当前页和每页条数）
     * @param tradeNo   //商户订单号
     * @param uId       //用户ID,网站端进行改版加入账号体系,数据将从User里获取(adminUserId将不再使用)
     * @param keyword   查询关键词，对运单号/手机号/姓名/地址四个字段进行查询
     * @return 分页对象（当前页、每页条数、数据）
     */
    public PageModel<Order> findPageOrders(PageModel<Order> pageModel, String tradeNo, ObjectId uId, String keyword) {
        //创建查询条件
        Query<Order> query = createQuery();
        //商户订单号(我们自己生成的支付订单号)
        if (StringUtils.isNotBlank(tradeNo)) {
            query.filter("tradeNo", tradeNo);
        }
        //用户ID,网站端进行改版加入账号体系,数据将从User里获取(adminUserId将不再使用)
        if (uId != null) {
            query.filter("uId", uId);
        }
        if (StringUtils.isNotBlank(keyword)) {
            query.or(query.criteria("mailNum").containsIgnoreCase(keyword),
                    query.criteria("orderNo").containsIgnoreCase(keyword),
                    query.criteria("reciever.phone").containsIgnoreCase(keyword),
                    query.criteria("reciever.name").containsIgnoreCase(keyword));
        }

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

    /**
     * 根据商品订单号查询该订单下包含的运单数目
     *
     * @param tradeNo //商户订单号(我们自己生成的支付订单号)
     * @return 订单下包含的运单数目
     */
    public long findCountByTradeNo(String tradeNo, Integer removeStatus) {
        //创建查询条件
        Query<Order> query = createQuery();
        //商户订单号(我们自己生成的支付订单号)
        if (StringUtils.isNotBlank(tradeNo)) {
            query.filter("tradeNo", tradeNo);
        }
        if (removeStatus != null) {
            query.filter("isRemoved", removeStatus);
        }
        return count(query);
    }

    /**
     * 分页查询手机号/姓名/地址三个字段中包含关键字（keyword）的运单
     *
     * @param uId     //用户ID,网站端进行改版加入账号体系,数据将从User里获取(adminUserId将不再使用)
     * @param tradeNo //商户订单号 ||运单号
     * @param keyword 查询关键词，对手机号/姓名/地址三个字段进行查询
     * @return 运单列表数据
     */
    public List<Order> findOrderList(ObjectId uId, String tradeNo, String keyword) {
        //创建查询条件
        Query<Order> query = createQuery();
        //用户ID,网站端进行改版加入账号体系,数据将从User里获取(adminUserId将不再使用)
        if (uId != null) {
            query.filter("uId", uId);
        }
        //商户订单号或者运单号
        if (StringUtils.isNotBlank(tradeNo)) {
            query.or(query.criteria("tradeNo").containsIgnoreCase(tradeNo),
                    query.criteria("mailNum").containsIgnoreCase(tradeNo));
        }
        if (StringUtils.isNotBlank(keyword)) {
            query.or(query.criteria("reciever.phone").containsIgnoreCase(keyword),
                    query.criteria("reciever.name").containsIgnoreCase(keyword));
        }
        //查询数据
        return find(query).asList();
    }

    public Order findByOrderNoAndSrc(String orderNO, Srcs srcs) {
        //创建查询条件
        Query<Order> query = createQuery();
        query.filter("orderNo", orderNO);
        query.filter("src", srcs);
        return findOne(query);
    }


    public List<Order> findAllByTradeNo(String tradeNo) {
        //创建查询条件
        Query<Order> query = createQuery();
        //商户订单号(我们自己生成的支付订单号)
        if (StringUtils.isNotBlank(tradeNo)) {
            query.filter("tradeNo", tradeNo);
        }
        query.filter("isRemoved", Constants.ISNOTREMOVED);
        return find(query).asList();
    }


    /**
     * 揽件入库的根据条件查询
     *
     * @param pageModel
     * @param orderQueryVO 封装的查询条件
     * @return
     */
    public PageModel<Order> findPageOrdersForHoldToStore(PageModel<Order> pageModel, OrderQueryVO orderQueryVO) {

        //设置查询条件
        Query<Order> query = getQueryForHoldToStore(orderQueryVO);
        query.filter("isRemoved", Constants.ISNOTREMOVED);

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

    /**
     * 揽件入库 封装查询条件
     *
     * @param orderQueryVO 订单的查询条件
     * @return
     */
    private Query<Order> getQueryForHoldToStore(OrderQueryVO orderQueryVO) {
        Date start = Dates.getBeginOfDay(new Date());
        Date end = Dates.getEndOfDay(new Date());
        Map<String, Object> time = new HashMap<>();
        time.put("$gt", start);
        time.put("$lt", end);

        Query<Order> query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE).filter("orderSetStatus <>", OrderSetStatus.REMOVED);

        if (orderQueryVO != null) {
            if (StringUtils.isNotBlank(orderQueryVO.orderSetStatus) && !"-1".equals(orderQueryVO.orderSetStatus)) {//有集包状态
                query.filter("orderSetStatus", OrderSetStatus.status2Obj(Numbers.parseInt(orderQueryVO.orderSetStatus, 0)));
            }
            //揽件入库 根据揽件员的id 查询
            if (StringUtils.isNotBlank(orderQueryVO.embraceId) && !"-1".equals(orderQueryVO.embraceId) && !"0".equals(orderQueryVO.embraceId)) {
                query.filter("embraceId", orderQueryVO.embraceId);
            } else {
                //揽件入库 的状态查询
                if ("0".equals(orderQueryVO.type)) {//今日成功接单数

                    query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE).filter("orderSetStatus <>", OrderSetStatus.REMOVED);
                    query.filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE);

                    Map<String, Object> siteTimeCatch = new HashMap<>();
                    siteTimeCatch.put("siteId", orderQueryVO.user.getSite().getId().toHexString());
                    siteTimeCatch.put("orderSetStatus", OrderSetStatus.WAITTOIN);
                    siteTimeCatch.put("dateAdd", time);
                    Map<String, Object> elemQuery = new HashMap<>();
                    elemQuery.put("$elemMatch", siteTimeCatch);
                    query.filter("siteTimes", elemQuery);

                } else if ("1".equals(orderQueryVO.type) || "3".equals(orderQueryVO.type)) {//未入库
                    if ("3".equals(orderQueryVO.type) && StringUtils.isNotBlank(orderQueryVO.between)) {//今日未入库
                        DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                        query.filter("dateUpd >=", dateBetween.getStart());
                        query.filter("dateUpd <=", dateBetween.getEnd());
                    }

                    if ("1".equals(orderQueryVO.user.getSite().getType())) {//分拨站点
                        query.or(query.criteria("sender.city").equal(orderQueryVO.user.getSite().getCity())
                                        .criteria("disAreaCode").equal(orderQueryVO.user.getSite().getAreaCode())
                                        .criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED),
                                query.criteria("tradeStationId").equal(orderQueryVO.user.getSite().getId().toHexString()).criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN));
                    } else {
                        query.filter("tradeStationId", orderQueryVO.user.getSite().getId().toHexString()).filter("orderSetStatus", OrderSetStatus.WAITTOIN);
                    }
                } else if ("2".equals(orderQueryVO.type)) {//今日已入库
                    query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE)
                            .filter("orderSetStatus <>", OrderSetStatus.REMOVED).filter("orderSetStatus <>", OrderSetStatus.WAITTOIN);
                    ;
                    Map<String, Object> siteTimeCatch = new HashMap<>();
                    siteTimeCatch.put("siteId", orderQueryVO.user.getSite().getId().toHexString());

                    List<OrderSetStatus> orderSetStatusList = Lists.newArrayList();
                    orderSetStatusList.add(OrderSetStatus.WAITSET);
                    orderSetStatusList.add(OrderSetStatus.ARRIVEDISPATCH);
                    orderSetStatusList.add(OrderSetStatus.ARRIVED);
                    Map<String, Object> inQuery = new HashMap<>();
                    inQuery.put("$in", orderSetStatusList);
                    siteTimeCatch.put("orderSetStatus", inQuery);
                    siteTimeCatch.put("dateAdd", time);
                    Map<String, Object> elemQuery = new HashMap<>();
                    elemQuery.put("$elemMatch", siteTimeCatch);
                    query.filter("siteTimes", elemQuery);

                } else {
                    if ("1".equals(orderQueryVO.user.getSite().getType())) {//分拨站点
                        query.or(query.criteria("sender.city").equal(orderQueryVO.user.getSite().getCity())
                                        .criteria("disAreaCode").equal(orderQueryVO.user.getSite().getAreaCode())
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITSET)
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITDRIVERGETED),
                                query.criteria("tradeStationId").equal(orderQueryVO.user.getSite().getId().toHexString()));
                    } else {
                        query.filter("tradeStationId", orderQueryVO.user.getSite().getId().toHexString());
                    }
                }

            }
        }
        return query;
    }

    /**
     * 揽件入库
     * 根据站点下的用户列表获取该站点 揽件的订单数量
     *
     * @param user 当前用户
     * @return
     */
    public OrderHoldToStoreNumVO getOrderHoldToStoreNum(User user) {
        Date start = Dates.getBeginOfDay(new Date());
        Date end = Dates.getEndOfDay(new Date());
        Map<String, Object> time = new HashMap<>();
        time.put("$gt", start);
        time.put("$lt", end);

        OrderHoldToStoreNumVO orderHoldToStoreNumVO = new OrderHoldToStoreNumVO();
        Query<Order> query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE).filter("orderSetStatus <>", OrderSetStatus.REMOVED);
        if ("1".equals(user.getSite().getType())) {//分拨站点
            query.or(query.criteria("sender.city").equal(user.getSite().getCity())
                            .criteria("disAreaCode").equal(user.getSite().getAreaCode())
                            .criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED),
                    query.criteria("tradeStationId").equal(user.getSite().getId().toHexString()).criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN));
        } else {
            query.filter("tradeStationId", user.getSite().getId().toHexString()).filter("orderSetStatus", OrderSetStatus.WAITTOIN);
        }
        // 历史未入库订单数
        orderHoldToStoreNumVO.setHistoryToStoreNum(count(query));

        //今日未入库订单数
        query.filter("dateUpd >=", start);
        query.filter("dateUpd <=", end);
        orderHoldToStoreNumVO.setTodayNoToStoreNum(count(query));

        //今日成功接单数
        query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE).filter("orderSetStatus <>", OrderSetStatus.REMOVED);
        query.filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE);

        Map<String, Object> siteTimeCatch = new HashMap<>();
        siteTimeCatch.put("siteId", user.getSite().getId().toHexString());
        siteTimeCatch.put("orderSetStatus", OrderSetStatus.WAITTOIN);
        siteTimeCatch.put("dateAdd", time);
        Map<String, Object> elemQuery = new HashMap<>();
        elemQuery.put("$elemMatch", siteTimeCatch);
        query.filter("siteTimes", elemQuery);
        orderHoldToStoreNumVO.setSuccessOrderNum(count(query));

        // 今日已入库订单数
        query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE)
                .filter("orderSetStatus <>", OrderSetStatus.REMOVED).filter("orderSetStatus <>", OrderSetStatus.WAITTOIN);
        ;
        siteTimeCatch = new HashMap<>();
        siteTimeCatch.put("siteId", user.getSite().getId().toHexString());

        List<OrderSetStatus> orderSetStatusList = Lists.newArrayList();
        orderSetStatusList.add(OrderSetStatus.WAITSET);
        orderSetStatusList.add(OrderSetStatus.ARRIVEDISPATCH);
        orderSetStatusList.add(OrderSetStatus.ARRIVED);
        Map<String, Object> inQuery = new HashMap<>();
        inQuery.put("$in", orderSetStatusList);
        siteTimeCatch.put("orderSetStatus", inQuery);
        siteTimeCatch.put("dateAdd", time);
        elemQuery = new HashMap<>();
        elemQuery.put("$elemMatch", siteTimeCatch);
        query.filter("siteTimes", elemQuery);
//        if("1".equals(user.getSite().getType())){//分拨站点
//            query.or(
//                    query.criteria("sender.city").equal(user.getSite().getCity())
//                            .criteria("disAreaCode").equal(user.getSite().getAreaCode())
//                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
//                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITSET)
//                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITDRIVERGETED),
//                    query.criteria("tradeStationId").equal(user.getSite().getId().toHexString())
//                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
//            );
//        }else {
//            query.filter("tradeStationId", user.getSite().getId().toHexString()).filter("orderSetStatus <>",OrderSetStatus.WAITTOIN);
//        }

        orderHoldToStoreNumVO.setTodayToStoreNum(count(query));
        return orderHoldToStoreNumVO;
    }

    /**
     * 根据运单号查询
     *
     * @param mailNum
     * @return
     */
    public Order findOneByMailNum(String mailNum) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(mailNum))
            query.filter("mailNum", mailNum);
        return findOne(query);
    }

    /**
     * 查询指定mailNum集合的订单中物流状态不为expressStatus的订单的条数
     *
     * @param mailNumList     mailNum集合
     * @param orderStatusList 订单状态集合
     * @return 订单的条数
     */
    public long selectCountByMailNumsAndExpressStatus(BasicDBList mailNumList, List<OrderStatus> orderStatusList) {
        Query<Order> query = createQuery();
        if (mailNumList != null && mailNumList.size() > 0) {
            query.filter("mailNum in", mailNumList);
        }
        if (orderStatusList != null) {
            query.filter("orderStatus nin", orderStatusList);
        }
        return count(query);
    }

    /**
     * 根据订单号或运单号查询
     *
     * @param keyword
     * @return
     */
    public Order findByOrderNoOrMailNum(String keyword) {
        //创建查询条件
        Query<Order> query = createQuery();
        query.or(query.criteria("orderNo").equal(keyword), query.criteria("mailNum").equal(keyword));
        return findOne(query);
    }

    /**
     * 根据areaCode和mailNumList查询
     *
     * @param areaCode
     * @param mailNumList
     * @return
     */
    public List<Order> selectByAreaCodeAndMailNums(String areaCode, BasicDBList mailNumList) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(areaCode)) {
            query.filter("areaCode", areaCode);
        }
        if (mailNumList != null && mailNumList.size() > 0) {
            query.filter("mailNum in", mailNumList);
        }
        return find(query).asList();
    }

    /**
     * 根据站点和状态分组统计(缺少历史未到站 && 已到站订单数 && 转其他站点的订单数) -- 多个站点
     *
     * @param dateArrived  到站时间
     * @param areaCodeList 站点编号集合
     * @return Map<areaCode, MailStatisticVO>
     */
    public Map<String, MailStatisticVO> sumWithAreaCodesAndOrderStatus(String dateArrived, List<String> areaCodeList) {
        //Query<Order> query = this.getDatastore().getQueryFactory().createQuery(this.getDatastore());
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(dateArrived)) {
            Date date = Dates.parseDate(dateArrived);
            Date startDate = Dates.getBeginOfDay(date);
            Date endDate = Dates.getEndOfDay(date);
            query.filter("dateArrived >=", startDate).filter("dateArrived <=", endDate);
        }
        if (areaCodeList != null && areaCodeList.size() > 0) {
            query.filter("areaCode in", areaCodeList);
        }
        query.filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null);
        //select areaCode as abc , orderStatus as orderStatus, count(*) as countAll from order where dateArrived >= startDate and dateArrived <= endDate
        // and areaCode in areaCodeList and orderStatus <> OrderStatus.NOTARR and orderStatus <> null group by areaCode,orderStatus order by abc asc;
        AggregationPipeline pipeline = this.getDatastore().createAggregation(Order.class).match(query).group(id(grouping("areaCode"), grouping("orderStatus")), grouping("abc", first("areaCode")), grouping("orderStatus", first("orderStatus")), grouping("countAll", new Accumulator("$sum", 1))).sort(Sort.ascending("abc"));
        Iterator<OrderGroup> iterator = pipeline.aggregate(OrderGroup.class);
        //System.out.println(iterator.hasNext());
        Map<String, MailStatisticVO> map = new HashedMap<String, MailStatisticVO>();
        OrderGroup orderGroup = null;
        while (iterator.hasNext()) {
            orderGroup = iterator.next();
            //System.out.println(orderGroup);
            MailStatisticVO mailStatisticVO = map.get(orderGroup.getAbc());
            if (mailStatisticVO == null) {
                mailStatisticVO = new MailStatisticVO();
            }
            map.put(orderGroup.getAbc(), OrderGroup2MailStatisticVO(mailStatisticVO, orderGroup));
        }
        return map;
    }

    private MailStatisticVO OrderGroup2MailStatisticVO(MailStatisticVO expressStatStation, OrderGroup orderGroup) {
        if (orderGroup != null) {
            switch (orderGroup.getOrderStatus().getStatus()) {
                /*case 0 : //未到站
                    expressStatStation.setNostationcnt(orderGroup.getCountAll());
                    break;*/
                case 1:  //未分派
                    expressStatStation.setNoDispatch(orderGroup.getCountAll());
                    break;
                case 2:  //已分派
                    expressStatStation.setDispatched(orderGroup.getCountAll());
                    break;
                case 3:  //滞留
                    expressStatStation.setRetention(orderGroup.getCountAll());
                    break;
                case 4:  //拒收
                    expressStatStation.setRejection(orderGroup.getCountAll());
                    break;
                case 5:  //已签收
                    expressStatStation.setSigned(orderGroup.getCountAll());
                    break;
                case 6:  //已转其他快递
                    expressStatStation.setToOtherExpress(orderGroup.getCountAll());
                    break;
                /*case 7 :  //申请退货
                    expressStatStation(orderGroup.getCountAll());
                    break;
                case 8 :  //退货完成
                    expressStatStation(orderGroup.getCountAll());
                    break;*/
            }
            return expressStatStation;
        }


        return expressStatStation;
    }

    /**
     * 根据站点和状态分组统计(缺少历史未到站 && 已到站订单数 && 转其他站点的订单数)--单个站点
     *
     * @param dateArrived 到站时间
     * @param areaCode    站点编号
     * @return MailStatisticVO
     */
    public MailStatisticVO sumWithAreaCodeAndOrderStatus(String dateArrived, String areaCode) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(dateArrived)) {
            Date date = Dates.parseDate(dateArrived);
            Date startDate = Dates.getBeginOfDay(date);
            Date endDate = Dates.getEndOfDay(date);
            query.filter("dateArrived >=", startDate).filter("dateArrived <=", endDate);
        }
        if (StringUtils.isNotBlank(areaCode)) {
            query.filter("areaCode", areaCode);
        }
        query.filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null);
        //select areaCode as abc , orderStatus as orderStatus, count(*) as countAll from order where dateArrived >= startDate and dateArrived <= endDate
        // and areaCode = areaCode and orderStatus <> OrderStatus.NOTARR and orderStatus <> null group by areaCode,orderStatus order by abc asc;
        AggregationPipeline pipeline = this.getDatastore().createAggregation(Order.class).match(query).group(id(grouping("areaCode"), grouping("orderStatus")), grouping("abc", first("areaCode")), grouping("orderStatus", first("orderStatus")), grouping("countAll", new Accumulator("$sum", 1))).sort(Sort.ascending("abc"));
        Iterator<OrderGroup> iterator = pipeline.aggregate(OrderGroup.class);
        MailStatisticVO expressStatStation = new MailStatisticVO();
        while (iterator.hasNext()) {
            OrderGroup orderGroup = iterator.next();
            System.out.println(orderGroup);
            OrderGroup2MailStatisticVO(expressStatStation, orderGroup);
        }
        return expressStatStation;
    }

    /**
     * 根据站点编号集合和时间查询各个站点的不同状态的运单的汇总信息(缺少历史未到站 && 已到站订单数 && 转其他站点的订单数)
     *
     * @param areaCodeList 站点编号集合
     * @param dateArrived  到站时间
     * @return 不同状态的运单的汇总信息
     */
    public MailStatisticVO selectSummaryByAreaCodesAndTime(List<String> areaCodeList, String dateArrived) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(dateArrived)) {
            Date date = Dates.parseDate(dateArrived);
            Date startDate = Dates.getBeginOfDay(date);
            Date endDate = Dates.getEndOfDay(date);
            query.filter("dateArrived >=", startDate).filter("dateArrived <=", endDate);
        }
        if (areaCodeList != null && !areaCodeList.isEmpty()) {
            query.filter("areaCode in", areaCodeList);
        }
        query.filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null);
        //select orderStatus as orderStatus, count(*) as countAll from order where dateArrived >= startDate and dateArrived <= endDate
        // and areaCode in areaCodeList and orderStatus <> OrderStatus.NOTARR and orderStatus <> null group by orderStatus;
        AggregationPipeline pipeline = this.getDatastore().createAggregation(Order.class).match(query).group(id(grouping("orderStatus")), grouping("orderStatus", first("orderStatus")), grouping("countAll", new Accumulator("$sum", 1)));
        Iterator<OrderGroup> iterator = pipeline.aggregate(OrderGroup.class);
        //System.out.println(iterator.hasNext());
        MailStatisticVO expressStatStation = new MailStatisticVO();
        while (iterator.hasNext()) {
            OrderGroup orderGroup = iterator.next();
            OrderGroup2MailStatisticVO(expressStatStation, orderGroup);
        }
        return expressStatStation;
    }

    /**
     * 获取一个站点下,未进行打包的订单集合
     *
     * @param areaCode 站点编号
     * @return 订单集合
     */
    public List<Order> findNotDispatchOrdersWithAreaCode(String areaCode) {
        Query<Order> query = createQuery();
        query.filter("areaCode", areaCode);
        query.or(query.criteria("expressStatus").equal(ExpressStatus.Suspense), query.criteria("expressStatus").equal(ExpressStatus.Separating));
        return find(query).asList();
    }

    /**
     * 根据站点编号和物流状态分页查询
     *
     * @param areaCode      站点编号
     * @param expressStatus 物流状态
     * @param startNum      跳过的条数
     * @param pageSize      查询的条数
     * @return 分页数据
     */
    public PageModel<Order> selectPageByAreaCodeAndExpressStatus(String areaCode, ExpressStatus expressStatus, Integer startNum, Integer pageSize) {
        Query<Order> query = createQuery().order("-dateUpd");
        query.filter("areaCode", areaCode);
        if (expressStatus == ExpressStatus.Separating) {
            query.or(query.criteria("expressStatus").equal(ExpressStatus.Separating), query.criteria("expressStatus").equal(ExpressStatus.Suspense));
        } else {
            query.filter("expressStatus", expressStatus);
        }
        query.filter("printStatus", "printed");
        query.filter("isRemoved <>", 1);
        logger.info(query.toString());
        //分页信息
        if (startNum > -1 && pageSize > 0) {
            query.offset(startNum).limit(pageSize);
        }
        PageModel<Order> pageModel = new PageModel<Order>();
        pageModel.setDatas(find(query).asList());
        pageModel.setTotalCount(count(query));
        return pageModel;
    }

    /**
     * 此商户订单号下的所有已入库的运单
     *
     * @param tradeNo
     * @return
     */
    public long findArrCountByTradeNo(String tradeNo) {
        //创建查询条件
        Query<Order> query = createQuery();
        //商户订单号(我们自己生成的支付订单号)
        if (StringUtils.isNotBlank(tradeNo)) {
            query.filter("tradeNo", tradeNo);
        }
        query.filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE);
        query.filter("orderSetStatus <>", OrderSetStatus.WAITTOIN);
        query.filter("orderSetStatus <>", OrderSetStatus.REMOVED);
        query.filter("isRemoved", Constants.ISNOTREMOVED);
        return count(query);
    }

    /**
     * 根据查询条件，只查询tradeNo
     *
     * @param orderQueryVO 查询条件
     * @return order只有tradeNo有值的运单集合
     */
    public List<Order> findTradeNoList(OrderQueryVO orderQueryVO) {
        //设置查询条件
        Query<Order> query = getQuery(orderQueryVO).retrievedFields(true, "tradeNo");
        //查询数据
        List<Order> orderList = find(query).asList();
        return orderList;
    }

    /**
     * 根据物流状态查询订单
     *
     * @param pageModel 分页信息
     * @param remark    物流remark包含关键词
     * @param startDate
     * @param endDate
     * @return
     */
    public PageModel<Order> selectPageOrdersByExpress(PageModel<Order> pageModel, String remark, String startDate, String endDate) {
        //设置查询条件,并按照更新时间倒叙
        Query<Order> query = createQuery().field("tradeNo").contains("");

        //查询数据
        List<Order> orderList = find(query).asList();
        return pageModel;
    }

    public List<Order> findByDateAdd(Date dateAdd) {
        //创建查询条件
        Query<Order> query = createQuery();
        if (dateAdd != null) {
            //商户订单号(我们自己生成的支付订单号)
            Date startDate = Dates.getBeginOfDay(dateAdd);
            Date endDate = Dates.getEndOfDay(dateAdd);
            query.filter("dateAdd >=", startDate);
            query.filter("dateAdd <=", endDate);
        }
        return find(query).asList();
    }

    public Order findOneByMailNumLike(String mailNum) {
        //创建查询条件
        Query<Order> query = createQuery().field("mailNum").contains(mailNum);
        return findOne(query);
    }

    public PageModel<Order> selectPageByAppOrderQuery(AppOrderQueryVO appOrderQueryVO, String orderStr, Integer startNum, Integer pageSize){
        Query<Order> query = this.getAppQuery(appOrderQueryVO);
        logger.info("OrderDao.selectPageByAppOrderQuery().query="+query.toString());
        PageModel<Order> pageModel = new PageModel<Order>();
        pageModel.setTotalCount(count(query));
        //排序
        if(StringUtils.isNotBlank(orderStr)){
            query.order(orderStr);
        }else{
            query.order("-dateUpd");
        }
        //分页信息
        if(startNum > -1 && pageSize > 0){
            query.offset(startNum).limit(pageSize);
        }
        pageModel.setDatas(find(query).asList());
        return  pageModel;
    }
    private Query<Order> getAppQuery(AppOrderQueryVO appOrderQueryVO){
        Query<Order> query = createQuery();
        if(StringUtils.isNotBlank(appOrderQueryVO.areaCode)){
            query.filter("areaCode", appOrderQueryVO.areaCode);
        }
        if(StringUtils.isNotBlank(appOrderQueryVO.mailNum)){
            query.filter("mailNum", appOrderQueryVO.mailNum);
        }
        if(StringUtils.isNotBlank(appOrderQueryVO.userId)){
            query.filter("userId", appOrderQueryVO.userId);
        }
        if(appOrderQueryVO.isArrived != null && appOrderQueryVO.isArrived == 1){//
            query.filter("userId <>", null);
        }else {
            if(StringUtils.isNotBlank(appOrderQueryVO.dateArrived_min)){
                query.filter("dateArrived >=", Dates.parseDate(appOrderQueryVO.dateArrived_min+" 00:00:00", Constants.DATE_PATTERN_YMDT2));
            }
        }
        if(StringUtils.isNotBlank(appOrderQueryVO.dateUpd_min)){
            query.filter("dateUpd >=", Dates.parseDate(appOrderQueryVO.dateUpd_min+" 00:00:00", Constants.DATE_PATTERN_YMDT2));
        }
        if(appOrderQueryVO.src != null){
            query.filter("src", appOrderQueryVO.src);
        }
        if(appOrderQueryVO.expressStatus != null){
            if(appOrderQueryVO.expressStatus  == ExpressStatus.Separating){
                query.or(query.criteria("expressStatus").equal(ExpressStatus.Separating), query.criteria("expressStatus").equal(ExpressStatus.Suspense));
            }else{
                query.filter("expressStatus", appOrderQueryVO.expressStatus);
            }
        }

        if(appOrderQueryVO.printStatus != null){
            query.filter("printStatus", appOrderQueryVO.printStatus);
        }
        if(appOrderQueryVO.isRemoved != -1){
            query.filter("isRemoved", appOrderQueryVO.isRemoved);
        }
        //app端异常订单查询
        if(appOrderQueryVO.errorStatus != null){
            if(appOrderQueryVO.errorStatus == -1){//全部（8-滞留，9-拒收）
                query.or(query.criteria("expressStatus").equal(ExpressStatus.Delay), query.criteria("expressStatus").equal(ExpressStatus.Refuse));
            }else{
                query.filter("expressStatus", ExpressStatus.status2Obj(appOrderQueryVO.errorStatus));
            }
        }
        return query;
    }
    public List<Order> selectListByAppOrderQuery(AppOrderQueryVO appOrderQueryVO, String orderStr){
        Query<Order> query = this.getAppQuery(appOrderQueryVO);
        logger.info("OrderDao.selectPageByAppOrderQuery().query="+query.toString());
        //排序
        if(StringUtils.isNotBlank(orderStr)){
            query.order(orderStr);
        }else{
            query.order("-dateUpd");
        }
        return  find(query).asList();
    }
    public long selectCountByAppOrderQuery(AppOrderQueryVO appOrderQueryVO){
        Query<Order> query = this.getAppQuery(appOrderQueryVO);
        logger.info("OrderDao.selectCountByAppOrderQuery().query="+query.toString());
        return  count(query);
    }
}
