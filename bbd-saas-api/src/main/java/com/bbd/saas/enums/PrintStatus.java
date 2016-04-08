package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 打印状态
 * Created by luobotao on 2016/4/8.
 */
public enum PrintStatus {
    waitToPrint(1, "待打印"),
    printed(2, "已打印");

    private int status;
    private String message;

    private PrintStatus(int status, String message) {
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
        PrintStatus[] stas = PrintStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (PrintStatus s : stas) {
            if (value==s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static String stas2Message(int status) {
        PrintStatus[] stas = PrintStatus.values();
        for (PrintStatus s : stas) {
            if (status==s.status) {
                return s.getMessage();
            }
        }
        return "";
    }
    public static PrintStatus status2Obj(int value) {
        PrintStatus[] stas = PrintStatus.values();
        for (PrintStatus s : stas) {
            if (value==s.status) {
                return s;
            }
        }
        return null;
    }
}
