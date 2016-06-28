package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 揽件集包状态
 * Created by luobotao on 2016/6/24.
 */
public enum OrderSetStatus {

    NOEMBRACE(0, "待扫描"),
    SCANED(1, "已扫描"),
    WAITTOIN(2, "已取件,待入库"),
    WAITSET(3, "已入库"),
    WAITDRIVERGETED(4, "待司机取货送往分拨中心"),
    DRIVERGETED(5, "正在运输到分拨中心"),
    ARRIVEDISPATCH(6, "已到达分拨中心"),
    WAITDISPATCHSET(7, "待分拣集包"),
    WAITDRIVERTOSEND(8, "待司机取货送往配送点"),
    DRIVERSENDING(9, "正在运输到配送点"),
    ARRIVED(10, "已到达配送点");
    private int status;
    private String message;
    private OrderSetStatus(int status, String message) {
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
        OrderSetStatus[] orderEnum = OrderSetStatus.values();
        sb.append(Htmls.generateOption(-1, "默认全部"));
        for (OrderSetStatus ps : orderEnum) {
            if (id == ps.status) {
                sb.append(Htmls.generateSelectedOption(ps.status,ps.message));
            } else {
                sb.append(Htmls.generateOption(ps.status, ps.message));
            }
        }
        return sb.toString();
    }
    public static OrderSetStatus status2Obj(int value) {
        OrderSetStatus[] status = OrderSetStatus.values();
        for (OrderSetStatus ps : status) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
    
}
