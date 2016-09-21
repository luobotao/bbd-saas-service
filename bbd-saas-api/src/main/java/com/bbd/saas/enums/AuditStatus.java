package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 转其他快递金额审核状态
 * Created by liyanlei on 2016/9/20.
 */
public enum AuditStatus {
    WAIT(0, "待审核"),
    PASS(1, "通过"),
    TURNDOWN(2, "驳回");
    private int status;
    private String message;

    private AuditStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static String Stas2HTML(Integer id) {
        StringBuilder sb = new StringBuilder();
        AuditStatus[] srcs = AuditStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (AuditStatus s : srcs) {
            if (id == s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static AuditStatus status2Obj(int value) {
        AuditStatus[] stas = AuditStatus.values();
        for (AuditStatus s : stas) {
            if (value == s.status) {
                return s;
            }
        }
        return null;
    }
}
