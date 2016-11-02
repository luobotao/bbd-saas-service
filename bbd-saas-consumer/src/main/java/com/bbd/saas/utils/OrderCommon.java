package com.bbd.saas.utils;

import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.vo.Express;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luobotao on 16/6/29.
 */
public class OrderCommon {
    public static final Logger logger = LoggerFactory.getLogger(OrderCommon.class);
    /**
     * 增加订单物流信息
     * @param expressStatus 物流状态
     * @param order 订单
     * @param user 当前用户
     * @param remark 物流信息
     */
    public static  void addOrderExpress(ExpressStatus expressStatus, Order order, User user, String remark){
        //更新物流状态
        order.setExpressStatus(expressStatus);
        //更新物流信息
        List<Express> expressList = order.getExpresses();
        if(expressList == null){
            expressList = new ArrayList<Express>();
        }
        Express express = new Express();
        express.setDateAdd(new Date());
        express.setRemark(remark);
        express.setLat(user.getSite().getLat());
        express.setLon(user.getSite().getLng());
        boolean expressIsNotAdd = true;//防止多次添加
        //检查是否添加过了
        if(!expressList.isEmpty()){
            Express express1 = expressList.get(expressList.size()-1);
            if (express.getRemark().equals(express1.getRemark())) {
                expressIsNotAdd = false;
            }
        }
        if (expressIsNotAdd) {//防止多次添加
            expressList.add(express);
            order.setExpresses(expressList);
        }
    }
}
