package com.bbd.saas.vo;

import com.bbd.saas.enums.OrderSetStatus;
import org.mongodb.morphia.annotations.Embedded;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点处理时的时间vo
 * Created by luobotao on 2016/7/11.
 */
@Embedded
public class SiteTime implements Serializable {
    private String siteId;
    private OrderSetStatus orderSetStatus;
    private Date dateAdd;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public OrderSetStatus getOrderSetStatus() {
        return orderSetStatus;
    }

    public void setOrderSetStatus(OrderSetStatus orderSetStatus) {
        this.orderSetStatus = orderSetStatus;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }
}
