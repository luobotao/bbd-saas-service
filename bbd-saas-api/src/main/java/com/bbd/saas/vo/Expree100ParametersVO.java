package com.bbd.saas.vo;

/**封装parameter对象
 * Created by huozhijie on 2016/5/31.
 */
public class Expree100ParametersVO {
    /*  例子： param=body{
       "company": "yuantong",
               "number": "12345678",
               "from": "广东深圳",
               "to": "北京朝阳",
               "key": "qWmaQkHp269",
               "parameters": {
           "callbackurl": " http://baidu.com",
                   "salt": "any string",
                   "resultv2": "1"
       }
   }*/
    private String callbackurl;//回调地址
    private String salt;//签名用随机字符串（可选）
    private String resultv2 ;//添加此字段表示开通行政区域解析功能

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
