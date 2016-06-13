package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 快递物流状态
 * Created by luobotao on 2016/4/8.
 */
public enum TransportStatus {
    Daixiadan(0, "待下单"),
    Daisijijiedan(1, "待司机接单"),
    Daisijiquhuo(2, "待司机取货"),
    Zhengzaiunshu(3, "正在运输"),
    ArriveStation(4, "已到达站点"),
    Cancel(5, "已取消");

    private int status;
    private String message;

    private TransportStatus(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static String status2HTML(int value) {
        StringBuilder sb = new StringBuilder();
        TransportStatus[] stas = TransportStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (TransportStatus s : stas) {
            if (value==s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }
    public static TransportStatus status2Obj(int value) {
        TransportStatus[] stas = TransportStatus.values();
        for (TransportStatus s : stas) {
            if (value==s.status) {
                return s;
            }
        }
        return null;
    }
    public static String stas2Message(int status) {
        TransportStatus[] stas = TransportStatus.values();
        for (TransportStatus s : stas) {
            if (status==s.status) {
                return s.getMessage();
            }
        }
        return "";
    }
}
