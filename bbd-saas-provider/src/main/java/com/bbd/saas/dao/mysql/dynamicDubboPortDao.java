package com.bbd.saas.dao.mysql;

import java.util.Map;


public interface dynamicDubboPortDao {

    /**
     * 向司机返现
     * @param parms
     */
    void driverIncome(Map<String, Object> parms);
}
