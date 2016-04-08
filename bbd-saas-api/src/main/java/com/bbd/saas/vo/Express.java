package com.bbd.saas.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流信息
 * Created by luobotao on 2016/4/8.
 */
public class Express implements Serializable {
    private String remark;
    private Date dateAdd;
    private String lat;
    private String lon;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
