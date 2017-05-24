package com.ct.caipiao.lottery.job;

import java.util.Map;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.webcollector.model.Page;

import com.ct.soa.quartz.core.AbstractQuartzJob;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JclqMatchCrawler extends AbstractQuartzJob{

	Logger logger = LoggerFactory.getLogger(LotteryCrawler.class);
	
	@Override
	public void visit(Page page) {
		logger.info("==========>>jclq data:{}");
		String body = new String(page.getContent());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		Map<String, Object> retMap = gson.fromJson(body,new TypeToken<Map<String, Object>>() {}.getType());
	}
	
	@Override
	public boolean handle(JobExecutionContext arg0) {
		this.addSeed("http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&poolcode=hdc&_="+System.currentTimeMillis());
		try {
			this.start(1);
		} catch (Exception e) {
			logger.error("",e);
		}
		return false;
	}
}
