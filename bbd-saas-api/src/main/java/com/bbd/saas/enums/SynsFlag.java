package com.bbd.saas.enums;

/**
 * 同步状态
 * Created by luobotao on 2016/4/8.
 */
public enum SynsFlag {
    WAITING("0", "待同步"),
    SUCCESS("1", "已同步"),
    FAIL("2", "同步失败");

    private String status;
    private String message;

    private SynsFlag(String status, String message) {
        this.message = message;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public static String stas2Message(String status) {
        SynsFlag[] stas = SynsFlag.values();
        for (SynsFlag s : stas) {
            if (s.status.equals(status)) {
                return s.getMessage();
            }
        }
        return "";
    }
}
