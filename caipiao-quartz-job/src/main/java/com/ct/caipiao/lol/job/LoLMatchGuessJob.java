package com.ct.caipiao.lol.job;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ct.commons.utils.Assert;
import com.ct.commons.utils.DateUtil;

@Component
@Scope("prototype")
public class LoLMatchGuessJob extends AbstractLoLMatchJob{
	
	Logger logger = LoggerFactory.getLogger(LoLMatchGuessJob.class);
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	private String lpl_match_guess_list = "lpl_match_guess_list";
	
	@SuppressWarnings("unchecked")
	public void visit(String body) {
		
		Assert.notNull(body);
		
		try{
			JSONObject datas = JSONObject.parseObject(body);
			String retCode = datas.getString("retCode"),errCode=datas.getString("errCode"),result=datas.getString("result");
			
			if(!("0".equals(retCode) && "0".equals(errCode) && "0".equals(result))){
				logger.error("retCode={},errCode={},result={}",retCode,errCode,result);
				return ;
			}
			
			JSONObject data = (JSONObject) datas.get("data");
			JSONArray guessList = data.getJSONArray("guessList");
			for(int i=0;i<guessList.size();i++){
				JSONObject item = guessList.getJSONObject(i);
				
				/*  "version": 20170620,
	                "matchId": "M4549",
	                "homeName": "DAN",
	                "awayName": "JDG",
	                "homeLogoUrl": "//static.gtimg.com/vd/act/ggimg/201705/a70de12c61bf412a0a9dfe7a1d0e34fd.png",
	                "awayLogoUrl": "//static.gtimg.com/vd/act/ggimg/201705/10f6f0590c6cd241c44a3286b465b21c.png",
	                "startTime": 1500710457,
	                "endTime": 1500738957,
	                "createTime": 1500020999,
	                "matchName": "20170604*DAN VS 20170604*JDG",
	                "matchState": 20,
	                "matchType": 3,
	                "result": "0:0",
	                "sportName": "2017 LPL 夏季赛",
	                "body": "DAN|JDG",
	                "extInfo": "",
	                "matchTxt": "夏季赛",
	                "dayTxt": "07月22日",
	                "timeTxt": "16:00",
				 */
				
				String homeName = item.getString("homeName");
				String awayName = item.getString("awayName");
				String homeLogoUrl = item.getString("homeLogoUrl");
				String awayLogoUrl = item.getString("awayLogoUrl");
				Long startTime = item.getLong("startTime") * 1000,endTime = item.getLong("endTime") * 1000;//开始
				
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(startTime);
				c.set(Calendar.SECOND, 0);
				startTime = c.getTimeInMillis();
				c.setTimeInMillis(endTime);
				c.set(Calendar.SECOND, 0);
				endTime = c.getTimeInMillis();
				
				logger.info("home_name={},away_name={},startTime={}",homeName,awayName,DateUtil.formatDate(new Date(startTime)),DateUtil.formatDate(new Date(endTime)));
				
				Query query = new Query().addCriteria(Criteria.where("home_name").is(homeName).and("away_name").is(awayName).and("match_date").is(startTime));
				Map<String,String> match = mongoTemplate.findOne(query,Map.class, LoLMatchJob.lol_matchs_cn);
				
				if(match == null){
					continue;
				}
				
				if(match.get("home_logo_url") == null){
					match.put("home_logo_url", homeLogoUrl);
					mongoTemplate.upsert(query, Update.update("home_logo_url", homeLogoUrl), Map.class);
				}
				
				if(match.get("away_logo_url") == null){
					match.put("away_logo_url", awayLogoUrl); 
					mongoTemplate.upsert(query, Update.update("away_logo_url", awayLogoUrl), Map.class); 
				}
				
				JSONArray topics = item.getJSONArray("topicList");
				logger.info("topics.size={}",topics.size());
				if(topics.isEmpty()){
					continue;
				}
				JSONObject sfGuessJ = getGuess(topics,"全场比赛结果为");
				
				/*{
	                "stock": 0,
	                "betState": 2,
	                "guessType": 15,
	                "drawTime": 1500716880,
	                "endTime": 1500720480,
	                "attach": {},
	                "optionFormat": {
	                    "A": {
	                        "desc": "DAN 胜",
	                        "logo": "",
	                        "probability": "",
	                        "odds": "1.40",
	                        "state": 3
	                    },
	                    "C": {
	                        "desc": "JDG 胜",
	                        "logo": "",
	                        "probability": "",
	                        "odds": "2.35",
	                        "state": 3
	                    }
	                },
	                "drawContent": "",
	                "extInfo": {
	                    "categoryId": "djlol_lol",
	                    "createType": "system",
	                    "drawTime": "1500738957",
	                    "filterRule": "",
	                    "guessDesc": "%E5%85%A8%E5%9C%BA%E6%AF%94%E8%B5%9B%E7%BB%93%E6%9E%9C%E4%B8%BA%EF%BC%9F",
	                    "hotFlag": "1",
	                    "keyword": "",
	                    "match": "DAN%7CJDG",
	                    "matchid": "M4549",
	                    "playName": "djlol_bssf",
	                    "projectId": "djlol",
	                    "relateKey": "749#4549#5",
	                    "sportname": "2017%2BLPL%2B%E5%A4%8F%E5%AD%A3%E8%B5%9B"
	                },
	                "guessContent": "2017 LPL 夏季赛，7月22日，DAN VS JDG全场比赛结果为？",
	                "isEnd": false,
	                "guessStatus": 10,
	                "itemType": 5,
	                "startTime": 1499500857,
	                "topicId": "T17071415000000203119334",
	                "shareKey": "",
	                "createTime": 1500021003,
	                "char1": "LOL",
	                "unitPrice": 0,
	                "officialId": "M4549",
	                "guessOfficialId": "T17071415000000203119334"
	            }*/
				Query sf_query = new Query().addCriteria(Criteria.where("match_id").is(match.get("match_id")).and("play_type").is("sf").and("bet_state").is("2"));
				if(sfGuessJ != null){
					
					Map<String,Object> sfGuess = mongoTemplate.findOne(sf_query, Map.class,lpl_match_guess_list);
					boolean sf_has = sfGuess != null;
					if(sfGuess == null){
						sfGuess = new HashMap<String,Object>();
					}
					String sf_sbonus = sfGuessJ.getJSONObject("optionFormat").getJSONObject("A").getString("odds");
					String sf_fbonus = sfGuessJ.getJSONObject("optionFormat").getJSONObject("C").getString("odds");
					
					sfGuess.put("play_type", "sf");
					sfGuess.put("match_id", match.get("match_id"));
					sfGuess.put("bet_state", sfGuessJ.getLong("betState").toString());
					sfGuess.put("guess_end_time", (sfGuessJ.getLong("endTime")*1000)+"");
					sfGuess.put("guess_type", sfGuessJ.getLong("endTime").toString());
					sfGuess.put("guess_status", sfGuessJ.getLong("guessStatus").toString());
					sfGuess.put("is_end", sfGuessJ.getBoolean("isEnd").toString());
					Map<String,List<Double>> odds = (Map<String, List<Double>>) sfGuess.get("odds");
					Double s_odds = Double.parseDouble(sf_sbonus),f_odds = Double.parseDouble(sf_fbonus);
					if(odds == null){
						odds = new HashMap<String,List<Double>>();
						odds.put("s", Arrays.asList(new Double[]{s_odds}));
						odds.put("f", Arrays.asList(new Double[]{f_odds}));
					}else{
						List<Double> sl = odds.get("s"),fl = odds.get("f");
						if(!sl.get(sl.size()-1).equals(s_odds)){
							sl.add(s_odds);
						}
						if(!fl.get(fl.size()-1).equals(f_odds)){
							fl.add(f_odds);
						}
					}
					sfGuess.put("odds", odds);
					logger.info("sf_sbonus={},sf_fbonus={}",sf_sbonus,sf_fbonus);
					if(sf_has){
						mongoTemplate.remove(sf_query, lpl_match_guess_list);	
					}
					mongoTemplate.insert(sfGuess, lpl_match_guess_list);
				}else{
					mongoTemplate.findAndModify(sf_query, Update.update("bet_state", "3"), Map.class,lpl_match_guess_list);
				}
				
				/*{
	                "stock": 0,
	                "betState": 2,
	                "guessType": 15,
	                "drawTime": 1500716880,
	                "endTime": 1500720480,
	                "attach": {},
	                "optionFormat": {
	                    "0:2": {
	                        "desc": "0:2",
	                        "logo": "",
	                        "probability": "",
	                        "odds": "4.75",
	                        "state": 3
	                    },
	                    "1:2": {
	                        "desc": "1:2",
	                        "logo": "",
	                        "probability": "",
	                        "odds": "4.40",
	                        "state": 3
	                    },
	                    "2:0": {
	                        "desc": "2:0",
	                        "logo": "",
	                        "probability": "",
	                        "odds": "2.15",
	                        "state": 3
	                    },
	                    "2:1": {
	                        "desc": "2:1",
	                        "logo": "",
	                        "probability": "",
	                        "odds": "3.60",
	                        "state": 3
	                    }
	                },
	                "drawContent": "",
	                "extInfo": {
	                    "categoryId": "djlol_lol",
	                    "createType": "system",
	                    "drawTime": "1500738957",
	                    "filterRule": "",
	                    "guessDesc": "%E5%85%A8%E5%9C%BA%E6%AF%94%E8%B5%9B%E8%83%9C%E5%B1%80%E6%AF%94%E5%88%86%E4%B8%BA%EF%BC%9F",
	                    "hotFlag": "2",
	                    "match": "DAN%7CJDG",
	                    "matchid": "M4549",
	                    "playName": "djlol_bsbf",
	                    "projectId": "djlol",
	                    "relateKey": "749#4549#5",
	                    "sportname": "2017+LPL+%E5%A4%8F%E5%AD%A3%E8%B5%9B"
	                },
	                "guessContent": "2017 LPL 夏季赛，7月22日，DAN VS JDG全场比赛胜局比分为？",
	                "isEnd": false,
	                "guessStatus": 10,
	                "itemType": 5,
	                "startTime": 1499500857,
	                "topicId": "T17071415000000203119335",
	                "shareKey": "",
	                "createTime": 1500021003,
	                "char1": "LOL",
	                "unitPrice": 0,
	                "officialId": "M4549",
	                "guessOfficialId": "T17071415000000203119335"
	            }*/
				
				JSONObject bfGuessJ = getGuess(topics,"全场比赛胜局比分为");
				Query bf_query = new Query().addCriteria(Criteria.where("match_id").is(match.get("match_id")).and("play_type").is("bf").and("bet_state").is("2"));
				if(bfGuessJ != null){
					Map<String,Object> bfGuess = mongoTemplate.findOne(bf_query, Map.class,lpl_match_guess_list);
					boolean bf_has = bfGuess != null;
					if(bfGuess == null){
						bfGuess = new HashMap<String,Object>();
					}
					
					String bf_1_bonus = bfGuessJ.getJSONObject("optionFormat").getJSONObject("0:2").getString("odds");
					String bf_2_bonus = bfGuessJ.getJSONObject("optionFormat").getJSONObject("1:2").getString("odds");
					String bf_3_bonus = bfGuessJ.getJSONObject("optionFormat").getJSONObject("2:0").getString("odds");
					String bf_4_bonus = bfGuessJ.getJSONObject("optionFormat").getJSONObject("2:1").getString("odds");
					logger.info("bf_1_bonus={},bf_2_bonus={},bf_3_bonus={},bf_4_bonus={}",bf_1_bonus,bf_2_bonus,bf_3_bonus,bf_4_bonus);
					
					Map<String,List<Double>> odds = (Map<String, List<Double>>) bfGuess.get("odds");
					Double bf_1_odds = Double.parseDouble(bf_1_bonus),bf_2_odds = Double.parseDouble(bf_2_bonus),bf_3_odds = Double.parseDouble(bf_3_bonus),bf_4_odds = Double.parseDouble(bf_4_bonus);
					if(odds == null){
						odds = new HashMap<String,List<Double>>();
						odds.put("_1", Arrays.asList(new Double[]{bf_1_odds}));
						odds.put("_2", Arrays.asList(new Double[]{bf_2_odds}));
						odds.put("_3", Arrays.asList(new Double[]{bf_3_odds}));
						odds.put("_4", Arrays.asList(new Double[]{bf_4_odds}));
					}else{
						List<Double> _1l = odds.get("_1"),_2l = odds.get("_2"),_3l = odds.get("_3"),_4l = odds.get("_4");
						if(!_1l.get(_1l.size()-1).equals(bf_1_odds)){
							_1l.add(bf_1_odds);
						}
						if(!_2l.get(_2l.size()-1).equals(bf_2_odds)){
							_2l.add(bf_2_odds);
						}
						if(!_3l.get(_3l.size()-1).equals(bf_3_odds)){
							_3l.add(bf_3_odds);
						}
						if(!_4l.get(_4l.size()-1).equals(bf_4_odds)){
							_4l.add(bf_4_odds);
						}
					}
					
					bfGuess.put("play_type", "bf");
					bfGuess.put("match_id", match.get("match_id"));
					bfGuess.put("bet_state", bfGuessJ.getLong("betState").toString());
					bfGuess.put("guess_end_time", bfGuessJ.getLong("endTime").toString());
					bfGuess.put("guess_type", bfGuessJ.getLong("endTime").toString());
					bfGuess.put("guess_status", bfGuessJ.getLong("guessStatus").toString());
					bfGuess.put("is_end", bfGuessJ.getBoolean("isEnd").toString());
					bfGuess.put("odds", odds);
					
					if(bf_has){
						mongoTemplate.remove(bf_query, lpl_match_guess_list);	
					}
					mongoTemplate.insert(bfGuess, lpl_match_guess_list);
				}else{
					mongoTemplate.findAndModify(bf_query, Update.update("bet_state", "3"), Map.class,lpl_match_guess_list);
				}
			}
		}catch(Exception e){
			logger.error("body={}",body,e);
		}
	}
	
