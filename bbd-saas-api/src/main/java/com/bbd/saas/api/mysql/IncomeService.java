package com.bbd.saas.api.mysql;


/**
 * 返现接口
 * Created by luobotao on 2016/5/25.
 */
public interface IncomeService {

    /**
     * 向司机返现
     * @param driverId 司机Id
     * @param price 价格
     * @param trackNo 运单号
     */
    void driverIncome(int driverId,double price,String trackNo );


}
