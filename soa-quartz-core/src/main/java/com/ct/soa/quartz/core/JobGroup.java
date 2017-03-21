package com.ct.soa.quartz.core;

public enum JobGroup {
	
	SYSTEM("xlhy.system"),SMS("xlhy.sms"),PAY("xlhy.pay"),SHOWDATA("xlhy.showdata"),
	MARKET("xlhy.market"),SECURITY("xlhy.security"),OPERATE("xlhy.operate");
	
	private String name;
	
	JobGroup(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public static JobGroup valueByName(String name){
		JobGroup jgs[] = JobGroup.values();
		for(JobGroup jg : jgs){
			if(jg.getName().equals(name)){
				return jg;
			}
		}
		return null;
	}
}
