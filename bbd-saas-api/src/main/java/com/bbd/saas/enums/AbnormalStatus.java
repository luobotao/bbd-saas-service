package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 包裹异常状态
 * Created by luobotao on 2016/4/8.
 */
public enum AbnormalStatus {

	RETENTION(3, "滞留"),
    REJECTION(4, "拒收");
    private int status;
    private String message;
    private AbnormalStatus(int status, String message) {
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
        AbnormalStatus[] abnormalStatus = AbnormalStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (AbnormalStatus abs : abnormalStatus) {
            if (id == abs.status) {
                sb.append(Htmls.generateSelectedOption(abs.status,abs.message));
            } else {
                sb.append(Htmls.generateOption(abs.status, abs.message));
            }
        }
        return sb.toString();
    }
    public static AbnormalStatus status2Obj(int value) {
    	AbnormalStatus[] abnormalStatus = AbnormalStatus.values();
        for (AbnormalStatus abs : abnormalStatus) {
            if (value == abs.status) {
                return abs;
            }
        }
        return null;
    }
}
