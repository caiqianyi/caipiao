package com.ct.caipiao.lottery.job;

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
public class LotteryCrawler extends AbstractQuartzJob{
	
	Logger logger = LoggerFactory.getLogger(LotteryCrawler.class);
	
	@Resource
    private LotteryNumsDao lotteryNumsDao;
	
	private String cat;
	
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
			ln.setCat(cat);
			List<LotteryNums> num = lotteryNumsDao.find(new Query(Criteria.where("qihao").is(ln.getQihao()).and("cat").is(cat)));
			logger.debug("num:"+num);
			if(num == null || num.isEmpty()){
				logger.debug("ln:"+gson.toJson(ln));
				lotteryNumsDao.save(ln);
			}
		}
	}
	
	@Override
	public boolean handle(JobExecutionContext arg0) {
		this.cat = (String) arg0.getJobDetail().getJobDataMap().get("dataJson");
		this.addSeed("http://f.apiplus.cn/"+cat+".json");
		this.addRegex("http://f.apiplus.cn/.*");
		try {
			this.start(1);
		} catch (Exception e) {
			logger.error("",e);
		}
		return false;
	}
}
