package com.ct.soa.quartz.manager.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/matchs")
public class MatchController {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@ResponseBody
	@RequestMapping(value="/jczq/{poolcode}", method=RequestMethod.GET)
	public List<?> jczq(@PathVariable String poolcode){
		String collectionName = "jczq_"+poolcode+"_odds";
		return mongoTemplate.findAll(Map.class,collectionName);
	}
	
	@ResponseBody
	@RequestMapping(value="/jclq/{poolcode}", method=RequestMethod.GET)
	public List<?> jclq(@PathVariable String poolcode){
		String collectionName = "jclq_"+poolcode+"_matchs";
		return mongoTemplate.findAll(Map.class,collectionName);
	}
	
	@ResponseBody
	@RequestMapping(value="/{collectionName}", method=RequestMethod.GET)
	public List<?> collectionName(@PathVariable String collectionName){
		return mongoTemplate.findAll(Map.class,collectionName);
	}
	
}
