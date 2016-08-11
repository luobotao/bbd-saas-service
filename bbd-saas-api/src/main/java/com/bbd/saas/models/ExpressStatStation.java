package com.bbd.saas.models;

import java.io.Serializable;

/**
 * 订单统计实体类
 * @author liyanlei
 *
 */
public class ExpressStatStation implements Serializable {
	private static final long serialVersionUID = -4465799217103434574L;
	private Integer id;
	private String tim;//日期-时间
	private String areacode;//站点区域码
	private String sitename;//站点名称
	private Integer nostationcnt;//未到站数量
	private Integer stationcnt;//到站数量
	private Integer successcnt;//签收数量
	private Integer dailycnt;//滞留数量
	private Integer refusecnt;//拒收数量
	private Integer deliverycnt;//派件数量
	private Integer changestationcnt;//转其他站点数量
	private Integer changeexpresscnt;//转其他快递数量
	private Integer companyId;//公司Id

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTim() {
		return tim;
	}

	public void setTim(String tim) {
		this.tim = tim;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public Integer getNostationcnt() {
		return nostationcnt;
	}

	public void setNostationcnt(Integer nostationcnt) {
		this.nostationcnt = nostationcnt;
	}

	public Integer getSuccesscnt() {
		return successcnt;
	}

	public void setSuccesscnt(Integer successcnt) {
		this.successcnt = successcnt;
	}

	public Integer getDailycnt() {
		return dailycnt;
	}

	public void setDailycnt(Integer dailycnt) {
		this.dailycnt = dailycnt;
	}

	public Integer getRefusecnt() {
		return refusecnt;
	}

	public void setRefusecnt(Integer refusecnt) {
		this.refusecnt = refusecnt;
	}

	public Integer getStationcnt() {
		return stationcnt;
	}

	public void setStationcnt(Integer stationcnt) {
		this.stationcnt = stationcnt;
	}

	public Integer getDeliverycnt() {
		return deliverycnt;
	}

	public void setDeliverycnt(Integer deliverycnt) {
		this.deliverycnt = deliverycnt;
	}

	public Integer getChangestationcnt() {
		return changestationcnt;
	}

	public void setChangestationcnt(Integer changestationcnt) {
		this.changestationcnt = changestationcnt;
	}

	public Integer getChangeexpresscnt() {
		return changeexpresscnt;
	}

	public void setChangeexpresscnt(Integer changeexpresscnt) {
		this.changeexpresscnt = changeexpresscnt;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public ExpressStatStation() {
	}

	public ExpressStatStation(String companyId, String  areacode, String sitename, String time) {
		this.areacode = areacode;
		this.sitename = sitename;
		this.tim = time;
		this.nostationcnt = 0;//未到站数量
		this.successcnt = 0;//签收数量
		this.dailycnt = 0;//滞留数量
		this.refusecnt = 0;//拒收数量
		this.stationcnt = 0;//到站数量
		this.deliverycnt = 0;//派件数量
		this.changestationcnt = 0;//转其他站点数量
		this.changeexpresscnt = 0;//转其他快递数量
		this.companyId = Integer.parseInt(companyId);//公司Id

	}
}
