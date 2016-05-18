package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 物流状态
 * Created by luobotao on 2016/4/8.
 */
public enum TransportStatus {
    Suspense(0, "待发货"),
    Separating(1, "分拣中"),
    Packed(2, "已打包"),
    DriverGeted(3, "司机已取货"),
    ArriveStation(4, "已到达站点"),
    Delivering(5, "正在派送"),
    Success(6, "已签收"),
    Cancel(7, "已取消"),
    Delay(8, "已滞留"),
    Refuse(9, "已拒收"),
    LOST(10, "已丢失");

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