package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * 商品信息
 * Created by luobotao on 2016/4/8.
 */
public class Goods implements Serializable {
    private String code;
    private String title;
    private int num;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
