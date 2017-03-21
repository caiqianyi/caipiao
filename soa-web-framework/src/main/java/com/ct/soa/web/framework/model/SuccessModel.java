package com.ct.soa.web.framework.model;

import java.io.Serializable;

public class SuccessModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2973879981286662465L;

	private Integer errcode;
	private Object data;
	private String errmsg;
	
	public SuccessModel() {
		this.errcode = 0;
		this.errmsg = "success";
	}
	
	public SuccessModel(Object data) {
		this();
		this.data = data;
	}
	
	public Integer getErrcode() {
		return errcode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getErrmsg() {
		return errmsg;
	}
}
