package com.ct.caipiao.lottery.job;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
public class JczqMatchCrawler extends AbstractQuartzJob{

	Logger logger = LoggerFactory.getLogger(LotteryCrawler.class);
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	public JczqMatchCrawler() {
		// TODO Auto-generated constructor stub
		
		
		/*设置是否断点爬取*/  
		//this.setResumable(false);
	}
	
	@Override
	public void visit(Page page) {
		String collectionName = "jczq_matchs";
		String body = new String(page.getContent());
		logger.debug("==========>>jczq data:{}",body);
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		Map<String, Object> retMap = gson.fromJson(body,new TypeToken<Map<String, Object>>() {}.getType());
		LinkedTreeMap<String,Object> datas = (LinkedTreeMap<String, Object>) retMap.get("data");
		
		Set<String> keys = datas.keySet();
		
		for(String key : keys){
			LinkedTreeMap<String,Object> item = (LinkedTreeMap<String, Object>) datas.get(key);
			String matchId = key.replace("_", "");
			logger.debug("=============>>jclq matchId :{}",matchId);
			Query query = new Query().addCriteria(Criteria.where("id").is(matchId));
			boolean hasId = mongoTemplate.exists(query, collectionName);
			if(hasId){
				logger.debug("=============>>jczq remove matchId:{}",matchId);
				mongoTemplate.remove(query, collectionName);
			}
			
			if(!hasId){
				item.put("id", matchId);
				logger.debug("=============>>jczq item :{}",gson.toJson(item));
				mongoTemplate.insert(item, collectionName);
			}
		}
	}
	
	@Override
	public boolean handle(JobExecutionContext arg0,Map<String, Object> dataJson) {
		this.addSeed("http://i.sporttery.cn/odds_calculator/get_proportion?i_format=json&pool[]=had&pool[]=hhad&_="+System.currentTimeMillis());
		this.addRegex("http://i.sporttery.cn/odds_calculator/.*");
		return false;
	}
}
