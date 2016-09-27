package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 投诉状态
 */
public enum ComplaintStatus {
    COMPLAINT_WAIT(0, "待处理投诉"),
    COMPLAINT_SUCCESS(1, "投诉成立"),
    COMPLAINT_CLOSE(2, "投诉关闭");
    private int status;
    private String message;

    private ComplaintStatus(int status, String message) {
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
        ComplaintStatus[] orderEnum = ComplaintStatus.values();
        sb.append(Htmls.generateOption(-1, "请选择"));
        for (ComplaintStatus ps : orderEnum) {
            if (id == ps.status) {
                sb.append(Htmls.generateSelectedOption(ps.status, ps.message));
            } else {
                sb.append(Htmls.generateOption(ps.status, ps.message));
            }
        }
        return sb.toString();
    }

    public static ComplaintStatus status2Obj(int value) {
        ComplaintStatus[] status = ComplaintStatus.values();
        for (ComplaintStatus ps : status) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
}
