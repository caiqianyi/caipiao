package com.ct.caipiao.lottery.job;

import org.springframework.stereotype.Component;

@Component
public class Pl3LotteryCrawler extends LotteryCrawler{

	@Override
	public String getLotteryCat() {
		return "pl3";
	}
	
	
}
