package com.bbd.saas.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huozhijie on 2016/5/26.
 */

/*请求参数封装类*/
public class RestRequestDTO {


    /*监控状态:polling:监控中，shutdown:结束，abort:中止，updateall：重新推送。
    其中当快递单为已签收时status=shutdown，当message为“3天查询无记录”
    或“60天无变化时”status= abort  */
    private String status;


    /*监控状态相关消息，如:3天查询无记录，60天无变化*/
    private String message;

    /*最新查询结果，全量，倒序（即时间最新的在最前）*/
    private  LastResultVO  lastResult ;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LastResultVO getLastResult() {
        return lastResult;
    }

    public void setLastResult(LastResultVO lastResult) {
        this.lastResult = lastResult;
    }
}
