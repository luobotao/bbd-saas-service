package com.bbd.saas.vo;

/**
 * Created by huozhijie on 2016/5/26.
 */
public class ResultResposeDTO {

    private  boolean result;/*result: "true"表示成功，false表示失败*/

    private   int returnCode;/*200: 提交成功， 500: 服务器错误,其他错误自行定义*/

    private  String  message;/*信息*/

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
