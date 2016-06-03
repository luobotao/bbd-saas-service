package com.bbd.saas.vo;

/**
 * Created by huozhijie on 2016/5/27.
 */
public class NewContextVO {
        private String context;/*内容*/

        private String  time ; /*时间，原始格式*/

        private  String ftime; /*格式化后时间*/

        private String status;/*本数据元对应的签收状态。只有在开通签收状态服务
        且在订阅接口中提交resultv2标记后才会出现*/

       private String   areaCode;/*本数据元对应的行政区域的编码，只有在开通签收状态服务
       且在订阅接口中提交resultv2标记后才会出现*/

         private String  areaName;/*本数据元对应的行政区域的名称，开通签收状态服务
         且在订阅接口中提交resultv2标记后才会出现*/

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
