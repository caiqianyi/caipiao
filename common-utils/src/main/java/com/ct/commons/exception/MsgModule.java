package com.ct.commons.exception;

public enum MsgModule {

	GLOBAL("sys.global"),SMS("sys.sms"),PAY("sys.pay");
	
	private String value;
	
	MsgModule(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
