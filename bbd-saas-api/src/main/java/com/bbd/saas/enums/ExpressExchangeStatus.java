package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 物流状态
 * Created by luobotao on 2016/4/8.
 */
public enum ExpressExchangeStatus {
    waiting(0, "新增物流推送"),
    pushing(1, "推送"),
    finish(2, "完成"),
    error(3,"错误"),
    nohandle(4,"不做处理");

    private int status;
    private String message;

    private ExpressExchangeStatus(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static String status2HTML(int value) {
        StringBuilder sb = new StringBuilder();
        ExpressExchangeStatus[] stas = ExpressExchangeStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (ExpressExchangeStatus s : stas) {
            if (value==s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }
    public static ExpressExchangeStatus status2Obj(int value) {
        ExpressExchangeStatus[] stas = ExpressExchangeStatus.values();
        for (ExpressExchangeStatus s : stas) {
            if (value==s.status) {
                return s;
            }
        }
        return null;
    }
    public static String stas2Message(int status) {
        ExpressExchangeStatus[] stas = ExpressExchangeStatus.values();
        for (ExpressExchangeStatus s : stas) {
            if (status==s.status) {
                return s.getMessage();
            }
        }
        return "";
    }
}
