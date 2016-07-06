package com.bbd.saas.vo;

import com.bbd.saas.enums.OrderSetStatus;

import java.io.Serializable;

/**  揽件入库 封装一条揽件入库信息的Vo
 * Created by huozhijie on 2016/6/25.
 */
public class OrderHoldToStoreVo implements Serializable {
    private  String areaCode;//订单所属站点的区域码
    private  String userName;//揽件人名称
    private  String  phone;//揽件人电话
    private String mailNum;//运单号
    private  String RecieverName;//收件人
    private String  RecieverPhone;//收件人电话
    private String  RecieverAddress;//收件人地址
    private OrderSetStatus orderSetStatus;//状态

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailNum() {
        return mailNum;
    }

    public void setMailNum(String mailNum) {
        this.mailNum = mailNum;
    }

    public String getRecieverName() {
        return RecieverName;
    }

    public void setRecieverName(String recieverName) {
        RecieverName = recieverName;
    }

    public String getRecieverPhone() {
        return RecieverPhone;
    }

    public void setRecieverPhone(String recieverPhone) {
        RecieverPhone = recieverPhone;
    }

    public String getRecieverAddress() {
        return RecieverAddress;
    }

    public void setRecieverAddress(String recieverAddress) {
        RecieverAddress = recieverAddress;
    }

    public OrderSetStatus getOrderSetStatus() {
        return orderSetStatus;
    }

    public void setOrderSetStatus(OrderSetStatus orderSetStatus) {
        this.orderSetStatus = orderSetStatus;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
