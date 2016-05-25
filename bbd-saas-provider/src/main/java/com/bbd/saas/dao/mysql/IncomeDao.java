package com.bbd.saas.dao.mysql;

import java.util.Map;


public interface IncomeDao {

    /**
     * 向司机返现
     * @param parms
     */
    void driverIncome( Map<String, Object> parms );
}
