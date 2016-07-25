package com.bbd.saas.enums;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiwei on 16/7/4.
 */
public enum LocSource {
    AMAP("高德地图"),
    BAIDU("百度地图"),
    TENCENT("腾讯地图"),
    BBD("棒棒达地图");

    private String msg;
    private LocSource(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    // Implementing a fromString method on an enum type
    private static final Map<String, LocSource> stringToEnum = new HashMap<String, LocSource>();
    static {
        // Initialize map from constant name to enum constant
        for(LocSource locSource : values()) {
            stringToEnum.put(locSource.toString(), locSource);
        }
    }

    // Returns Blah for string, or null if string is invalid
    public static LocSource fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    public static void main(String[] args) {
        System.out.println(fromString("AMAP").getMsg());
        System.out.println(LocSource.fromString("BAIDU").getMsg());
    }
}
