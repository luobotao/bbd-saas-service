package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.*;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.OrderHoldToStoreNumVO;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.OrderUpdateVO;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
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
                    query.filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null);
                    query.filter("orderSetStatus",OrderSetStatus.ARRIVED);
                    if (StringUtils.isNotBlank(orderQueryVO.between)) {//到站时间
                        DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                        query.filter("dateArrived >=", dateBetween.getStart());
                        query.filter("dateArrived <=", dateBetween.getEnd());
                    }
                } else {//未到站
                    query.filter("orderStatus", OrderStatus.NOTARR);
                    query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING),query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN),query.criteria("orderSetStatus").equal(null));

                    if(StringUtils.isNotBlank(orderQueryVO.between)){//预计到站时间
                        DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                        query.filter("dateMayArrive >=", dateBetween.getStart());
                        query.filter("dateMayArrive <=", dateBetween.getEnd());
                    }
                }
            }else{//全部（已到站||未到站）
                query.filter("orderStatus <>", null);
                query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING),query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN),query.criteria("orderSetStatus").equal(OrderSetStatus.ARRIVED),query.criteria("orderSetStatus").equal(null));
                if(StringUtils.isNotBlank(orderQueryVO.between)){//预计到站时间
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
        Query<Order> queryArrive = createQuery().filter("areaCode", areaCode).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        query.filter("orderStatus", OrderStatus.NOTARR);
        query.or(query.criteria("orderSetStatus").equal(OrderSetStatus.DRIVERSENDING),query.criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN),query.criteria("orderSetStatus").equal(null));

        orderNumVO.setNoArriveHis(count(query));//历史未到站

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
        cal.add(Calendar.DAY_OF_YEAR,-1);
        queryArrive.filter("dateArrived <=",new Date()).filter("dateArrived >",cal.getTime()).filter("orderStatus <>", OrderStatus.NOTARR).filter("orderStatus <>", null);
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
        query.filter("mailNum",mailNum);
        if(orderStatusOld!=null){//旧状态不为空，则需要加入旧状态的判断
            query.or(query.criteria("orderStatus").equal(orderStatusOld),query.criteria("orderStatus").equal(null));
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
                } else if (orderQueryVO.arriveStatus == 1) {//已到站 即只要不是未到站，则全为已到站
                    query.filter("orderStatus <>", null).filter("orderStatus <>", OrderStatus.NOTARR);
                } else {//未到站
                    query.filter("orderStatus", OrderStatus.NOTARR);
                }
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
            } else {//数据查询页面查询订单某一状态的记录
                if (orderQueryVO.orderStatus == OrderStatus.NOTARR.getStatus()) {//未到站--OrderStatus=0
                    query.filter("orderStatus", OrderStatus.NOTARR);
                } else {//已到站
                    query.filter("orderStatus =", OrderStatus.status2Obj(orderQueryVO.orderStatus));
                    //到站时间，只有已到站的订单才会有到站时间
                    if (StringUtils.isNotBlank(orderQueryVO.arriveBetween)) {
                        DateBetween dateBetween = new DateBetween(orderQueryVO.arriveBetween);
                        query.filter("dateArrived >=", dateBetween.getStart());
                        query.filter("dateArrived <=", dateBetween.getEnd());
                    }
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
     * 根据站点编码和时间获取该站点已分派的订单数
     *
     * @param areaCode    站点编号
     * @param betweenTime 查询时间范围
     * @return
     */
    public long getDispatchedNums(String areaCode, String betweenTime) {
        OrderNumVO orderNumVO = new OrderNumVO();
        Query<Order> query = createQuery().filter("areaCode", areaCode).filter("mailNum <>", null).filter("mailNum <>", "");//运单号不能为空
        //
        if (StringUtils.isNotBlank(betweenTime)) {
            DateBetween dateBetween = new DateBetween(betweenTime);
            //expressQuery.criteria("expresses.dateAdd").greaterThanOrEq(dateBetween.getStart());
            //expressQuery.criteria("expresses.dateAdd").lessThanOrEq(dateBetween.getEnd());
            //物流状态--模糊查询
            /*expressQuery.and(query.criteria("expresses.remark").contains("正在派送"),
                    query.criteria("expresses.dateAdd").greaterThanOrEq(dateBetween.getStart()),
                    query.criteria("expresses.dateAdd").lessThanOrEq(dateBetween.getEnd())
            );*/
            //String  expressQuery =" {'dateAdd' : {'$gte' : {'$date' :'2016-04-27T16:00:00.000Z'}}} , {'dateAdd' : {'$lte' : {'$date' :'2016-05-09T15:59:59.000Z'}}}";
            BasicDBObject expressQuery = new BasicDBObject();
            expressQuery.put("remark", new BasicDBObject("$regex", "正在派送"));
            expressQuery.put("dateAdd", new BasicDBObject("$gte", dateBetween.getStart())
                    .append("$lte", dateBetween.getEnd()));  // i.e.   start <= dateAdd <= end
            query.filter("expresses elem", expressQuery);
        }
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
    public Order findOneByNewMailNum(String newMailNum) {
        Query<Order> query = createQuery();
        if (StringUtils.isNotBlank(newMailNum))
            query.filter("otherExprees.mailNum", newMailNum);
        return findOne(query);
    }

    /**
     * 查询指定mailNum集合的订单中物流状态不为expressStatus的订单的条数
     * @param mailNumList mailNum集合
     * @param expressStatus 物流状态
     * @return 订单的条数
     */
    public long selectCountByMailNumsAndExpressStatus(BasicDBList mailNumList, ExpressStatus expressStatus) {
        Query<Order> query = createQuery();
        if(mailNumList != null && mailNumList.size() > 0){
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
        if(removeStatus != null){
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
     * @param pageModel
     * @param orderQueryVO 封装的查询条件
     * @return
     */
    public PageModel<Order> findPageOrdersForHoldToStore(PageModel<Order> pageModel,OrderQueryVO orderQueryVO ) {

        //设置查询条件
        Query<Order> query = getQueryForHoldToStore( orderQueryVO );
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
     * @param orderQueryVO  订单的查询条件
     * @return
     */
    private Query<Order> getQueryForHoldToStore( OrderQueryVO orderQueryVO ) {
        Query<Order> query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE).filter("orderSetStatus <>", OrderSetStatus.REMOVED);

        if (orderQueryVO != null) {
            if (StringUtils.isNotBlank(orderQueryVO.orderSetStatus)&& !"-1".equals(orderQueryVO.orderSetStatus)) {//有集包状态
                query.filter("orderSetStatus", OrderSetStatus.status2Obj(Numbers.parseInt(orderQueryVO.orderSetStatus,0)));
            }


            //揽件入库 根据揽件员的id 查询
            if (StringUtils.isNotBlank(orderQueryVO.embraceId) && !"-1".equals(orderQueryVO.embraceId) && !"0".equals(orderQueryVO.embraceId)) {
                query.filter("embraceId", orderQueryVO.embraceId);
            }else {
                //揽件入库 的状态查询
                if("0".equals(orderQueryVO.type)){//今日成功接单数
                    query.filter("tradeStationId", orderQueryVO.user.getSite().getId().toHexString());
                }else if ("1".equals(orderQueryVO.type)||"3".equals(orderQueryVO.type)) {//未入库
                    if("1".equals(orderQueryVO.user.getSite().getType())){//分拨站点
                        query.or(query.criteria("sender.city").equal(orderQueryVO.user.getSite().getCity())
                                        .criteria("disAreaCode").equal(orderQueryVO.user.getSite().getAreaCode())
                                        .criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED),
                                query.criteria("tradeStationId").equal(orderQueryVO.user.getSite().getId().toHexString()).criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN));
                    }else {
                        query.filter("tradeStationId", orderQueryVO.user.getSite().getId().toHexString()).filter("orderSetStatus",OrderSetStatus.WAITTOIN);
                    }
                }else if("2".equals(orderQueryVO.type)){//已入库
                    if("1".equals(orderQueryVO.user.getSite().getType())){//分拨站点
                        query.or(
                                query.criteria("sender.city").equal(orderQueryVO.user.getSite().getCity())
                                        .criteria("disAreaCode").equal(orderQueryVO.user.getSite().getAreaCode())
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITSET)
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITDRIVERGETED),
                                query.criteria("tradeStationId").equal(orderQueryVO.user.getSite().getId().toHexString())
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
                        );
                    }else {
                        query.filter("tradeStationId", orderQueryVO.user.getSite().getId().toHexString()).filter("orderSetStatus <>",OrderSetStatus.WAITTOIN);
                    }
                }else {
                    if("1".equals(orderQueryVO.user.getSite().getType())){//分拨站点
                        query.or(query.criteria("sender.city").equal(orderQueryVO.user.getSite().getCity())
                                        .criteria("disAreaCode").equal(orderQueryVO.user.getSite().getAreaCode())
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITSET)
                                        .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITDRIVERGETED),
                                query.criteria("tradeStationId").equal(orderQueryVO.user.getSite().getId().toHexString()));
                    }else {
                        query.filter("tradeStationId", orderQueryVO.user.getSite().getId().toHexString());
                    }
                }

            }

            if (StringUtils.isNotBlank(orderQueryVO.between)) {//到站时间
                DateBetween dateBetween = new DateBetween(orderQueryVO.between);
                query.filter("dateUpd >=", dateBetween.getStart());
                query.filter("dateUpd <=", dateBetween.getEnd());
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
    public OrderHoldToStoreNumVO getOrderHoldToStoreNum(User user){
        OrderHoldToStoreNumVO orderHoldToStoreNumVO = new OrderHoldToStoreNumVO();
        Query<Order> query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE).filter("orderSetStatus <>", OrderSetStatus.REMOVED);
        if("1".equals(user.getSite().getType())){//分拨站点
            query.or(query.criteria("sender.city").equal(user.getSite().getCity())
                            .criteria("disAreaCode").equal(user.getSite().getAreaCode())
                            .criteria("orderSetStatus").equal(OrderSetStatus.DRIVERGETED),
                    query.criteria("tradeStationId").equal(user.getSite().getId().toHexString()).criteria("orderSetStatus").equal(OrderSetStatus.WAITTOIN));
        }else {
            query.filter("tradeStationId", user.getSite().getId().toHexString()).filter("orderSetStatus",OrderSetStatus.WAITTOIN);
        }
        // 历史未入库订单数
        orderHoldToStoreNumVO.setHistoryToStoreNum(count(query));

        Date start =  Dates.getBeginOfDay(new Date());
        Date end =  Dates.getEndOfDay(new Date());
        //今日未入库订单数
        query.filter("dateUpd >=",start);
        query.filter("dateUpd <=",end);
        orderHoldToStoreNumVO.setTodayNoToStoreNum(count(query));

        //今日成功接单数
        query = createQuery().filter("orderSetStatus <>", null).filter("tradeNo <>", null).filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE).filter("orderSetStatus <>", OrderSetStatus.REMOVED);
        query.filter("tradeStationId", user.getSite().getId().toHexString());
        query.filter("orderSetStatus <>", OrderSetStatus.NOEMBRACE);
        query.filter("dateUpd >=",start);
        query.filter("dateUpd <=",end);
        orderHoldToStoreNumVO.setSuccessOrderNum(count(query));
        // 今日已入库订单数
        if("1".equals(user.getSite().getType())){//分拨站点
            query.or(
                    query.criteria("sender.city").equal(user.getSite().getCity())
                            .criteria("disAreaCode").equal(user.getSite().getAreaCode())
                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITSET)
                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITDRIVERGETED),
                    query.criteria("tradeStationId").equal(user.getSite().getId().toHexString())
                            .criteria("orderSetStatus").notEqual(OrderSetStatus.WAITTOIN)
            );
        }else {
            query.filter("tradeStationId", user.getSite().getId().toHexString()).filter("orderSetStatus <>",OrderSetStatus.WAITTOIN);
        }

        query.filter("orderSetStatus <>", OrderSetStatus.WAITTOIN);
        orderHoldToStoreNumVO.setTodayToStoreNum(count(query));
        return orderHoldToStoreNumVO;
    }

    /**
     * 根据运单号查询
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
     * @param mailNumList mailNum集合
     * @param orderStatusList 订单状态集合
     * @return 订单的条数
     */
    public long selectCountByMailNumsAndExpressStatus(BasicDBList mailNumList, List<OrderStatus> orderStatusList) {
        Query<Order> query = createQuery();
        if(mailNumList != null && mailNumList.size() > 0){
            query.filter("mailNum in",mailNumList);
        }
        if(orderStatusList != null){
            query.filter("orderStatus nin", orderStatusList);
        }
        return count(query);
    }


    /**
     * 此商户订单号下的所有已入库的运单
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
     * @param pageModel 分页信息
     * @param remark 物流remark包含关键词
     * @param startDate
     * @param endDate
     * @return
     */
    public PageModel<Order> selectPageOrdersByExpress(PageModel<Order> pageModel, String remark, String startDate, String endDate){
        //设置查询条件,并按照更新时间倒叙
        Query<Order> query = createQuery().field("tradeNo").contains("");

        //查询数据
        List<Order> orderList = find(query).asList();
        return pageModel;
    }
}
