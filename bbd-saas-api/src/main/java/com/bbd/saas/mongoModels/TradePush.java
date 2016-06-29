package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.Srcs;
import com.bbd.saas.vo.Goods;
import com.bbd.saas.vo.Reciever;
import com.bbd.saas.vo.Sender;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Entity("tradePush")
@Indexes(
        @Index(value = "tradeNo", fields = @Field("tradeNo"))
)
public class TradePush implements Serializable {
    @Id
    private ObjectId id;
    private String tradeNo;             //商户订单号
    private Integer time;               //推送次数   默认0
    private Integer flag;               //是否已推送 0 否 1 是
    private Integer postmanId;          //揽件员Id
    private Date dateAdd;               //添加时间
    private Date dateUpd;               //更新时间

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(Integer postmanId) {
        this.postmanId = postmanId;
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
}
