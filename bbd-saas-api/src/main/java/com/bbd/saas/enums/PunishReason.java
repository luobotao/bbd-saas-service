package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * @description: Created by liyanlei on 2016/9/27 16:28.
 * 投诉理由||处罚理由
 */
public enum PunishReason {
    XJQS(0, "虚假签收"),
    CZSWYXSX(1, "操作失误影响时效"),
    LRXXYSJBF(2, "录入信息与实际不符"),
    APPCZBGF(3, "APP操作不规范"),
    CWCNHWZQYDKH(4, "错误承诺或未正确引导客户"),
    WLXKH(5, "未联系客户/非本人签收"),
    WJSGJ(6, "未及时跟进/知会"),
    CZBFGZKJPS(7, "装卸/分拣操作不规范致快件破损"),
    DZJWBZPS(8, "到站件外包装破损未及时上报"),
    PJTZYS(9, "派件途中遗失"),
    ZDNYS(10, "站点内遗失"),
    WASPSZDCP(11, "未按时派送遭到催派"),
    YWPJ(12, "延误派件"),
    BSHSM(13, "不送货上门"),
    WZJL(14, "伪造记录"),
    YXJZBD(15, "言行举止不当"),
    WLYQKH(16, "无理要求客户"),
    YKHFSMC(17, "与客户发生摩擦"),
    XXBFHYQ(18, "形象不符合要求");

    private int status;
    private String message;

    private PunishReason(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static String Srcs2HTML(int value) {
        StringBuilder sb = new StringBuilder();
        PunishReason[] typs = PunishReason.values();
        sb.append(Htmls.generateOption(-1, "请选择"));
        for (PunishReason s : typs) {
            if (value == s.status) {
                sb.append(Htmls.generateSelectedOption(s.status, s.message));
            } else {
                sb.append(Htmls.generateOption(s.status, s.message));
            }
        }
        return sb.toString();
    }

    public static String status2Msg(int status) {
        PunishReason[] typs = PunishReason.values();
        for (PunishReason s : typs) {
            if (status == s.status) {
                return s.getMessage();
            }
        }
        return "";
    }
}