	private JSONObject getGuess(JSONArray topics,String contentWith){
		for(int i=0;i<topics.size();i++){
			JSONObject guess = topics.getJSONObject(i);
			if(guess.getString("guessContent").contains(contentWith)){
				return guess;
			}
		}
		return null;
	}
	
	public String request(String url){
		
        Map<String,String> header = new HashMap<String,String>();
		header.put("accept-language", "zh-CN,zh;q=0.8");
		header.put("referer", "https://qs.888.qq.com/m_qq/es/es.lol.html?channelName=landing");
		header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		header.put("x-requested-with", "XMLHttpRequest");
		header.put("Content-Type","text/html;charset=UTF-8"); 
		
		return doGet(url, header);
	}
	

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		// TODO Auto-generated method stub
		String url = "https://qs.888.qq.com/node_pool2/?d=es&c=esLOL&m=getTopicInfoLoop&ajax=true&cms_where=603005&vb2ctag=1_64_1_5475&bc_web=484664209&t=1500691597438&g_tk=5381&_=1500691597439";
		
        visit(request(url));
	}
	
	public static void main(String[] args) {
		String url = "https://qs.888.qq.com/node_pool2/?d=es&c=esLOL&m=getTopicInfoLoop&ajax=true&cms_where=603005&vb2ctag=1_64_1_5475&bc_web=484664209&t=1500691597438&g_tk=5381&_=1500691597439";
		LoLMatchGuessJob lmgj = new LoLMatchGuessJob();
		String body = lmgj.request(url);
		lmgj.visit(body);
	}

}