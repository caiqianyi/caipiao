package com.ct.soa.quartz.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;

public abstract class AbstractQuartzJob extends BreadthCrawler implements Job{
	
	public static Set<String> JOB_CLASS_NAMES= new HashSet<String>();
	
	private Integer depth = 1;
	
	public AbstractQuartzJob() {
		JOB_CLASS_NAMES.add(this.getClass().getName());
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Map<String, Object> retMap = null;
		if(arg0 != null){
			String json  = (String) arg0.getJobDetail().getJobDataMap().get("dataJson");
			if(StringUtils.isNoneBlank(json)){
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
						.create(); 
				retMap = gson.fromJson(json,new TypeToken<Map<String, Object>>() {}.getType());
				if(retMap.get("depth") != null && StringUtils.isNumeric((String)retMap.get("depth"))){
					depth = Integer.parseInt((String)retMap.get("depth"));
				}
			}
		}
		this.handle(arg0,retMap);
	}
	
	public Integer getDepth() {
		return depth;
	}

	public abstract boolean handle(JobExecutionContext arg0,Map<String, Object> retMap);
}
