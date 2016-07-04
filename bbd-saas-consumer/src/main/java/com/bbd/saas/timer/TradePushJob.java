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
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.vo.Express;
import com.bbd.saas.vo.TradeQueryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    TradeService tradeService;



    //每个20秒跑一次
    @Scheduled(cron = "*/20 * * * * ?")
    public void doJobWithPushAllTrade() {
        logger.info("运单数据推送给揽件员");
        tradeService.doJobWithAllPushTrade();

    }


}