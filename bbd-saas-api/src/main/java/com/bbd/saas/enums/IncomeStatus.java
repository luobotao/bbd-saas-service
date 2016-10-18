package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 结算状态
 */
public enum IncomeStatus {
    WEIJIESUAN(0, "未结算"),
    YIJIESUAN(1, "已结算");
    private int status;
    private String message;

    IncomeStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static String status2HTML(Integer id) {
        StringBuilder sb = new StringBuilder();
        IncomeStatus[] orderEnum = IncomeStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (IncomeStatus ps : orderEnum) {
            if (id == ps.status) {
                sb.append(Htmls.generateSelectedOption(ps.status, ps.message));
            } else {
                sb.append(Htmls.generateOption(ps.status, ps.message));
            }
        }
        return sb.toString();
    }

    public static IncomeStatus status2Obj(int value) {
        IncomeStatus[] status = IncomeStatus.values();
        for (IncomeStatus ps : status) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }

}