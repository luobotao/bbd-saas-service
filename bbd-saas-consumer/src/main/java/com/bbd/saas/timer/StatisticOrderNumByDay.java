package com.bbd.saas.timer;

import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.OrderLogService;
import com.bbd.saas.models.OrderLog;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.Express;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @description: Created by liyanlei on 2016/5/9 15:20.
 */
public class StatisticOrderNumByDay {
    public static final Logger logger = LoggerFactory.getLogger(StatisticOrderNumByDay.class);
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    OrderService orderService;
    @Autowired
    SiteService siteService;

    //每个凌晨1点统计昨天的数据
    @Scheduled(cron = "0 23 20 * * ?")
    public void sysOrderExpressToMysql() {
        logger.info("把当天的更新的订单的物流信息同步到mysql数据库中 trigger start ...");
        try {
            List<Site> siteList = siteService.findAllSiteList();
            List<Express> expressList = null;
            if (siteList != null && siteList.size() > 0) {
                for (Site site : siteList) {
                    List<Order> orderList = orderService.getTodayUpdateOrdersBySite(site.getAreaCode());
                    if (orderList != null && orderList.size() > 0) {
                        for (Order order : orderList) {
                            if (order != null && order.getExpresses() != null && order.getExpresses().size() > 0) {
                                expressList = order.getExpresses();
                                if (expressList != null && expressList.size() > 0) {
                                    for (Express express : expressList) {
                                        OrderLog orderLog = new OrderLog();
                                        orderLog.setAreaCode(order.getAreaCode());
                                        orderLog.setDate(express.getDateAdd());
                                        orderLog.setStatus(getStatusByRemark(express.getRemark()));
                                        orderLog.setOrderNo(order.getOrderNo());
                                        orderLog.setMailNum(order.getMailNum());
                                        orderLogService.insert(orderLog);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("把订单物流状态同步到mysql库出错：" + e.getMessage());
        }
        logger.info("把当天的更新的订单的物流信息同步到mysql数据库中 trigger end。");
    }

    public void saveToMysql(Express express, Order order) {

    }
    //0-未到达站点，1-已到达站点，2-正在派送，3-已签收，4-已滞留，5-已拒收，6-转站, 7-已丢失,8-已取消

    /**
     * 根据remark获取状态值
     * @param remark 物流备注
     * @return 状态值
     */
    int getStatusByRemark(String remark) {
        if(remark == null){
            return 0;
        }
        if (remark.contains("已到达站点")) {
            return 1;
        } else if (remark.contains("正在派送")) {
            return 2;
        } else if (remark.contains("签收")) { //您的订单已完成签收
            return 3;
        } else if (remark.contains("已滞留")) {//您的订单已滞留
            return 4;
        } else if (remark.contains("被拒收")) {//您的订单被拒收
            return 5;
        } else if (remark.contains("转送到")) {
            return 6;
        } else if (remark.contains("丢失")) {
            return 7;
        }else if (remark.contains("取消")) {
            return 8;
        }else{
            return 0; //未到站
        }
    }
}