package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * Created by huozhijie on 2016/6/28.
 */
public class OrderHoldToStoreNumVO implements Serializable {
    private long  successOrderNum ;//今日成功接单数
    private long TodayNoToStoreNum;//今日未入库订单数
    private long historyToStoreNum;// 历史未入库订单数
    private long  todayToStoreNum; // 今日已入库订单数

    public long getSuccessOrderNum() {
        return successOrderNum;
    }

    public void setSuccessOrderNum(long successOrderNum) {
        this.successOrderNum = successOrderNum;
    }

    public long getTodayNoToStoreNum() {
        return TodayNoToStoreNum;
    }

    public void setTodayNoToStoreNum(long todayNoToStoreNum) {
        TodayNoToStoreNum = todayNoToStoreNum;
    }

    public long getHistoryToStoreNum() {
        return historyToStoreNum;
    }

    public void setHistoryToStoreNum(long historyToStoreNum) {
        this.historyToStoreNum = historyToStoreNum;
    }

    public long getTodayToStoreNum() {
        return todayToStoreNum;
    }

    public void setTodayToStoreNum(long todayToStoreNum) {
        this.todayToStoreNum = todayToStoreNum;
    }
}
