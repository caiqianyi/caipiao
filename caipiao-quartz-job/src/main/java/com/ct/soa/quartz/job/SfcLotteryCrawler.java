package com.ct.soa.quartz.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.model.Page;

import com.ct.caipiao.core.dao.LotteryNumsDao;
import com.ct.caipiao.core.entity.LotteryNums;
import com.ct.soa.quartz.core.AbstractQuartzJob;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Component
public class SfcLotteryCrawler extends AbstractQuartzJob{
	
	Logger logger = LoggerFactory.getLogger(MinuteJob.class);
	
	@Resource
    private LotteryNumsDao lotteryNumsDao;

	public SfcLotteryCrawler() {
		super();
		this.addSeed("http://f.apiplus.cn/zcsfc.json");
		this.addRegex("http://f.apiplus.cn/.*");
	}
	
	@Override
	public void visit(Page page) {
		String body = new String(page.getContent());
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		Map<String, Object> retMap = gson.fromJson(body,new TypeToken<Map<String, Object>>() {}.getType());
		List<Map<String,Object>> vls = (List<Map<String,Object>>) retMap.get("data");
		for(Map<String,Object> vl : vls){
			LotteryNums ln = new LotteryNums();
			ln.setLottery(vl.get("opencode").toString());
			ln.setOpenTimeStamp(((Double) vl.get("opentimestamp")).longValue());
			ln.setQihao(vl.get("expect").toString());
			ln.setTime(new Date());
			ln.setCat("sfc");
			List<LotteryNums> num = lotteryNumsDao.find(new Query(Criteria.where("qihao").is(ln.getQihao()).and("cat").is("sfc")));
			logger.debug("num:"+num);
			if(num == null || num.isEmpty()){
				logger.debug("ln:"+gson.toJson(ln));
				lotteryNumsDao.save(ln);
			}
		}  
	}
	
	public static void main(String[] args) throws Exception {
		SfcLotteryCrawler crawler=new SfcLotteryCrawler();
		crawler.start(1);
		/*Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		String body = "{\"rows\":5,\"code\":\"zcsfc\",\"info\":\"免费接口随机延迟3-6分钟，实时接口请访问opencai.net或QQ:23081452(注明彩票或API)\",\"data\":[{\"expect\":\"2017039\",\"opencode\":\"3,3,3,0,1,0,3,3,1,3,3,3,1,3\",\"opentime\":\"2017-03-19 14:00:00\",\"opentimestamp\":1489903200}]}";
		Map<String, Object> retMap = gson.fromJson(body,new TypeToken<Map<String, Object>>() {}.getType());
		List<Object> json = (List<Object>) retMap.get("data");
		System.out.println(json);*/
	}

	@Override
	public boolean handle(JobExecutionContext arg0) {
		try {
			this.start(1);
		} catch (Exception e) {
			logger.error("",e);
		}
		return false;
	}
}
