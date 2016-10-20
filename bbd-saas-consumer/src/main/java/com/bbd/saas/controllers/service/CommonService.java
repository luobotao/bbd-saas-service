package com.bbd.saas.controllers.service;

import com.bbd.saas.api.mongo.ExpressExchangeService;
import com.bbd.saas.api.mongo.OrderParcelService;
import com.bbd.saas.api.mysql.ExpressCompanyService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.enums.ExpressExchangeStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.mongoModels.ExpressExchange;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.vo.Express;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *  with redis
 * Created by liyanlei on 2016/8/18.
 */
@Service("commonService")
public class CommonService {
    public static final Logger logger = LoggerFactory.getLogger(CommonService.class);


    @Autowired
    PostmanUserService postmanUserService;
    @Autowired
    OrderParcelService orderParcelService;
    @Autowired
    ExpressExchangeService expressExchangeService;
    @Autowired
    ExpressCompanyService expressCompanyService;

    @Value("${EPREE100_KEY}")
    private String EPREE100_KEY ;
    @Value("${EPREE100_CALLBACK_URL}")
    private String EPREE100_CALLBACK_URL ;
    @Value("${EPREE100_URL}")
    private String EPREE100_URL;





    /**
     * 设置物流信息
     * @param remark 物流描述
     * @param pmu_lat 当前登录的派件员纬度
     * @param pmu_lon 当前登录的派件员经度
     * @param lat 参数_纬度
     * @param lon 参数—经度
     * @return 物流信息
     */
    public Express getExpress(String remark, Double pmu_lat, Double pmu_lon, String lat, String lon) {
        Express express = new Express();
        express.setDateAdd(new Date());
        express.setRemark(remark);
        if (StringUtils.isNotBlank(lat) || lat.indexOf("E") >= 0) {
            express.setLat(lat);
        } else {
            express.setLat(pmu_lat + "");
        }
        if (StringUtils.isNotBlank(lon) || lon.indexOf("E") >= 0) {
            express.setLon(lon);
        } else {
            express.setLon(pmu_lon + "");
        }
        return express;
    }

    /**
     * 运单添加物流信息
     * @param order 运单
     * @param express 物流信息
     * @return 运单
     */
    public Order addExpressToOrder(Order order, Express express) {
        if (order.getExpresses() != null) {
            order.getExpresses().add(express);
        } else {
            List<Express> expressList = Lists.newArrayList();
            expressList.add(express);
            order.setExpresses(expressList);
        }
        return order;
    }

    /**
     * 运单添加物流信息 -- 防止进入多次物流信息
     * @param order 运单
     * @param express 物流信息
     * @return 运单
     */
    public Order addSingleExpressToOrder(Order order, Express express) {
        if (order.getExpresses() != null) {
            //防止进入多次物流信息
            boolean flag = true;
            for (Express exp : order.getExpresses()) {
                if (exp.getRemark().equals(express.getRemark())) {  // 防止进入多次物流信息
                    flag = false;
                    break;
                }
            }
            if (flag) {
                order.getExpresses().add(express);
            }
        } else {
            List<Express> expressList = Lists.newArrayList();
            expressList.add(express);
            order.setExpresses(expressList);
        }
        return order;
    }

    /**
     * 物流同步
     * @param order 订单
     * @param realName 派件员姓名
     * @param phone 派件员电话
     */
    public void saveExpressExChange(Order order, String realName, String phone){
        if (order.getSrc() == Srcs.PINHAOHUO || order.getSrc() == Srcs.DANGDANG || order.getSrc() == Srcs.DDKY) {
            this.doSaveExpressExChange(order, realName, phone);
        }
    }

    /**
     * 物流同步
     * @param order 订单
     * @param realName 派件员姓名
     * @param phone 派件员电话
     */
    public void doSaveExpressExChange(Order order, String realName, String phone){
        ExpressExchange expressExchange = new ExpressExchange();
        expressExchange.setOperator(realName);
        expressExchange.setPhone(phone);
        expressExchange.setOrder(order.coverOrderVo());
        expressExchange.setStatus(ExpressExchangeStatus.waiting);
        expressExchange.setDateAdd(new Date());
        expressExchangeService.save(expressExchange);
    }

    /**
     * 获得详细地址字符串
     * @param province 省
     * @param city 市
     * @param district 区
     * @param address 详细地址
     * @return 详细地址字符串
     */
    public String getAddress(String province, String city, String district, String address, String jointStr){
        StringBuffer address_SB= new StringBuffer("");
        if(!com.alibaba.dubbo.common.utils.StringUtils.isBlank(province)){
            address_SB.append(province);
            address_SB.append(jointStr);
        }
        if(!com.alibaba.dubbo.common.utils.StringUtils.isBlank(city)){
            address_SB.append(city);
            address_SB.append(jointStr);
        }
        if(!com.alibaba.dubbo.common.utils.StringUtils.isBlank(district)){
            address_SB.append(district);
            address_SB.append(jointStr);
        }
        if(!com.alibaba.dubbo.common.utils.StringUtils.isBlank(address)){
            address_SB.append(address);
        }
        return address_SB.toString();
    }


}
