package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 站点类型
 * Created by liyanlei on 2016/8/15.
 */
public enum SiteType {
    ORDERNARY(1, "普通"),
    SOCIAL_CAPACITY(2, "社会化运力"),
    EXPRESS_CABINET(3, "快递柜");
    private int status;
    private String message;

    private SiteType(int status, String message) {
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
        SiteType[] srcs = SiteType.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (SiteType s : srcs) {
            if (id == s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static SiteType status2Obj(int value) {
        SiteType[] stas = SiteType.values();
        for (SiteType s : stas) {
            if (value == s.status) {
                return s;
            }
        }
        return null;
    }
}
