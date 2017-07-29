package com.ct.caipiao.lol.job;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ct.commons.utils.Assert;
import com.ct.commons.utils.DateUtil;

@Component
@Scope("prototype")
public class LoLMatchJob extends AbstractLoLMatchJob{
	
	Logger logger = LoggerFactory.getLogger(LoLMatchJob.class);
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	public final static String lol_matchs_cn = "lol_matchs";
	
	private final static Map<String,String> leagues = new HashMap<String,String>();
	
	static{
		leagues.put("7", "lspl");//lspl甲级联赛
		
		leagues.put("4", "demaxiya");//德玛西亚杯
		leagues.put("20", "csyxzbs");//城市英雄争霸赛
		leagues.put("9", "gxls");//高校联赛
		leagues.put("39", "xjjys");//校际精英赛
		
		leagues.put("5", "lpl");
		leagues.put("68", "lms");//台港澳职业联赛
		leagues.put("51", "lck");//韩国职业联赛
		leagues.put("54", "lcs_oz");//欧洲职业联赛
		leagues.put("55", "lcs_bm");//北美职业联赛
		
		leagues.put("1", "wcs");//全球总决赛
		leagues.put("8", "msi");//MSI季中邀请赛
		leagues.put("71", "zjxls");//洲际系列赛
		leagues.put("6", "allstar");//全明星赛
	}
	public void visit(String body,Integer p8) {
		
		Assert.notNull(body);
		
		String json = body.replace("var retObj=", "").replace(";", "");
		JSONObject datas = JSONObject.parseObject(json);
		String status = datas.getString("status");
		
		if("0".equals(status)){
			JSONObject msg = (JSONObject) datas.get("msg");
			String total = msg.getString("total"),totalpage=msg.getString("totalpage"),page = msg.getString("page");
			logger.info("total={},totalpage={},page={}",total,totalpage,page);
			
			JSONArray result = msg.getJSONArray("result");
			logger.info("result.size={}",result.size());
			
			for(int i=0;i<result.size();i++){
				JSONObject data = result.getJSONObject(i);
				/*{
				    "bMatchId": "2672",
				    "bMatchName": "EDG VS JDG",
				    "GameId": "49",
				    "GameName": "2017职业联赛",
				    "GameTypeId": "7",
				    "GameMode": "3",
				    "GameTypeName": "夏季赛常规赛",
				    "GameProcId": "160",
				    "GameProcName": "第六周",
				    "TeamA": "1",
				    "ScoreA": "2",
				    "TeamB": "29",
				    "ScoreB": "0",
				    "MatchDate": "2017-07-20 17:00:00",
				    "MatchStatus": "3",
				    "MatchWin": "1",
				    "iQTMatchId": "24290",
				    "bGameId": "5",
				    "NewsId": "11900",
				    "HighlightsId": "11899",
				    "Video1": "0",
				    "Video2": "0",
				    "Video3": "764502578",
				    "Chat1": "94961178",
				    "Chat2": "http://zhibojiasu.tuwan.com/EDGvsJDG0720live.htm",
				    "Chat3": "",
				    "News1": "http://zhibojiasu.tuwan.com/EDGvsJDG0720news.html",
				    "News2": "http://zhibojiasu.tuwan.com/EDGvsJDG0720zhanbao.html",
				    "News3": ""
				}*/
				Map<String,Object> match = new HashMap<String,Object>();
				
				String  mathName = data.getString("bMatchName");
				String names[] = mathName.replaceAll(" ", "").replaceAll("VS", "vs").split("vs");
				
				logger.info("home.name={},away.name={},league={},score={},match.time={},status={}",names[0],names[1],leagues.get(p8.toString()),data.getString("ScoreA")+":"+data.getString("ScoreB"),data.getString("MatchDate"),data.getString("MatchStatus"));
				
				match.put("match_id", data.getString("bMatchId"));
				match.put("match_name", data.getString("bMatchName"));
				match.put("game_id",data.getString("GameId"));
				match.put("game_name", data.getString("GameName"));
				match.put("game_type_id", data.getString("GameTypeId"));
				match.put("game_mode", data.getString("GameMode"));
				match.put("game_type_name", data.getString("GameTypeName"));
				match.put("game_proc_id", data.getString("GameProcId"));
				match.put("game_proc_name", data.getString("GameProcName"));
				match.put("home_team_id", data.getString("TeamA"));
				match.put("home_name", names[0]);
				match.put("home_score", data.getString("ScoreA"));
				
				match.put("away_team_id", data.getString("TeamB"));
				match.put("away_name", names[1]);
				match.put("away_score", data.getString("ScoreB"));
				
				match.put("match_status", data.getString("MatchStatus"));
				match.put("match_result", data.getString("MatchWin"));
				match.put("b_game_id", data.getString("bGameId"));
				match.put("iqt_match_id", data.getString("iQTMatchId"));
				match.put("text_live_href", data.getString("Chat2"));//文字直播地址
				match.put("league", leagues.get(p8.toString()));
				try {
					long matchDate = DateUtils.parseDate(data.getString("MatchDate"), "yyyy-MM-dd HH:mm:ss").getTime();
					match.put("match_date", matchDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Query query = new Query().addCriteria(Criteria.where("match_id").is(match.get("match_id")));
				boolean has = mongoTemplate.exists(query, lol_matchs_cn);
				logger.info("match.exists={}",has);
				if(has){
					mongoTemplate.remove(query, lol_matchs_cn);	
				}
				mongoTemplate.insert(match, lol_matchs_cn);
				
			}
		}
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
		
		String dataJson = (String) context.getJobDetail().getJobDataMap().get("dataJson");
		
		if(StringUtils.isBlank(dataJson)){//
			return ;
		}
		
		run(dataJson);
	}
	
	public void run(String body){
		//系统配置
		
		JSONObject json = JSONObject.parseObject(body);
		Long s = json.getLong("start"),e = json.getLong("end");
		Integer p8 = json.getInteger("p8"),pageSize = json.getInteger("pageSize");
		
		if(s == null){//更新最新的
			s = new Date().getTime();
		}
		if(e == null){//更新最新的
			e = new Date().getTime();
		}
		if(pageSize == null){
			pageSize = 500;
		}
		
		Date start = new Date(s),end = new Date(e);
		
		// TODO Auto-generated method stub
		String url = "http://apps.game.qq.com/lol/match/apis/searchBMatchInfo.php?p8="+p8+"&p1=&p9="+DateUtil.formatDatetime(start, "yyyy-MM-dd")+"%2000:00:00&p10="+DateUtil.formatDatetime(end, "yyyy-MM-dd")+"%2023:59:59&p6=3&pagesize="+pageSize+"&r1=retObj&_=1500702932923";
		
        visit(request(url),p8);
	}

}