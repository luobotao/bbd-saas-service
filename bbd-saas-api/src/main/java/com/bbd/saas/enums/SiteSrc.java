package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 站点来源
 * Created by liyanlei on 2016/10/13.
 */
public enum SiteSrc {
    PUBLIC(1, "公共的"),
    QXSH(2, "抢鲜生活"),
    OTHER(99, "其他");
    private int status;
    private String message;

    private SiteSrc(int status, String message) {
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
        SiteSrc[] srcs = SiteSrc.values();
        if(id == -1){
            sb.append(Htmls.generateOption("all", "全部"));
        }
        for (SiteSrc s : srcs) {
            if (id == s.status) {
                sb.append(Htmls.generateSelectedOptionString(s.toString(), s.message));
            } else {
                sb.append(Htmls.generateOptionString(s.toString(), s.message));
            }
        }
        return sb.toString();
    }

    public static SiteSrc status2Obj(int value) {
        SiteSrc[] stas = SiteSrc.values();
        for (SiteSrc s : stas) {
            if (value == s.status) {
                return s;
            }
        }
        return null;
    }
}
