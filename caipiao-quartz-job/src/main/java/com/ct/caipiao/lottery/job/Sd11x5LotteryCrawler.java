package com.ct.caipiao.lottery.job;

import org.springframework.stereotype.Component;

@Component
public class Sd11x5LotteryCrawler extends LotteryCrawler{

	@Override
	public String getLotteryCat() {
		return "sd11x5";
	}
	
	
}
