package com.ct.soa.core.redis;

public enum RedisSpace {
	
	SMS("sys.sms"),GUESS("sys.guess");
	
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
