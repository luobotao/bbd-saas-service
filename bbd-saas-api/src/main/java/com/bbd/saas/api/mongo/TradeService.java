package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.TradeStatus;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.TradeQueryVO;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liyanlei on 2016/6/13.
 * 商户订单trade接口(包裹)
 */
public interface TradeService {

    /**
     * 根据id查询
     * @param tradeId  商户订单id
     * @return  商户订单
     */
    Trade findOne(String tradeId);
    /**
     * 根据tradeNo查询
     * @param tradeNo  商户订单号
     * @return  商户订单
     */
    Trade findOneByTradeNo(String tradeNo);

    /**
     * 根据查询条件和站点状态获取商户订单列表信息
     * @param pageIndex 当前页
     * @param tradeQueryVO 查询条件
     * @param rcvKeyword 收件人查询关键词
     * @return 分页对象（分页信息和当前页的数据）
     */
    public PageModel<Trade> findTradePage(Integer pageIndex, TradeQueryVO tradeQueryVO, String rcvKeyword);

    /**
     * 根据指定条件查询商户订单列表
     * @param tradeQueryVO 指定查询条件
     * @return  商户订单列表
     */
    public List<Trade> findTradeListByQuerys(TradeQueryVO tradeQueryVO);

    /**
     * 根据指定条件查询商户订单号集合
     * @param tradeQueryVO 指定查询条件
     * @return  商户订单号集合
     */
    public Set<String> findTradeNoSetByQuerys(TradeQueryVO tradeQueryVO);

    /**
     * 保存商户订单对象
     * @param trade 商户订单
     * @return Key<Trade>保存结果
     */
    Key<Trade> save(Trade trade);

    /**
     * 根据商户订单名id去更新此商户订单的状态
     * @param tradeId 商户订单名id
     * @param tradeStatus 订单状态
     */
    void updateTradeStatusByTradeId(String tradeId, TradeStatus tradeStatus);

    /**
     * 根据商户订单id删除
     * @param tradeId 商户订单id
     */
    void delTradesBySiteId(String tradeId);

    /**
     * 根据trade对象删除商户订单
     * @param trade  商户订单i
     */
    public void delTrade(Trade trade);

    /**
     * 根据用户Id获得该用户不同状态的订单的数量
     * @return {WAITPAY:待支付；WAITCATCH：待接单；WAITGET:待取件； GETED:已取件；CANCELED:已取消}
     */
    public Map<String, Long> findDiffStatusCountByUid(ObjectId uId);
    /**
     * 根据用户Id、订单状态获取订单的数量
     * @return 订单数量
     */
    public long findCountByUidAndStatus(ObjectId uId, TradeStatus tradeStatus);

}
