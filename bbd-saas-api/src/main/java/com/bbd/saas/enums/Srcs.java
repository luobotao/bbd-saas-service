package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;
import com.bbd.saas.utils.StringUtil;

/**
 * 订单来源
 * Created by luobotao on 2016/4/8.
 */
public enum Srcs {
    BBT(0,"棒棒达"),
    JD(1,"京东"),
    TAOBAO(2,"淘宝"),
    TIANMAO(3,"天猫"),
    YIHAODIAN(4,"1号店"),
    BAIDUWAIMAI(5,"百度外卖"),
    PINHAOHUO(6,"拼好货"),
    HANWEI(7,"汉维"),
    DDKY(8,"叮当快药"),
    WEIXINXIAODIAN(9,"微信小店"),
    ZHE800(10,"折800"),
    DANGDANG(11,"当当网"),
    OTHERS(99,"其他");

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
    public static Srcs getEnumFromString(String string){
        if(StringUtil.isNotEmpty(string)){
            try{
                return Enum.valueOf(Srcs.class, string.trim());
            }catch(IllegalArgumentException ex){
                System.out.println("订单来源：字符串转为枚举失败");
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Srcs srcs=getEnumFromString("HANWEI");
        if(srcs != null){
            System.out.println(srcs.getMessage());
        }
    }
}
