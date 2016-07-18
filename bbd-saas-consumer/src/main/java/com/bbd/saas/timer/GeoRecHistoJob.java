package com.bbd.saas.timer;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.utils.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 运单push逻辑
 */
@Component("geoRecHistoJob")
public class GeoRecHistoJob {
    public static final Logger logger = LoggerFactory.getLogger(GeoRecHistoJob.class);
    @Autowired
    OrderService orderService;



    //每天凌晨跑一次
    //@Scheduled(cron = "0 0 1 * * ?")
    public void doJobWithPushAllTrade() {
        logger.info("处理收件人信息");
        String date = Dates.formatDate2(new Date());
        //orderService.doJobWithAllRecGeo(new Date());
        logger.info("cover Reciver address to geo finish");
    }


}