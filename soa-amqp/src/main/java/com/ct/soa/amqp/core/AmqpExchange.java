package com.ct.soa.amqp.core;

public enum AmqpExchange {
	
	FANOUT("xlhy.fanout"),TOPIC("xlhy.topic"),HEADERS("xlhy.headers"),DIRECT("xlhy.direct");
	
	private String value;
	
	private AmqpExchange(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}