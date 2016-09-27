package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * //投诉类型
 */
public enum ComplaintType {
    BBDKJYW(0, "操作错误"),
    BBDKJPS(1, "遗失破损"),
    BBDKJDS(2, "时效类投诉"),
    BBDFWTDBL(3, "态度/行为类投诉"),
    BBDFBRQS(4, "其他");

    private int status;
    private String message;

    private ComplaintType(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static String types2HTML(int value) {
        StringBuilder sb = new StringBuilder();
        ComplaintType[] typs = ComplaintType.values();
        sb.append(Htmls.generateOption(-1, "请选择投诉类型"));
        for (ComplaintType s : typs) {
            if (value==s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static String types2Message(int status) {
        ComplaintType[] typs = ComplaintType.values();
        for (ComplaintType s : typs) {
            if (status==s.status) {
                return s.getMessage();
            }
        }
        return "";
    }
    public static ComplaintType status2Obj(int value) {
        ComplaintType[] status = ComplaintType.values();
        for (ComplaintType ps : status) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
}
