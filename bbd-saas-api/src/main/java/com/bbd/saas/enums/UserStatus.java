package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 用户状态
 * Created by liyanlei on 2016/4/11.
 */
public enum UserStatus {

	VALID(1,"有效"),
    INVALID(3,"无效");
    private int status;
    private String message;
    private UserStatus(int status, String message) {
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
        UserStatus[] packageStatus = UserStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (UserStatus ps : packageStatus) {
            if (id == ps.status) {
                sb.append(Htmls.generateSelectedOption(ps.status,ps.message));
            } else {
                sb.append(Htmls.generateOption(ps.status, ps.message));
            }
        }
        return sb.toString();
    }
    public static UserStatus status2Obj(int value) {
    	UserStatus[] packageStatus = UserStatus.values();
        for (UserStatus ps : packageStatus) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
}
