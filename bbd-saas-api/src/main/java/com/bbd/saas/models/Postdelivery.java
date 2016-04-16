package com.bbd.saas.models;

import java.io.Serializable;
import java.util.Date;

/**
 * 快递配送实体类
 * @author luobotao
 *
 */
public class Postdelivery implements Serializable {
	
	private static final long serialVersionUID = 3742686543177230584L;
	private Integer id;
	
	private Integer postman_id;//派送员唯一 ID
	private String company_code;//落地配企业代码
	private String out_trade_no;//接入方订单唯一 ID
	private String mail_num;
	private String staffid;//工号 落地配公司员工工号
	private String merchant_code;
	private String need_pay;
	private String sender_name;//寄件方联系人
	private String sender_phone;//寄件方固定电话
	private String sender_telphone;//寄件方手机
	private String sender_province;//寄件方所在省 直辖市直接传北京市，上海市等
	private String sender_city;//寄件方所在市  直辖市直接传北京市，上海市等
	private String sender_district;//寄件方所在区
	private String sender_address;//寄件方地址
	private String sender_company_name;//寄件方公司名称
	private String sender_region_code;//寄件方所在区域码
	private String receiver_name;//收件方联系人
	private String receiver_phone;//收件方固定电话 固定电话和手机二选一
	private String receiver_telphone;//收件方手机 固定电话和手机二选一
	private String receiver_province;//收件方所在省
	private String receiver_city;//收件方所在市
	private String receiver_district;//收件方所在区
	private String receiver_address;//收件方地址
	private String receiver_company_name;//收件方公司名称
	private String receiver_region_code;//收件方所在区域码
	private Integer goods_fee;//收款金额 单位为分 退货、换货时，金额为负值，无付款、已付款时，金额为 0，其他情况，金额为正值。
	private Integer goods_number;//邮寄物总数量  单位为个
	private String remark;
	private String pay_status;//1已支付
	private String pay_mode;//（0：现金，1：刷卡，2：微信，3：支付宝，4：其它）
	private String resultmsg;
	private String typ;//订单类型 1-签单返回 2-退货 3-换货 4-普通件 5-加急件
	private String flg;//是否展示在今天未完成
	private String sta;//（[0:全部，服务器查询逻辑],1：未完成【需要签收】，2：未完成【需要支付签收】，3：已签收，4：已支付签收，5：已拒绝【未退单到站点】，6：已滞留，7，已拒绝【已退单到站点】
	private Date dateNew;
	private Date dateUpd;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPostman_id() {
		return postman_id;
	}

	public void setPostman_id(Integer postman_id) {
		this.postman_id = postman_id;
	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getMail_num() {
		return mail_num;
	}

	public void setMail_num(String mail_num) {
		this.mail_num = mail_num;
	}

	public String getStaffid() {
		return staffid;
	}

	public void setStaffid(String staffid) {
		this.staffid = staffid;
	}

	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	public String getNeed_pay() {
		return need_pay;
	}

	public void setNeed_pay(String need_pay) {
		this.need_pay = need_pay;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSender_phone() {
		return sender_phone;
	}

	public void setSender_phone(String sender_phone) {
		this.sender_phone = sender_phone;
	}

	public String getSender_telphone() {
		return sender_telphone;
	}

	public void setSender_telphone(String sender_telphone) {
		this.sender_telphone = sender_telphone;
	}

	public String getSender_province() {
		return sender_province;
	}

	public void setSender_province(String sender_province) {
		this.sender_province = sender_province;
	}

	public String getSender_city() {
		return sender_city;
	}

	public void setSender_city(String sender_city) {
		this.sender_city = sender_city;
	}

	public String getSender_district() {
		return sender_district;
	}

	public void setSender_district(String sender_district) {
		this.sender_district = sender_district;
	}

	public String getSender_address() {
		return sender_address;
	}

	public void setSender_address(String sender_address) {
		this.sender_address = sender_address;
	}

	public String getSender_company_name() {
		return sender_company_name;
	}

	public void setSender_company_name(String sender_company_name) {
		this.sender_company_name = sender_company_name;
	}

	public String getSender_region_code() {
		return sender_region_code;
	}

	public void setSender_region_code(String sender_region_code) {
		this.sender_region_code = sender_region_code;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_phone() {
		return receiver_phone;
	}

	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	public String getReceiver_telphone() {
		return receiver_telphone;
	}

	public void setReceiver_telphone(String receiver_telphone) {
		this.receiver_telphone = receiver_telphone;
	}

	public String getReceiver_province() {
		return receiver_province;
	}

	public void setReceiver_province(String receiver_province) {
		this.receiver_province = receiver_province;
	}

	public String getReceiver_city() {
		return receiver_city;
	}

	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}

	public String getReceiver_district() {
		return receiver_district;
	}

	public void setReceiver_district(String receiver_district) {
		this.receiver_district = receiver_district;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getReceiver_company_name() {
		return receiver_company_name;
	}

	public void setReceiver_company_name(String receiver_company_name) {
		this.receiver_company_name = receiver_company_name;
	}

	public String getReceiver_region_code() {
		return receiver_region_code;
	}

	public void setReceiver_region_code(String receiver_region_code) {
		this.receiver_region_code = receiver_region_code;
	}

	public Integer getGoods_fee() {
		return goods_fee;
	}

	public void setGoods_fee(Integer goods_fee) {
		this.goods_fee = goods_fee;
	}

	public Integer getGoods_number() {
		return goods_number;
	}

	public void setGoods_number(Integer goods_number) {
		this.goods_number = goods_number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getPay_mode() {
		return pay_mode;
	}

	public void setPay_mode(String pay_mode) {
		this.pay_mode = pay_mode;
	}

	public String getResultmsg() {
		return resultmsg;
	}

	public void setResultmsg(String resultmsg) {
		this.resultmsg = resultmsg;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	public String getSta() {
		return sta;
	}

	public void setSta(String sta) {
		this.sta = sta;
	}

	public Date getDateNew() {
		return dateNew;
	}

	public void setDateNew(Date dateNew) {
		this.dateNew = dateNew;
	}

	public Date getDateUpd() {
		return dateUpd;
	}

	public void setDateUpd(Date dateUpd) {
		this.dateUpd = dateUpd;
	}
}
