package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.TradeStatus;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.PropertiesLoader;
import com.bbd.saas.vo.TradeQueryVO;
import org.apache.commons.lang3.StringUtils;
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
 * Created by liyanlei on 2016/6/13.
 * tradeDao
 */
@Repository
public class TradeDao extends BaseDAO<Trade, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(TradeDao.class);



    TradeDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    private Query<Trade> getQuery(TradeQueryVO tradeQueryVO){
        Query<Trade> query = createQuery();
        if(tradeQueryVO.uId != null){//用户ID
            query.filter("uId", tradeQueryVO.uId);
        }
        if(tradeQueryVO.tradeStatus != null && tradeQueryVO.tradeStatus != -1){//商户订单状态
            if(tradeQueryVO.tradeStatus == TradeStatus.CANCELED.getStatus()){
                query.or(query.criteria("tradeStatus").equal(TradeStatus.CANCELED),
                        query.criteria("tradeStatus").equal(TradeStatus.RETURNED));

            }else {
                query.filter("tradeStatus", TradeStatus.status2Obj(tradeQueryVO.tradeStatus));
            }
        }
        if(StringUtils.isNotBlank(tradeQueryVO.tradeNo)){//商户订单号
            query.filter("tradeNo", tradeQueryVO.tradeNo);
        }
        if(StringUtils.isNotBlank(tradeQueryVO.tradeNoLike)){//商户订单号模糊查询
            query.and(query.criteria("tradeNo").containsIgnoreCase(tradeQueryVO.tradeNoLike));
        }
//        if(StringUtils.isNotBlank(tradeQueryVO.noLike)){//商户运单号模糊查询
//            query.or(query.criteria("tradeNo").containsIgnoreCase(tradeQueryVO.noLike),
//                    query.criteria("orderSnaps.mailNum").containsIgnoreCase(tradeQueryVO.noLike));
//        }
        if(tradeQueryVO.tradeNoList!=null && tradeQueryVO.tradeNoList.size()>0){
            query.filter("tradeNo in", tradeQueryVO.tradeNoList);
        }
        if(StringUtils.isNotBlank(tradeQueryVO.rcvKeyword)){
            query.or(query.criteria("orderSnaps.reciever.phone").containsIgnoreCase(tradeQueryVO.rcvKeyword),
                    query.criteria("orderSnaps.reciever.name").containsIgnoreCase(tradeQueryVO.rcvKeyword),
                    query.criteria("orderSnaps.reciever.province").containsIgnoreCase(tradeQueryVO.rcvKeyword),
                    query.criteria("orderSnaps.reciever.city").containsIgnoreCase(tradeQueryVO.rcvKeyword),
                    query.criteria("orderSnaps.reciever.area").containsIgnoreCase(tradeQueryVO.rcvKeyword),
                    query.criteria("orderSnaps.reciever.address").containsIgnoreCase(tradeQueryVO.rcvKeyword));
        }
        if(StringUtils.isNotBlank(tradeQueryVO.dateAddStart)){//下单时间
            query.filter("dateAdd >=", Dates.strToDate(tradeQueryVO.dateAddStart));
        }
        if(StringUtils.isNotBlank(tradeQueryVO.dateAddEnd)){//下单时间
            query.filter("dateAdd <", Dates.addOneDay(tradeQueryVO.dateAddEnd));
        }
        if(tradeQueryVO.tradeNoSet != null && tradeQueryVO.tradeNoSet.size() > 0){//订单号集合
            query.filter("tradeNo in", tradeQueryVO.tradeNoSet);
        }
        return  query;
    }
    /**
     * 根据查询条件和站点状态获取商户订单列表信息
     * @param pageIndex 当前页
     * @param tradeQueryVO 查询条件
     * @return 分页对象（分页信息和当前页的数据）
     */
    public PageModel<Trade> findTradePage(Integer pageIndex,TradeQueryVO tradeQueryVO) {
        PageModel<Trade> pageModel = new PageModel<Trade>();
        if(tradeQueryVO!=null){
            Query<Trade> query = getQuery(tradeQueryVO);
            //设置排序
            //query.order("-dateUpd");
            List<Trade> tradeList = find(query.offset(pageIndex * pageModel.getPageSize()).order("-dateUpd").limit(pageModel.getPageSize())).asList();
            pageModel.setDatas(tradeList);
            pageModel.setTotalCount(count(query));
        }
    	return pageModel;
    }

    /**
     * 根据指定条件查询商户订单列表
     * @param tradeQueryVO 指定查询条件
     * @return  商户订单列表
     */
    public List<Trade> findTradeListByQuerys(TradeQueryVO tradeQueryVO) {
    	Query<Trade> query = getQuery(tradeQueryVO);
        return  find(query).asList();
    }
    

    /**
     * 根据商户订单名id去更新此商户订单的状态
     * @param tradeId 商户订单名id
     * @param tradeStatus 订单状态
     */
    public UpdateResults updateTradeStatusByTradeId(String tradeId, TradeStatus tradeStatus) {
        Query<Trade> query = createQuery();
        query.filter("_id", new ObjectId(tradeId));
        UpdateOperations<Trade> ops = createUpdateOperations().set("tradeStatus", tradeStatus).set("dateUpd",new Date());
        return update(query,ops);
    }

    /**
     * 根据商户订单id删除
     * @param tradeId 商户订单id
     */
    public void delTradesBySiteId(String tradeId) {
        Query<Trade> query = createQuery();
        query.filter("_id", new ObjectId(tradeId));
        deleteByQuery(query);
    }
    public long selectCountByUidAndStatus(ObjectId uId, TradeStatus tradeStatus){
        Query<Trade> query = createQuery();
        if(uId != null){//用户ID
            query.filter("uId", uId);
        }
        if(tradeStatus != null && tradeStatus == TradeStatus.CANCELED){//商户订单状态
            query.or(query.criteria("tradeStatus").equal(TradeStatus.CANCELED),
                    query.criteria("tradeStatus").equal(TradeStatus.RETURNED));
        }else {
            query.filter("tradeStatus", tradeStatus);
        }
        return count(query);
    }


    public List<Trade> findTradeListByPushJob(int bbdTradePushCount) {
        Query<Trade> query = createQuery();
        //待接单
        query.filter("tradeStatus", TradeStatus.WAITCATCH);
        /*query.filter("pushCount <=", bbdTradePushCount);
        query.or(query.criteria("postmanId").doesNotExist(),
                query.criteria("postmanId").containsIgnoreCase(""));*/
        return find(query).asList();
    }
    /**
     * 根据embraceId查询出Trade
     * @param embraceId
     * @return
     */
    public List<Trade> findTradesByEmbraceId(String embraceId) {
        Query<Trade> query = createQuery();
        query.filter("embraceId", embraceId);
        return find(query).asList();
    }
}
