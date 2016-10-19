package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 物流状态
 * Created by luobotao on 2016/4/8.
 */
public enum ExpressStatus {
    Suspense(0, "待发货"),
    OutHouse(14, "揽件出仓"),
    Separating(1, "分拣中"),
    Packed(2, "已打包"),
    DriverGeted(3, "司机已取货"),
    ArriveStation(4, "已到达站点"),
    Delivering(5, "正在派送"),
    Success(6, "已签收"),
    Cancel(7, "已取消"),
    Delay(8, "已滞留"),
    Refuse(9, "已拒收"),
    LOST(10, "已丢失"),
    APPLY_RETURN(11, "申请退货"),
    TO_OTHER_EXPRESS(13, "已转其他快递"),
    RETURNED(12, "退货完成");

    private int status;
    private String message;

    ExpressStatus(int status, String message) {
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
        ExpressStatus[] stas = ExpressStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (ExpressStatus s : stas) {
            if (value==s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }
    public static ExpressStatus status2Obj(int value) {
        ExpressStatus[] stas = ExpressStatus.values();
        for (ExpressStatus s : stas) {
            if (value==s.status) {
                return s;
            }
        }
        return null;
    }
    public static String stas2Message(int status) {
        ExpressStatus[] stas = ExpressStatus.values();
        for (ExpressStatus s : stas) {
            if (status==s.status) {
                return s.getMessage();
            }
        }
        return "";
    }
}
