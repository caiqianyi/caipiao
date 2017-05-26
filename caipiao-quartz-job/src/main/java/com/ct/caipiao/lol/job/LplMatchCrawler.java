package com.ct.caipiao.lol.job;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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
public class LplMatchCrawler extends AbstractQuartzJob{
	
	Logger logger = LoggerFactory.getLogger(LplMatchCrawler.class);
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void visit(Page page) {
		String lpl_guess_list_cn = "lpl_guess_list";
		String lpl_play_num_list_cn = "lpl_play_num_List";
		
		String body = null;
		try {
			body = new String(page.getContent(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("==========>>lpl match data:{}",body);
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()  
                .create(); 
		Map<String, Object> retMap = gson.fromJson(body,new TypeToken<Map<String, Object>>() {}.getType());
		LinkedTreeMap<String,Object> data = (LinkedTreeMap<String, Object>) retMap.get("data");
		LinkedTreeMap<String,Object> playNumList = (LinkedTreeMap<String, Object>) data.get("playNumList");
		for(String key : playNumList.keySet()){
			String match = key.replace("match_", "");
			Query query = new Query().addCriteria(Criteria.where("match").is(match));
			boolean has = mongoTemplate.exists(query, lpl_play_num_list_cn);
			if(!has){
				LinkedTreeMap<String,Object> item = (LinkedTreeMap<String, Object>)playNumList.get(key);
				item.put("match", match);
				
				logger.debug("==========>>add lpl play num match:{}",match);
				mongoTemplate.insert(item, lpl_play_num_list_cn);
			}
		}
		
		
		ArrayList<LinkedTreeMap<String,Object>> guessList = (ArrayList<LinkedTreeMap<String, Object>>) data.get("guessList");
		for(LinkedTreeMap<String,Object> item : guessList){
			String matchId = (String) item.get("matchId");
			Query query = new Query().addCriteria(Criteria.where("matchId").is(matchId));
			boolean has = mongoTemplate.exists(query, lpl_guess_list_cn);
			if(has){
				logger.debug("=============>>lpl guess remove match matchId:{}",matchId);
				mongoTemplate.remove(query, lpl_guess_list_cn);				
			}
			logger.debug("==========>>add lpl guess match matchId:{}",matchId);
			mongoTemplate.insert(item, lpl_guess_list_cn);
		}
	}

	@Override
	public boolean handle(JobExecutionContext arg0, Map<String, Object> retMap) {
		Map<String,String> header = new HashMap<String,String>();
		header.put("accept-language", "zh-CN,zh;q=0.8");
		header.put("cookie", "_gscu_661903259=87560627xf4fn426; pgv_pvi=4976998400; RK=dFF6VyzKfW; pac_uid=1_270852221; tvfe_boss_uuid=e689f84a31f2cc76; eas_sid=w174c984I8V1j3s137Y4Q9W627; LW_uid=K13419x4z8I1z3U1S7f4U9h9v8; _qpsvr_localtk=tk4481; o_cookie=270852221; pgv_si=s6268758016; LW_sid=m1v4M9U5X7t7A0E9l674Z4C7x3; vb2ctag=107_201; ptisp=cnc; ptcz=d47089ea60a216fb8b9df4eb9ecd2ff200bc23d1e2866155a1879444c80b48e0; pt2gguin=o0270852221; uin=o0270852221; skey=@eFm558Oqp; IED_LOG_INFO2=userUin%3D270852221%26nickName%3D%2525E6%2525AF%252585%2525E7%2525A2%2525BC%2525E5%2525B9%2525B3%2525E5%2525B7%25259D%2525E4%2525B8%2525B6%26userLoginTime%3D1495770992; lol_areaId=2; lol_roleId=2927932645; lol_areaName=%25E6%25AF%2594%25E5%25B0%2594%25E5%2590%2589%25E6%25B2%2583%25E7%2589%25B9%2520%25E7%25BD%2591%25E9%2580%259A; ts_refer=lpl.qq.com/es/guess/; lol_externalKey=874ae9e120374955d10de1d67aaa9af92a8f2aa49239de4f8b576603e340bd33; pgv_info=ssid=s8010872146&pgvReferrer=; ts_uid=8929654546; pgv_pvid=8377197295");
		header.put("referer", "https://qs.888.qq.com/m_qq/es/es.lol.html?channelName=landing");
		header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		header.put("x-requested-with", "XMLHttpRequest");
		header.put("Content-Type","text/html;charset=UTF-8"); 
		this.setHeader(header);
		this.addSeed("https://qs.888.qq.com/node_esports/?d=es&c=esLOL&m=getTopicInfoLoop&ajax=true&cms_where=603005&vb2ctag=1_64_1_5475&reportUin=270852221&bc_web=132937075&t=1495777878058&g_tk=225927407&_="+System.currentTimeMillis());
		this.addRegex("https://qs.888.qq.com/node_esports/.*");
		//1495777878059
		return false;
	}
	
	public static void main(String[] args) throws JobExecutionException {
		new LplMatchCrawler().execute(null);
	}

}
