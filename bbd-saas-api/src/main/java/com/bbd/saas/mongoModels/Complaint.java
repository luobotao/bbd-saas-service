package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.AppealStatus;
import com.bbd.saas.enums.ComplaintStatus;
import com.bbd.saas.enums.ComplaintType;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Complaint
 * 记录订单投诉信息
 * @author ctt
 * @date 16-09-26
 */
@Entity("complaint")
@Indexes(
        @Index(value = "mailNum", fields = @Field("mailNum"))
)
public class Complaint {
    @Id
    public ObjectId id;
    public String mailNum;  //运单号
    @Transient
    public Order order;     //订单快照
    public ComplaintStatus complaintStatus;
    public Date dateAdd;    //投诉时间
    public Date dateUpd;    //更新时间
    public ComplaintType typ;      //投诉类型
    public String reason;   //投诉原因
    public String memo;     //投诉备注
    public String name;     //投诉人姓名
    public String phone;    //投诉人手机号
    public AppealStatus appealStatus;   //申诉状态
    public String appealMemo;     //申诉说明
    public List<String> appealImg;      //申诉图片凭证
    public String appealDate;           //申诉时间
    public String closeReason;  //关闭理由
    public String punishReason; //处罚理由
    public List<ComplaintDealLog> complaintDealLogList;    //处理记录
    public String postmanPhone;    //快递员手机号
    public String areaCode;         //站点编码
    public String areaName;         //站点名称
    public String complaintHandler; //投诉处理人
    public String complaintHandlerId; //投诉处理人id
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMailNum() {
        return mailNum;
    }

    public void setMailNum(String mailNum) {
        this.mailNum = mailNum;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ComplaintStatus getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(ComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
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

    public ComplaintType getTyp() {
        return typ;
    }

    public void setTyp(ComplaintType typ) {
        this.typ = typ;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AppealStatus getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(AppealStatus appealStatus) {
        this.appealStatus = appealStatus;
    }

    public String getAppealMemo() {
        return appealMemo;
    }

    public void setAppealMemo(String appealMemo) {
        this.appealMemo = appealMemo;
    }

    public List<String> getAppealImg() {
        return appealImg;
    }

    public void setAppealImg(List<String> appealImg) {
        this.appealImg = appealImg;
    }

    public String getAppealDate() {
        return appealDate;
    }

    public void setAppealDate(String appealDate) {
        this.appealDate = appealDate;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public String getPunishReason() {
        return punishReason;
    }

    public void setPunishReason(String punishReason) {
        this.punishReason = punishReason;
    }

    public List<ComplaintDealLog> getComplaintDealLogList() {
        return complaintDealLogList;
    }

    public void setComplaintDealLogList(List<ComplaintDealLog> complaintDealLogList) {
        this.complaintDealLogList = complaintDealLogList;
    }

    public String getPostmanPhone() {
        return postmanPhone;
    }

    public void setPostmanPhone(String postmanPhone) {
        this.postmanPhone = postmanPhone;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getComplaintHandler() {
        return complaintHandler;
    }

    public void setComplaintHandler(String complaintHandler) {
        this.complaintHandler = complaintHandler;
    }

    public String getComplaintHandlerId() {
        return complaintHandlerId;
    }

    public void setComplaintHandlerId(String complaintHandlerId) {
        this.complaintHandlerId = complaintHandlerId;
    }
}
