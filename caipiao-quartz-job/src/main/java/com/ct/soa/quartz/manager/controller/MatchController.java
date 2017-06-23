package com.ct.soa.quartz.manager.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
	
	
	@ResponseBody
	@RequestMapping(value="/lpl_guess_list/{feild}/{value}", method=RequestMethod.GET)
	public List<?> lpl_guess_list(@PathVariable String feild,@PathVariable String value){
		Query query = new Query().addCriteria(Criteria.where(feild).is(value)).with(new Sort(new Order(Direction.DESC,"startTime")));
		return mongoTemplate.find(query, Map.class, "lpl_guess_list");
	}
	
	@ResponseBody
	@RequestMapping(value="/lpl_guess_list/current", method=RequestMethod.GET)
	public List<?> lpl_guess_list_current(){
		Query query = new Query().addCriteria(Criteria.where("startTime").gt(System.currentTimeMillis()/1000)).with(new Sort(new Order(Direction.DESC,"startTime")));
		return mongoTemplate.find(query, Map.class, "lpl_guess_list");
	}
	
}
