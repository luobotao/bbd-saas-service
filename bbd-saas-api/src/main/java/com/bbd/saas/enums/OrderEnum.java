package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * Created by luobotao on 2016/4/7.
 */
public class OrderEnum {


    /**
     * 包裹状态
     */
    public static enum Srcs {
        NOTARR(0,"未到站"),
        ARRIVED(1,"已扫描到站");
        private int status;
        private String message;
        private Srcs(int status, String message) {
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
            Srcs[] srcs = Srcs.values();
            sb.append(Htmls.generateOption(-1, "默认全部"));
            for (Srcs s : srcs) {
                if (id==s.status) {
                    sb.append(Htmls.generateSelectedOption(s.status,s.message));
                } else {
                    sb.append(Htmls.generateOption(s.status, s.message));
                }
            }
            return sb.toString();
        }
        public static Srcs status2Obj(int value) {
            Srcs[] stas = Srcs.values();
            for (Srcs s : stas) {
                if (value==s.status) {
                    return s;
                }
            }
            return null;
        }
    }
}
