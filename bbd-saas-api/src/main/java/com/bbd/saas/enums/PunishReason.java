package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * @description: Created by liyanlei on 2016/9/27 16:28.
 * 投诉理由||处罚理由
 */
public enum PunishReason {
    XJQS(0, "APP手机端做虚假签收"),
    CZSWYXSX(1, "操作失误影响包裹时效"),
    LRXXYSJBF(2, "信息报备不符"),
    APPCZBGF(3, "未做或做错APP扫描"),
    CWCNHWZQYDKH(4, "错误承诺客户"),
    WLXKH(5, "未与客户取得联系私自放包裹"),
    WJSGJ(6, "未及时处理客户投诉及客户需求"),
    CZBFGZKJPS(7, "装卸/分拣操作不规范致快件破损"),
    DZJWBZPS(8, "到站件外包装破损未及时上报"),
    PJTZYS(9, "派件途中遗失"),
    ZDNYS(10, "站点内遗失"),
    WASPSZDCP(11, "客户多次催派未及时处理"),
    YWPJ(12, "延误派件"),
    BSHSM(13, "拒绝送货上门"),
    WZJL(14, "伪造记录"),
    YXJZBD(15, "言行举止不当"),
    WLYQKH(16, "无理要求客户"),
    YKHFSMC(17, "言行冲突"),
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
