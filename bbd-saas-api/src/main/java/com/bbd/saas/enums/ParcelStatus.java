package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 包裹状态--用于数据查询页面
 * Created by liyanlei on 2016/4/11.
 */
public enum ParcelStatus {
    Suspense(0, "待打包"),
    Separating(1, "已打包"),
    DriverGeted(2, "司机已取货"),
    ArriveStation(3, "已到达站点");

    private int status;
    private String message;

    private ParcelStatus(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static String stas2HTML(int value) {
        StringBuilder sb = new StringBuilder();
        ParcelStatus[] stas = ParcelStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (ParcelStatus s : stas) {
            if (value==s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static String stas2Message(int status) {
        ParcelStatus[] stas = ParcelStatus.values();
        for (ParcelStatus s : stas) {
            if (status==s.status) {
                return s.getMessage();
            }
        }
        return "";
    }

    public static ParcelStatus status2Obj(int value) {
        ParcelStatus[] stas = ParcelStatus.values();
        for (ParcelStatus s : stas) {
            if (value==s.status) {
                return s;
            }
        }
        return null;
    }
}
