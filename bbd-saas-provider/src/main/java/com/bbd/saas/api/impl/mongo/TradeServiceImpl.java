package com.bbd.saas.api.impl.mongo;
import com.bbd.saas.api.mongo.TradeService;
import com.bbd.saas.dao.mongo.OrderDao;
import com.bbd.saas.dao.mongo.TradeDao;
import com.bbd.saas.dao.mongo.UserDao;
import com.bbd.saas.enums.TradeStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.TradeQueryVO;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import java.util.*;

/**
 * Created by liyanlei on 2016/6/13.
 * trade接口(包裹)
 */
public class TradeServiceImpl implements TradeService {
    private TradeDao tradeDao;
    private OrderDao orderDao;
    private UserDao userDao;

    public TradeDao getTradeDao() {
        return tradeDao;
    }

    public void setTradeDao(TradeDao tradeDao) {
        this.tradeDao = tradeDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 根据id查询
     * @param tradeId  商户订单id
     * @return  商户订单
     */
    @Override
    public Trade findOne(String tradeId) {
        if (tradeId == null || "".equals(tradeId)) {
            return null;
        }
        return tradeDao.findOne("_id", new ObjectId(tradeId));

    }

    @Override
    public Trade findOneByTradeNo(String tradeNo) {
        //订单信息
        Trade trade = tradeDao.findOne("tradeNo", tradeNo);
        if (trade != null){
            //快件数量（预计或者实取）
            if(trade.getTradeStatus() == TradeStatus.WAITPAY){//待支付，订单还没有进入order表
                trade.setTotalMail(trade.getOrderSnaps().size());
            } else { //从order表中查询，因为有移动端移除的情况 -- 接口实现待修改
                trade.setTotalMail(orderDao.findCountByTradeNo(trade.getTradeNo()));
            }
        }
        return trade;
    }

    /**
     * 根据查询条件和订单状态获取商户订单列表信息
     * @param pageIndex 当前页
     * @param tradeQueryVO 查询条件
     * @param rcvKeyword 收件人查询关键词
     * @return 分页对象（分页信息和当前页的数据）
     */
    //@Override
    public PageModel<Trade> findTradePage2(Integer pageIndex, TradeQueryVO tradeQueryVO, String rcvKeyword){
        //查询运单号包含tradeQueryVO.tradeNo && 收件人手机号、姓名、地址中包含rcvKeyword的运单的订单号集合tradeNoSet
        List<Order> orderList = orderDao.findOrderList(tradeQueryVO.uId, tradeQueryVO.tradeNoLike, rcvKeyword);
        Set<String> tradeNoSet = new HashSet<String>();
        if(orderList != null && orderList.size() > 0){
            for (Order order : orderList){
                if(order != null && order.getTradeNo() != null){
                    tradeNoSet.add(order.getTradeNo());
                }
            }
        }
        //根据uId、tradeNo、dateAddBetween，tradeStatus查询订单
        tradeQueryVO.tradeNoSet = tradeNoSet;
        tradeQueryVO.tradeNoLike = null;
        PageModel<Trade> tradePageModel = tradeDao.findTradePage(pageIndex, tradeQueryVO);
        //设置快件数量和揽件人、状态
        List<Trade> tradeList = tradePageModel.getDatas();
        if (tradeList != null && tradeList.size() > 0){
            for (Trade trade : tradeList){
                //快件数据量
                trade.setTotalMail(orderDao.findCountByTradeNo(trade.getTradeNo()));
                //揽件人
                trade.setEmbrace(userDao.findOne("_id", trade.getEmbraceId()));
                //状态
                trade.setStatusMsg(trade.getTradeStatus().getMessage());
            }
        }
        return tradePageModel;
    }
    /**
     * 根据查询条件和订单状态获取商户订单列表信息
     * @param pageIndex 当前页
     * @param tradeQueryVO 查询条件
     * @return 分页对象（分页信息和当前页的数据）
     */
    @Override
    public PageModel<Trade> findTradePage(Integer pageIndex, TradeQueryVO tradeQueryVO){
        //查询订单号或者运单号包含tradeQueryVO.tradeNoLike && 运单的收件人手机号、姓名、地址中包含rcvKeyword的订单
        PageModel<Trade> tradePageModel = tradeDao.findTradePage(pageIndex, tradeQueryVO);
        //设置快件数量和揽件人、状态
        List<Trade> tradeList = tradePageModel.getDatas();
        if (tradeList != null && tradeList.size() > 0){
            for (Trade trade : tradeList){
                //快件数据量
                if(trade.getTradeStatus() == TradeStatus.WAITPAY){//待支付，订单还没有进入order表
                    trade.setTotalMail(trade.getOrderSnaps().size());
                } else { //从order表中查询，因为有移动端移除的情况 -- 接口实现待修改
                    trade.setTotalMail(orderDao.findCountByTradeNo(trade.getTradeNo()));
                }
                //揽件人
                if(trade.getEmbraceId() != null){
                    trade.setEmbrace(userDao.findOne("_id", trade.getEmbraceId()));
                }
                //状态
                trade.setStatusMsg(trade.getTradeStatus().getMessage());
            }
        }
        return tradePageModel;
    }
    /**
     * 根据指定条件查询商户订单列表
     * @param tradeQueryVO 指定查询条件
     * @return  商户订单列表
     */
    @Override
    public List<Trade> findTradeListByQuerys(TradeQueryVO tradeQueryVO) {
        return tradeDao.findTradeListByQuerys(tradeQueryVO);
    }
    /**
     * 根据指定条件查询商户订单号集合
     * @param tradeQueryVO 指定查询条件
     * @return  商户订单号集合
     */
    @Override
    public Set<String> findTradeNoSetByQuerys(TradeQueryVO tradeQueryVO) {
        List<Trade> tradeList = tradeDao.findTradeListByQuerys(tradeQueryVO);
        Set<String> tradeNoSet = new HashSet<String>();
        if(tradeList != null && tradeList.size() > 0){
            for (Trade trade : tradeList){
                if(trade != null && trade.getTradeNo() != null){
                    tradeNoSet.add(trade.getTradeNo());
                }
            }
        }
        return  tradeNoSet;
    }

    /**
     * 保存商户订单对象
     * @param trade 商户订单
     * @return Key<Trade>保存结果
     */
    @Override
    public Key<Trade> save(Trade trade) {
        return tradeDao.save(trade);
    }

    /**
     * 根据商户订单名id去更新此商户订单的状态
     * @param tradeId 商户订单名id
     * @param tradeStatus 订单状态
     */
    @Override
    public void updateTradeStatusByTradeId(String tradeId, TradeStatus tradeStatus) {
        tradeDao.updateTradeStatusByTradeId(tradeId, tradeStatus);
    }

    /**
     * 根据商户订单id删除
     * @param tradeId 商户订单id
     */
    @Override
    public void delTradesBySiteId(String tradeId) {
        tradeDao.delTradesBySiteId(tradeId);
    }

    /**
     * 根据trade对象删除商户订单
     * @param trade
     */
    @Override
    public void delTrade(Trade trade) {
        tradeDao.delete(trade);
    }

    /**
     * 根据用户Id获得该用户不同状态的订单的数量
     * @return {WAITPAY:待支付；WAITCATCH：待接单；WAITGET:待取件； GETED:已取件；CANCELED:已取消}
     */
    @Override
    public Map<String, Long> findDiffStatusCountByUid(ObjectId uId) {
        Map<String, Long> map = new HashMap<String, Long>();
        //WAITPAY:待支付
        map.put("WAITPAY", tradeDao.selectCountByUidAndStatus(uId, TradeStatus.WAITPAY));
        //WAITCATCH：待接单
        map.put("WAITCATCH", tradeDao.selectCountByUidAndStatus(uId, TradeStatus.WAITCATCH));
        //WAITGET:待取件
        map.put("WAITGET", tradeDao.selectCountByUidAndStatus(uId, TradeStatus.WAITGET));
        //GETED:已取件
        map.put("GETED", tradeDao.selectCountByUidAndStatus(uId, TradeStatus.GETED));
        //CANCELED:已取消
        map.put("CANCELED", tradeDao.selectCountByUidAndStatus(uId, TradeStatus.CANCELED));
        return map;
    }
    /**
     * 根据用户Id、订单状态获取订单的数量
     * @return 订单数量
     */
    @Override
    public long findCountByUidAndStatus(ObjectId uId, TradeStatus tradeStatus) {
        return tradeDao.selectCountByUidAndStatus(uId, tradeStatus);
    }
}
