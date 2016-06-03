package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;

/**
 * dubbo端口管理表
 * 根据IP获取端口
 * @author luobotao
 */
public class DubboPort implements Serializable {


    private Integer id;
    private String ip;
    private Integer port;
    public Date dateNew;
    public Date dateUpd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Date getDateNew() {
        return dateNew;
    }

    public void setDateNew(Date dateNew) {
        this.dateNew = dateNew;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }
}
