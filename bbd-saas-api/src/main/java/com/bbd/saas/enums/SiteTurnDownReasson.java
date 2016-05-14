package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 站点审核状态
 * Created by luobotao on 2016/4/8.
 */
public enum SiteTurnDownReasson {

    NOTTHISCOMPANY(1, "此站点不属于本公司"),
    SITENAMEERROR(2, "站点名称不正确"),
    SITEADDERROR(3, "站点地址不正确"),
    MASTERINFOERROR(4, "站长信息不正确"),
    OTHER(5, "其他");
    private int status;
    private String message;

    private SiteTurnDownReasson(int status, String message) {
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
        SiteTurnDownReasson[] srcs = SiteTurnDownReasson.values();
        for (SiteTurnDownReasson s : srcs) {
            if (id == s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static SiteTurnDownReasson status2Obj(int value) {
        SiteTurnDownReasson[] stas = SiteTurnDownReasson.values();
        for (SiteTurnDownReasson s : stas) {
            if (value == s.status) {
                return s;
            }
        }
        return null;
    }
    public static SiteTurnDownReasson objStr2Obj(String obj) {
        SiteTurnDownReasson[] stas = SiteTurnDownReasson.values();
        for (SiteTurnDownReasson s : stas) {
            if (obj.equals(s.toString()) ) {
                return s;
            }
        }
        return null;
    }
}
