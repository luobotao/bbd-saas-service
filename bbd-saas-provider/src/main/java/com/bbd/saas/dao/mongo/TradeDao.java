package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.TradeStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.PageModel;
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
        if(StringUtils.isNotBlank(tradeQueryVO.uId)){//用户ID
            query.filter("uId", tradeQueryVO.uId);
        }
        if(StringUtils.isNotBlank(tradeQueryVO.tradeNo)){//商户订单号
            query.filter("tradeNo", tradeQueryVO.tradeNo);
        }
        if(StringUtils.isNotBlank(tradeQueryVO.tradeNoLike)){//商户订单号模糊查询
            query.and(query.criteria("tradeNo").containsIgnoreCase(tradeQueryVO.tradeNoLike));
        }
        if(StringUtils.isNotBlank(tradeQueryVO.dateAddBetween)){//下单时间
            DateBetween dateBetween = new DateBetween(tradeQueryVO.dateAddBetween);
            query.filter("dateAdd >=", dateBetween.getStart());
            query.filter("dateAdd <=", dateBetween.getEnd());
        }
        if(tradeQueryVO.tradeNoSet != null && tradeQueryVO.tradeNoSet.size() > 0){//订单号集合
            query.filter("tradeNo in", tradeQueryVO.tradeNoSet);
        }
        if(tradeQueryVO.tradeStatus != null){//商户订单状态
            query.filter("tradeStatus", tradeQueryVO.tradeStatus);
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
            List<Trade> tradeList = find(query.offset(pageIndex * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();
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
    public UpdateResults updateTradeStatusByTradeId(String tradeId, String tradeStatus) {
        Query<Trade> query = createQuery();
        query.filter("_id", new ObjectId(tradeId));
        UpdateOperations<Trade> ops = createUpdateOperations().set("tradeStatus",tradeStatus).set("dateUpd",new Date());
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
    public long selectCountByUidAndStatus(String uId, TradeStatus tradeStatus){
        Query<Trade> query = createQuery();
        if(StringUtils.isNotBlank(uId)){//用户ID
            query.filter("uId", uId);
        }
        if(tradeStatus != null){//商户订单状态
            query.filter("tradeStatus", tradeStatus);
        }
        return count(query);
    }


}
