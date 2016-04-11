package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 退货原因
 * Created by liyanlei on 2016/4/11.
 */
public enum ReturnReason {

	GOOD_DAMAGE(0, "货物破损"),
    TIMEOUT_DILIVERY(1, "超时配送"),
    CLIENT_RETURN(2, "客户端要求退换"),
    OTHER(3, "其他");
    private int status;
    private String message;
    private ReturnReason(int status, String message) {
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
        ReturnReason[] returnReason = ReturnReason.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (ReturnReason rr : returnReason) {
            if (id == rr.status) {
                sb.append(Htmls.generateSelectedOption(rr.status,rr.message));
            } else {
                sb.append(Htmls.generateOption(rr.status, rr.message));
            }
        }
        return sb.toString();
    }
    public static ReturnReason status2Obj(int value) {
    	ReturnReason[] returnReason = ReturnReason.values();
        for (ReturnReason rr : returnReason) {
            if (value == rr.status) {
                return rr;
            }
        }
        return null;
    }
}
