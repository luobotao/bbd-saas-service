package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 退货原因
 * Created by liyanlei on 2016/4/11.
 */
public enum ReturnReason {
	RECIEVER_REJECTION(0, "收件人拒收退回"),
    SENDER_INFO(1, "发件人通知退回"),
    CUSTOMER_INFO_ERROR(2, "客户电话、地址错误无法派送退回"),
    RETENTION_TIMEOUT(3, "滞留超出派送时效退回"),
    OTHER(4, "其他");
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
        sb.append(Htmls.generateOption(-1, "请选择"));
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
