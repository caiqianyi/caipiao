package com.ct.caipiao.lottery.job;

import org.springframework.stereotype.Component;

@Component
public class SfcLotteryCrawler extends LotteryCrawler{

	@Override
	public String getLotteryCat() {
		return "zcsfc";
	}
	
	
}
