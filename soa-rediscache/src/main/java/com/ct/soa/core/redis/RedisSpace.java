package com.ct.soa.core.redis;

public enum RedisSpace {
	
	OPERATE("xlhy.operate"),SMS("xlhy.sms"),SHIRO("xlhy.shiro"),QUEUE("xlhy.queue");
	
	private String value;
	RedisSpace(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
