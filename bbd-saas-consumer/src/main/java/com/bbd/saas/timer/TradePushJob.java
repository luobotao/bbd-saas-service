package com.bbd.saas.timer;

import com.bbd.saas.api.mongo.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 运单push逻辑
 */
@Component("tradePushJob")
public class TradePushJob {
    public static final Logger logger = LoggerFactory.getLogger(TradePushJob.class);
    @Autowired
    TradeService tradeService;

    //每隔60秒跑一次
    //@Scheduled(cron = "*/60 * * * * ?")
    public void doJobWithPushAllTrade() {
        logger.info("运单数据推送给揽件员");
        tradeService.doJobWithAllPushTrade();//抢单
        //tradeService.doJobWithAllPushTradeOneByOne();//一个一个的推单
    }
}