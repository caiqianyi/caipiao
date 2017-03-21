package com.ct.soa.core.exception;

import java.io.Serializable;

public class I18nErrorMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3814041435546894811L;

	private String errmsg;
	
	private Integer errcode;

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
}
