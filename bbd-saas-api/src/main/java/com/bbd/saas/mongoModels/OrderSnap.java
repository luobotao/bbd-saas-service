package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.Srcs;
import com.bbd.saas.enums.SynsFlag;
import com.bbd.saas.vo.Goods;
import com.bbd.saas.vo.Reciever;
import com.bbd.saas.vo.Sender;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Order
 * 订单表
 * Created by luobotao on 2016/4/10.
 */
@Entity("orderSnap")
@Indexes(
        @Index(value = "orderNo", fields = @Field("orderNo"))
)
public class OrderSnap implements Serializable {
    private static final long serialVersionUID = -6802839001275501390L;
    @Id
    private ObjectId id;
    private ObjectId uId;//用户ID,网站端进行改版加入账号体系,数据将从User里获取(adminUserId将不再使用)
    private String orderNo;

    @Embedded
    private Sender sender;
    @Embedded
    private Reciever reciever;
    private Srcs src;
    private List<Goods> goods;
    private Date orderCreate;//订单创建时间
    private Date orderPay;     //订单支付时间

    private Date dateMayArrive;//预计到站时间
    private SynsFlag synsFlag;//与Order同步状态0未同步 1已同步 2同步失败

    private Date dateAdd;
    private Date dateUpd;//

    private String errorRemark;//
    private String mailNum;//运单号

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getuId() {
        return uId;
    }

    public void setuId(ObjectId uId) {
        this.uId = uId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMailNum() {
        return mailNum;
    }

    public void setMailNum(String mailNum) {
        this.mailNum = mailNum;
    }


    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Reciever getReciever() {
        return reciever;
    }

    public void setReciever(Reciever reciever) {
        this.reciever = reciever;
    }

    public Srcs getSrc() {
        return src;
    }

    public void setSrc(Srcs src) {
        this.src = src;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public Date getOrderCreate() {
        return orderCreate;
    }

    public void setOrderCreate(Date orderCreate) {
        this.orderCreate = orderCreate;
    }

    public Date getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(Date orderPay) {
        this.orderPay = orderPay;
    }

    public Date getDateMayArrive() {
        return dateMayArrive;
    }

    public void setDateMayArrive(Date dateMayArrive) {
        this.dateMayArrive = dateMayArrive;
    }

    public SynsFlag getSynsFlag() {
        return synsFlag;
    }

    public void setSynsFlag(SynsFlag synsFlag) {
        this.synsFlag = synsFlag;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }

    public String getErrorRemark() {
        return errorRemark;
    }

    public void setErrorRemark(String errorRemark) {
        this.errorRemark = errorRemark;
    }
}
