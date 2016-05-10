package com.bbd.saas.models;

import java.io.Serializable;

/**
 * bbt地址类
 * @author luobotao
 *
 */
public class BbtAddress implements Serializable {

	private static final long serialVersionUID = -6881239945444620545L;

	private Integer id;

	private Integer parentid;

	private String code;

	private String name;

	private Integer tier;

	private String maxstationcode;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public String getMaxstationcode() {
		return maxstationcode;
	}

	public void setMaxstationcode(String maxstationcode) {
		this.maxstationcode = maxstationcode;
	}
}
