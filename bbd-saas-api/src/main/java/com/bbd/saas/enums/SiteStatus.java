package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 站点审核状态
 * Created by luobotao on 2016/4/8.
 */
public enum SiteStatus {

    WAIT(1, "待审核"),
    APPROVE(2, "审核通过"),
    REFUND(3, "驳回");
    private int status;
    private String message;

    private SiteStatus(int status, String message) {
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
        SiteStatus[] srcs = SiteStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (SiteStatus s : srcs) {
            if (id == s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static SiteStatus status2Obj(int value) {
        SiteStatus[] stas = SiteStatus.values();
        for (SiteStatus s : stas) {
            if (value == s.status) {
                return s;
            }
        }
        return null;
    }
}
