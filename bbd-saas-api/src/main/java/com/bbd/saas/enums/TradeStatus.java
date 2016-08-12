package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 商户订单状态
 * Created by luobotao on 2016/6/13.
 */
public enum TradeStatus {

    WAITPAY(0, "待支付"),
    WAITCATCH(1, "待接单"),
    WAITGET(2, "待取件"),
    GETED(3, "待入库"),
    ARRIVED(4, "已到站"),
    CANCELED(5, "已取消"),
    RETURNED(6, "已退款"),
    FINISH(7, "已完成"),
    LASTOPER(8, "待接单");
    private int status;
    private String message;
    private TradeStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public int getStatus() {
        return status;
    }
    public static String Srcs2HTML(Integer id) {
        StringBuilder sb = new StringBuilder();
        TradeStatus[] arriveStatus = TradeStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (TradeStatus as : arriveStatus) {
            if (id == as.status) {
                sb.append(Htmls.generateSelectedOption(as.status,as.message));
            } else {
                sb.append(Htmls.generateOption(as.status, as.message));
            }
        }
        return sb.toString();
    }
    public static TradeStatus status2Obj(int value) {
        TradeStatus[] arriveStatus = TradeStatus.values();
        for (TradeStatus as : arriveStatus) {
            if (value == as.status) {
                return as;
            }
        }
        return null;
    }
}
