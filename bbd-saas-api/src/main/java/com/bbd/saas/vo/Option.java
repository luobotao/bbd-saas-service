package com.bbd.saas.vo;

import java.io.Serializable;

/**
 * Description: select下拉框对象
 * @author: liyanlei
 * 2016年7月26日上午11:21:34
 */
public class Option implements Serializable{
	private static final long serialVersionUID = -1123072469849637576L;
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
