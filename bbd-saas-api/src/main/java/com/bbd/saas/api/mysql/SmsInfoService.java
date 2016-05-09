package com.bbd.saas.api.mysql;


import com.bbd.saas.models.SmsInfo;

/**
 * 短信接口
 * Created by luobotao on 2016/4/11.
 */
public interface SmsInfoService {

    /**
     * 发送验证码
     * @param phone
     * @param code
     * @param type
     * @return
     */
    SmsInfo saveVerify(String phone, String code, String type);


}
