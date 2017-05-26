package com.ct.caipiao.lottery.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.google.gson.reflect.TypeToken;

@Component
@Scope("prototype")
public class LotteryCrawler extends AbstractQuartzJob{
	
	Logger logger = LoggerFactory.getLogger(LotteryCrawler.class);
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	private String cat;
	
	@Override
	public void visit(Page page) {
		String cat = this.getCat();
		String collectionName = "lottery_nums";
		String body = new String(page.getContent());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		Map<String, Object> retMap = gson.fromJson(body,new TypeToken<Map<String, Object>>() {}.getType());
		List<Map<String,Object>> vls = (List<Map<String,Object>>) retMap.get("data");
		for(Map<String,Object> vl : vls){
			Query query = new Query().addCriteria(Criteria.where("expect").is(vl.get("expect")).and("cat").is(cat));
			boolean has = mongoTemplate.exists(query,collectionName);
			if(!has){
				vl.put("cat", cat);
				vl.put("time", new Date());
				logger.debug("=============>>add lotterynum vl :{}",gson.toJson(vl));
				mongoTemplate.insert(vl, collectionName);
			}
		}
	}
	
	@Override
	public boolean handle(JobExecutionContext arg0,Map<String, Object> dataJson) {
		if(dataJson != null){
			this.setCat((String) dataJson.get("cat"));
		}
		String cat = this.getCat();
		if(cat == null){
			cat = "jx11x5";
		}
		logger.debug("2=============>>cat :{}",cat);
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(new String[]{"http://f.apiplus.cn/"+cat+".json"}));
		this.setSeeds(list);
		this.addRegex("http://f.apiplus.cn/.*");
		return false;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}
	
}
