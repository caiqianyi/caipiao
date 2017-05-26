package com.ct.soa.quartz.core;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.net.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public abstract class AbstractQuartzJob extends BreadthCrawler implements Job{
	
	Logger logger = LoggerFactory.getLogger(AbstractQuartzJob.class);
	
	public static Set<String> JOB_CLASS_NAMES= new HashSet<String>();
	
	private Integer depth = 1;
	
	private Map<String,String> header;
	
	public AbstractQuartzJob() {
		JOB_CLASS_NAMES.add(this.getClass().getName());
	}

	@Override
	public Request createRequest(String url) throws Exception {
		CrawlerRequest request = new CrawlerRequest();
        request.setURL(new URL(url));
        request.setHeader(header);
        return request;
	}	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Map<String, Object> retMap = null;
		if(arg0 != null){
			String json  = (String) arg0.getJobDetail().getJobDataMap().get("dataJson");
			if(StringUtils.isNotBlank(json)){
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
						.create(); 
				retMap = gson.fromJson(json,new TypeToken<Map<String, Object>>() {}.getType());
				if(retMap.get("depth") != null && StringUtils.isNumeric((String)retMap.get("depth"))){
					depth = Integer.parseInt((String)retMap.get("depth"));
				}
			}
		}
		this.handle(arg0,retMap);
		
		try {
			this.start(this.getDepth());
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	public Integer getDepth() {
		return depth;
	}
	
	public void setHeader(Map<String,String> header) {
		this.header = header;
	}

	public abstract boolean handle(JobExecutionContext arg0,Map<String, Object> retMap);

}
