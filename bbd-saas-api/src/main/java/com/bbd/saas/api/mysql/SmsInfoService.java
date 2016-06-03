package com.bbd.saas.api.mysql;


import com.bbd.saas.models.SmsInfo;

/**
 * 短信接口
 * Created by luobotao on 2016/4/11.
 */
public interface SmsInfoService {

    /**
     * 发送验证码
     * @param phone 手机号
     * @param code 验证码
     * @param type // 1普通短信 2营销短信 3语音短信
     * @return 短信
     */
    SmsInfo saveVerify(String phone, String code, String type);


}
