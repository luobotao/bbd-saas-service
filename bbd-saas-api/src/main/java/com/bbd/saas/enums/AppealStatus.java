package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * //申诉状态
 */
public enum AppealStatus {
    NOAPPEAL(0, "未申诉"),
    APPEALED(1, "已申诉");
    private int status;
    private String message;

    private AppealStatus(int status, String message) {
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
        AppealStatus[] orderEnum = AppealStatus.values();
        sb.append(Htmls.generateOption(-1, "请选择"));
        for (AppealStatus ps : orderEnum) {
            if (id == ps.status) {
                sb.append(Htmls.generateSelectedOption(ps.status, ps.message));
            } else {
                sb.append(Htmls.generateOption(ps.status, ps.message));
            }
        }
        return sb.toString();
    }

    public static AppealStatus status2Obj(int value) {
        AppealStatus[] status = AppealStatus.values();
        for (AppealStatus ps : status) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
    public static String enumStr2Msg(String enumStr) {
        if(enumStr == null){
            return "";
        }
        AppealStatus[] status = AppealStatus.values();
        for (AppealStatus appealStatus : status) {
            if (appealStatus.toString().equals(enumStr)) {
                return appealStatus.getMessage();
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(enumStr2Msg("NOAPPEAL"));//NOAPPEAL(0, "未申诉"),
        System.out.println(enumStr2Msg("APPEALED"));//APPEALED(1, "已申诉");
    }
}
