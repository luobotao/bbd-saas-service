package com.bbd.saas.vo;

/**
 * Created by huozhijie on 2016/5/31.
 */
public class Expree100ParametersVO {
    private String callbackurl;
    private String salt;
    private String resultv2 ;

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getResultv2() {
        return resultv2;
    }

    public void setResultv2(String resultv2) {
        this.resultv2 = resultv2;
    }
}
