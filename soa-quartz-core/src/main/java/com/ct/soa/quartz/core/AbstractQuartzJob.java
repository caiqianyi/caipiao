package com.ct.soa.quartz.core;

import java.util.HashSet;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;

public abstract class AbstractQuartzJob extends BreadthCrawler implements Job{
	
	public static Set<String> JOB_CLASS_NAMES= new HashSet<String>();
	
	public AbstractQuartzJob() {
		JOB_CLASS_NAMES.add(this.getClass().getName());
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		this.handle(arg0);
	}
	
	public abstract boolean handle(JobExecutionContext arg0);
}
