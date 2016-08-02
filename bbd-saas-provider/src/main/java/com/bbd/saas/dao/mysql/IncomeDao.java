package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.SmsInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;


public interface IncomeDao {

    /**
     * 向司机返现
     *
     * @param parms
     */
    void driverIncome(Map<String, Object> parms);

    /**
     *
     * @param parms
     */
    void expresstomysql(Map<String, Object> parms);

//    @Select("select * from smsinfo where id= #{id}")
//    SmsInfo findSmsInfo(@Param("id")String id);
}
