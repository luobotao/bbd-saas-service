package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * 收件人信息
 * Created by luobotao on 2016/4/8.
 */
public class OtherInfo implements Serializable {
    private String key;
    private String value;

    public OtherInfo() {
    }

    public OtherInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
