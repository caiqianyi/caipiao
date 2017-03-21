package com.ct.soa.core.exception;

public enum MsgModule {

	GLOBAL("xlhy.global"),SMS("xlhy.sms"),PAY("xlhy.pay"),
	SHOWDATA("xlhy.showdata"),MARKET("xlhy.market"),SECURITY("xlhy.security"),OPERATE("xlhy.operate");
	
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
