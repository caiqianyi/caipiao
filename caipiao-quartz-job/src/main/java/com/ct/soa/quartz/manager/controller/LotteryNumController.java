package com.ct.soa.quartz.manager.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/lotterynum")
public class LotteryNumController {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@ResponseBody
	@RequestMapping(value="/{cat}/{expect}", method=RequestMethod.GET)
	public List<?> query(@PathVariable String cat,@PathVariable String expect){
		Query query = new Query().addCriteria(Criteria.where("expect").is(expect).and("cat").is(cat));
		return mongoTemplate.find(query,Map.class,"lottery_nums");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/{cat}/", method=RequestMethod.GET)
	public List<?> query(@PathVariable String cat){
		Query query = new Query().addCriteria(Criteria.where("cat").is(cat));
		return mongoTemplate.find(query,Map.class,"lottery_nums");
	}
}
