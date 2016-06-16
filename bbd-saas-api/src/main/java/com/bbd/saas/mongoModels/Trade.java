package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.TradeStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * returnReason
 * 退货原因（类型）表
 * Created by luobotao on 2016/6/13.
 */
@Entity("trade")
@Indexes(
        @Index(value = "tradeNo", fields = @Field("tradeNo"))
)
public class Trade implements Serializable {

    private static final long serialVersionUID = 2828324763635826609L;
    @Id
    private ObjectId id;
    private ObjectId uId;//用户ID,网站端进行改版加入账号体系,数据将从User里获取
    private ObjectId embraceId;//揽件员Id
    private String tradeNo;//商户订单号
    private Integer amountMay;//订单金额(估计) 单位分
    private Integer amountReal;//订单金额(实际) 单位分
    private Integer amountReturn;//实际退款金额 单位分(应该退的金额可以计算出来,此为实际退款金额)
    private TradeStatus tradeStatus;//商户订单状态
    private Date dateAdd;      //下单时间
    private Date dateUpd;      //更新时间
    private Date datePay;      //支付时间
    private Date dateCatched;      //接单时间
    private Date dateGeted;      //取件时间

    private List<OrderSnap> orderSnaps; //订单快照，此交易单下的订单快照

    @Transient
    private User embrace;//揽件员
    @Transient
    private long totalMail;//快件数量
    @Transient
    private String statusMsg;//商户订单状态 -- tradeStatus.message



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

    public ObjectId getEmbraceId() {
        return embraceId;
    }

    public void setEmbraceId(ObjectId embraceId) {
        this.embraceId = embraceId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getAmountMay() {
        return amountMay;
    }

    public void setAmountMay(Integer amountMay) {
        this.amountMay = amountMay;
    }

    public Integer getAmountReal() {
        return amountReal;
    }

    public void setAmountReal(Integer amountReal) {
        this.amountReal = amountReal;
    }

    public Integer getAmountReturn() {
        return amountReturn;
    }

    public void setAmountReturn(Integer amountReturn) {
        this.amountReturn = amountReturn;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
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

    public Date getDatePay() {
        return datePay;
    }

    public void setDatePay(Date datePay) {
        this.datePay = datePay;
    }

    public Date getDateCatched() {
        return dateCatched;
    }

    public void setDateCatched(Date dateCatched) {
        this.dateCatched = dateCatched;
    }

    public Date getDateGeted() {
        return dateGeted;
    }

    public void setDateGeted(Date dateGeted) {
        this.dateGeted = dateGeted;
    }

    public User getEmbrace() {
        return embrace;
    }

    public void setEmbrace(User embrace) {
        this.embrace = embrace;
    }

    public long getTotalMail() {
        return totalMail;
    }

    public void setTotalMail(long totalMail) {
        this.totalMail = totalMail;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public List<OrderSnap> getOrderSnaps() {
        return orderSnaps;
    }

    public void setOrderSnaps(List<OrderSnap> orderSnaps) {
        this.orderSnaps = orderSnaps;
    }
}
