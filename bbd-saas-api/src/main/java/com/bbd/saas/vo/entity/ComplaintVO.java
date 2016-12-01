package com.bbd.saas.vo.entity;

import com.bbd.saas.enums.AppealStatus;
import com.bbd.saas.enums.ComplaintStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Complaint
 * 记录订单投诉信息
 * @author liyanlei
 * @date 16-09-27
 */
public class ComplaintVO implements Serializable {
    private String id;
    private String mailNum;  //运单号
    private String reason;   //发起投诉理由
    private String punishReason;   //投诉成立理由
    private Date dateAdd;    //投诉时间
    private String respondent ;    //被投诉人
    private AppealStatus appealStatus;   //申诉状态
    private ComplaintStatus complaintStatus; //投诉状态
    private String dealResult; //处罚结果
    private String appealStatusMsg; //申诉状态Msg
    private String complaintStatusMsg; //投诉状态Msg

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMailNum() {
        return mailNum;
    }

    public void setMailNum(String mailNum) {
        this.mailNum = mailNum;
    }

    public String getReason() {
        return reason == null ? "" : reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getRespondent() {
        return respondent == null ? "" : respondent;
    }

    public void setRespondent(String respondent) {
        this.respondent = respondent;
    }

    public AppealStatus getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(AppealStatus appealStatus) {
        this.appealStatus = appealStatus;
    }

    public ComplaintStatus getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(ComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getDealResult() {
        return dealResult == null ? "" : dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getAppealStatusMsg() {
        return appealStatusMsg;
    }

    public void setAppealStatusMsg(String appealStatusMsg) {
        this.appealStatusMsg = appealStatusMsg;
    }

    public String getComplaintStatusMsg() {
        return complaintStatusMsg;
    }

    public void setComplaintStatusMsg(String complaintStatusMsg) {
        this.complaintStatusMsg = complaintStatusMsg;
    }

    public String getPunishReason() {
        return punishReason;
    }

    public void setPunishReason(String punishReason) {
        this.punishReason = punishReason;
    }
}
