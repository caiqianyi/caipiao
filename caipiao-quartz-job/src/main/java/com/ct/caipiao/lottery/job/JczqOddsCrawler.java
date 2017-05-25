package com.ct.caipiao.lottery.job;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.model.Page;

import com.ct.soa.quartz.core.AbstractQuartzJob;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

@Component
public class JczqOddsCrawler extends AbstractQuartzJob{

	Logger logger = LoggerFactory.getLogger(LotteryCrawler.class);
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	private String poolcode;
	
	
	public JczqOddsCrawler() {
		// TODO Auto-generated constructor stub
		
		/*设置是否断点爬取*/  
		//this.setResumable(false);
	}
	
	@Override
	public void visit(Page page) {
		logger.debug("=============>>jczq poolcode :{}",poolcode);
		String collectionName = "jczq_"+poolcode+"_odds";
		String body = new String(page.getContent());
		logger.debug("==========>>jczq odds data:{}",body);
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		Map<String, Object> retMap = gson.fromJson(body,new TypeToken<Map<String, Object>>() {}.getType());
		LinkedTreeMap<String,Object> datas = (LinkedTreeMap<String, Object>) retMap.get("data");
		
		Set<String> keys = datas.keySet();
		
		for(String key : keys){
			LinkedTreeMap<String,Object> item = (LinkedTreeMap<String, Object>) datas.get(key);
			String matchId = key.replace("_", "");
			logger.debug("=============>>jczq odds matchId :{}",matchId);
			boolean hasId = mongoTemplate.exists(new Query().addCriteria(Criteria.where("id").is(matchId)), collectionName);
			if(!hasId){
				logger.debug("=============>>jczq odds item :{}",gson.toJson(item));
				mongoTemplate.insert(item, collectionName);
			}
		}
	}
	
	@Override
	public boolean handle(JobExecutionContext arg0,Map<String, Object> dataJson) {
		if(dataJson != null && dataJson.get("poolcode") != null){
			this.poolcode = (String) dataJson.get("poolcode");
		}
		if(poolcode == null){
			poolcode = "hhad";
		}
		this.addSeed("http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&poolcode[]="+poolcode+"&_="+System.currentTimeMillis());
		
		this.addRegex("http://i.sporttery.cn/odds_calculator/.*");
		try {
			this.start(this.getDepth());
		} catch (Exception e) {
			logger.error("竞彩足球",e);
		}
		return false;
	}

	public String getPoolcode() {
		return poolcode;
	}

	public void setPoolcode(String poolcode) {
		this.poolcode = poolcode;
	}
}
