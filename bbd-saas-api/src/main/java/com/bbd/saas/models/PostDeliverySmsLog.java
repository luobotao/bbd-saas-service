package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;

/**
 * 运单分派是发送的短信日志
 * @description: Created by liyanlei on 2016/11/2 15:15.
 */
public class PostDeliverySmsLog implements Serializable {
    private Integer id;//
    private Date date_new;//时间
    private String phone;//电话
    private String mailnum;//运单号码
    private String base64num;//base64编码的运单号
    private String url;//
    private String shorturl;//

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate_new() {
        return date_new;
    }

    public void setDate_new(Date date_new) {
        this.date_new = date_new;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailnum() {
        return mailnum;
    }

    public void setMailnum(String mailnum) {
        this.mailnum = mailnum;
    }

    public String getBase64num() {
        return base64num;
    }

    public void setBase64num(String base64num) {
        this.base64num = base64num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }
}
