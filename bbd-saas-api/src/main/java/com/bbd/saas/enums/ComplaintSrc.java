package com.bbd.saas.enums;


/**
 * Created by ctt on 2016/11/17.
 */
public enum ComplaintSrc {
    /**
     * 投诉状态
     */
    KFCZTS(0, "客服操作投诉"),
    YHTS(1, "用户投诉");
    private int status;
    private String message;

    private ComplaintSrc(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static ComplaintSrc status2Obj(int value) {
        ComplaintSrc[] status = ComplaintSrc.values();
        for (ComplaintSrc ps : status) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
    
}
