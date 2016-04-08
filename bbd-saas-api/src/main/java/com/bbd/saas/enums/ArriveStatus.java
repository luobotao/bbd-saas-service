package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 包裹到站状态
 * Created by luobotao on 2016/4/8.
 */
public enum ArriveStatus {

    NOTARR(0, "未到站"),
    ARRIVED(1, "已扫描到站");
    private int status;
    private String message;
    private ArriveStatus(int status, String message) {
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
        ArriveStatus[] arriveStatus = ArriveStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (ArriveStatus as : arriveStatus) {
            if (id == as.status) {
                sb.append(Htmls.generateSelectedOption(as.status,as.message));
            } else {
                sb.append(Htmls.generateOption(as.status, as.message));
            }
        }
        return sb.toString();
    }
    public static ArriveStatus status2Obj(int value) {
        ArriveStatus[] arriveStatus = ArriveStatus.values();
        for (ArriveStatus as : arriveStatus) {
            if (value == as.status) {
                return as;
            }
        }
        return null;
    }
}
