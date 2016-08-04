package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

import java.util.Arrays;

/**
 * 订单状态
 * Created by luobotao on 2016/4/7.
 */
public enum OrderStatus {
    NOTARR(0, "未到站"),
    NOTDISPATCH(1, "未分派"),
    DISPATCHED(2, "已分派"),
    RETENTION(3, "滞留"),
    REJECTION(4, "拒收"),
    SIGNED(5, "已签收"),
    TO_OTHER_EXPRESS(6, "已转其他快递"),
    APPLY_RETURN(7, "申请退货"),
    RETURNED(8, "退货完成");
    private int status;
    private String message;
    private OrderStatus(int status, String message) {
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
        OrderStatus[] orderEnum = OrderStatus.values();
        sb.append(Htmls.generateOption(-1, "默认全部"));
        for (OrderStatus ps : orderEnum) {
            if (id == ps.status) {
                sb.append(Htmls.generateSelectedOption(ps.status,ps.message));
            } else {
                sb.append(Htmls.generateOption(ps.status, ps.message));
            }
        }
        return sb.toString();
    }
    public static OrderStatus status2Obj(int value) {
        OrderStatus[] status = OrderStatus.values();
        for (OrderStatus ps : status) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
    public static String Srcs2MultiHTML(Integer[] status) {
        StringBuilder sb = new StringBuilder();
        OrderStatus[] orderEnum = OrderStatus.values();
        sb.append(Htmls.generateMultiSelectedOptionAll("statusOpt", -1, "全部"));
        for (OrderStatus ps : orderEnum) {
            if(ps.getStatus()==NOTARR.getStatus()){
                continue;
            }
            if (Arrays.asList(status).contains(ps.status)) {
                sb.append(Htmls.generateMultiSelectedOption("statusOpt",ps.status,ps.message));
            } else {
                sb.append(Htmls.generateMultiOption("statusOpt",ps.status, ps.message));
            }
        }
        return sb.toString();
    }
    
}
