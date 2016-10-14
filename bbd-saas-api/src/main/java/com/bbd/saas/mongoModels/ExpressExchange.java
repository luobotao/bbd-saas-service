package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.ExpressExchangeStatus;
import com.bbd.saas.vo.OrderVO;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 向外部系统推送物流信息
 */
@Entity("expressExchange")
public class ExpressExchange implements Serializable {

    @Id
    private ObjectId id;

    private String url;                                  //请求url
    private String operator;                             //操作人       （初始必填）
    private String phone;                                //手机号       （初始必填）
    private OrderVO order;                                 //订单         （初始必填）
    private String typ;                                  //操作类型
    private String pushInfo;                             //推送内容，json字符串
    private String requestStr;                           //请求串
    private List<String> responseStr;                    //返回结果串
    private ExpressExchangeStatus status;                //操作状态      （初始必填）
    private List<String> memo;                           //备注
    private int pushCount;                               //推送次数
    private int timeInterval;                            //间隔时间
    private Date dateAdd;                                //添加时间      （初始必填）
    private Date dateUpd;                                //更新时间

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public OrderVO getOrder() {
        return order;
    }

    public void setOrder(OrderVO order) {
        this.order = order;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getPushInfo() {
        return pushInfo;
    }

    public void setPushInfo(String pushInfo) {
        this.pushInfo = pushInfo;
    }

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }

    public List<String> getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(List<String> responseStr) {
        this.responseStr = responseStr;
    }

    public ExpressExchangeStatus getStatus() {
        return status;
    }

    public void setStatus(ExpressExchangeStatus status) {
        this.status = status;
    }

    public List<String> getMemo() {
        return memo;
    }

    public void setMemo(List<String> memo) {
        this.memo = memo;
    }

    public int getPushCount() {
        return pushCount;
    }

    public void setPushCount(int pushCount) {
        this.pushCount = pushCount;
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