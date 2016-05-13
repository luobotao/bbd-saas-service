package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.dao.mongo.OrderDao;
import com.bbd.saas.vo.OrderMonitorVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public class StatisticTaskJob {
    private OrderDao orderDao;

    /**
     * 统计每天不同状态的订单数
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public OrderMonitorVO taskJob() {
        OrderMonitorVO orderMonitorVO = new OrderMonitorVO();
        /*//未到站订单数
        orderMonitorVO.setNoArrive(orderDao);
        //已到站订单数
        orderMonitorVO.setArrived(0);
        //未分派
        orderMonitorVO.getNoDispatch(0);
        //已分派
        orderMonitorVO.setDispatched(0);
        //签收
        orderMonitorVO.setSigned();
        //滞留
        orderMonitorVO.setRetention();
        //拒收
        orderMonitorVO.setRejection();
        //转站
        orderMonitorVO.setToOtherSite();*/
        return null;
    }
}
