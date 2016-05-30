package com.bbd.saas.api.mysql;


/**
 * 返现接口
 * Created by luobotao on 2016/5/25.
 */
public interface IncomeService {

    /**
     * 向司机返现
     * @param driverId
     * @param price
     * @param trackNo
     */
    void driverIncome(int driverId,double price,String trackNo );


}
