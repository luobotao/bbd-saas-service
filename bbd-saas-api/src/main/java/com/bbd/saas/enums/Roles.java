package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 包裹到站状态
 * Created by liyanlei on 2016/4/11.
 */
public enum Roles {
	SITEMASTER(0, "站长"),
    SENDMEM(1, "派件员");
    private int status;
    private String message;
    private Roles(int status, String message) {
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
        Roles[] returnReason = Roles.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (Roles rr : returnReason) {
            if (id == rr.status) {
                sb.append(Htmls.generateSelectedOption(rr.status,rr.message));
            } else {
                sb.append(Htmls.generateOption(rr.status, rr.message));
            }
        }
        return sb.toString();
    }
    public static Roles status2Obj(int value) {
    	Roles[] returnReason = Roles.values();
        for (Roles rr : returnReason) {
            if (value == rr.status) {
                return rr;
            }
        }
        return null;
    }
}
