package com.bbd.saas.dao.mysql;


import com.bbd.saas.models.SmsInfo;

import java.util.Map;

public interface SmsInfoDao {


    /**
     * 插入短信实体
     * @param smsInfo
     * @return
     */
    void insertSmsinfo(SmsInfo smsInfo);

    /**
     * 根所手机号与ip检查是否可以发送短信
     *
     * @param parms
     * @return
     */
    String checkToSendsms(Map<String, Object> parms);
}
