package com.bbd.saas.dao.mysql;


import com.bbd.saas.models.SmsInfo;

public interface SmsInfoDao {


    /**
     * 插入短信实体
     * @param smsInfo
     * @return
     */
    void insertSmsinfo(SmsInfo smsInfo);

}
