package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.*;
import com.bbd.saas.vo.Express;
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
@Entity("order")
@Indexes(
        @Index(value = "orderNo", fields = @Field("orderNo"))
)
public class Order implements Serializable {
    private static final long serialVersionUID = -6802839001275501390L;
    @Id
    private ObjectId id;
    private ObjectId adminUserId;
    private String mailNum;
    private String orderNo;
    private String areaName;
    private String areaCode;//站点编码
    private String areaRemark;//站点地址
    @Embedded
    private Sender sender;
    @Embedded
    private Reciever reciever;
    @Embedded
    private User user;
    private Srcs src;
    private OrderStatus orderStatus;
    private ExpressStatus expressStatus;
    private PrintStatus printStatus;
    private int errorFlag;//异常面单？ 0否 1是
    private String errorRemark;//异常信息
    private List<Goods> goods;
    private List<Express> expresses;
    private Date orderCreate;//订单创建时间
    private Date orderPay;     //订单支付时间
    private Date dateAdd;
    private Date datePrint;//物流单打印时间
    private Date dateMayArrive;//预计到站时间
    private Date dateArrived;//到站时间
    private Date dateUpd;//
    private SynsFlag synsFlag;//与易普同步状态0未同步 1已同步 2同步失败
    @Transient
    private String parcelCode;
    @Transient
    private String srcMessage;//前台JSP页面中的JS无法根据枚举来获取message



    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(ObjectId adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getMailNum() {
        return mailNum;
    }

    public void setMailNum(String mailNum) {
        this.mailNum = mailNum;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaRemark() {
        return areaRemark;
    }

    public void setAreaRemark(String areaRemark) {
        this.areaRemark = areaRemark;
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
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Srcs getSrc() {
        return src;
    }

    public void setSrc(Srcs src) {
        this.src = src;
    }

    public ExpressStatus getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(ExpressStatus expressStatus) {
        this.expressStatus = expressStatus;
    }

    public PrintStatus getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(PrintStatus printStatus) {
        this.printStatus = printStatus;
    }

    public int getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(int errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getErrorRemark() {
        return errorRemark;
    }

    public void setErrorRemark(String errorRemark) {
        this.errorRemark = errorRemark;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public List<Express> getExpresses() {
        return expresses;
    }

    public void setExpresses(List<Express> expresses) {
        this.expresses = expresses;
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

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }

    public SynsFlag getSynsFlag() {
        return synsFlag;
    }

    public void setSynsFlag(SynsFlag synsFlag) {
        this.synsFlag = synsFlag;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getParcelCode() {
        return parcelCode;
    }

    public void setParcelCode(String parcelCode) {
        this.parcelCode = parcelCode;
    }

    public Date getDateMayArrive() {
        return dateMayArrive;
    }

    public void setDateMayArrive(Date dateMayArrive) {
        this.dateMayArrive = dateMayArrive;
    }

	public Date getDateArrived() {
		return dateArrived;
	}

	public void setDateArrived(Date dateArrived) {
		this.dateArrived = dateArrived;
	}

    public String getSrcMessage() {
        return src.getMessage();
    }
}
