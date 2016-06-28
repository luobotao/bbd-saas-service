package com.bbd.saas.timer;

import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.TradeService;
import com.bbd.saas.api.mysql.OrderLogService;
import com.bbd.saas.enums.TradeStatus;
import com.bbd.saas.models.OrderLog;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.vo.Express;
import com.bbd.saas.vo.TradeQueryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 运单push逻辑
 */
@Component("tradePushJob")
public class TradePushJob {
    public static final Logger logger = LoggerFactory.getLogger(TradePushJob.class);
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    OrderService orderService;
    @Autowired
    SiteService siteService;
    @Autowired
    TradeService tradeService;

    //每个20秒跑一次
    @Scheduled(cron = "*/20 * * * * ?")
    public void doJobWithPushAllTrade() {
        logger.info("运单数据推送给揽件员");
        try {
            //获取到所有需要推送的Trade信息
            List<Trade> tradeList = tradeService.findTradeListByPushJob();
            if(tradeList!=null&&tradeList.size()>0){
                for (Trade trade : tradeList) {
                    tradeService.doJobWithPushTrade(trade);
                }
            }else{
                logger.info("暂无订单需要推送给揽件员");
            }
            logger.info("一波订单推送揽件员完成");
        } catch (Exception e) {
            logger.error("把订单物流状态同步到mysql库出错：" + e.getMessage());
        }
    }


}