package com.ct.commons.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 微信客服記錄處理工具類
 * @author tanhb
 * 2016-09-28
 *
 */
public class WxCsRecUtil {
	/**
	 * 獲取所有微信公衆號的信息
	 * @return List<Map<String,String>>
	 */
	public static List<Map<String,String>> area() {
    	List<String> list=null;
		List<Map<String,String>> listArea = new ArrayList<Map<String,String>>();
    	try {
			list=PropertiesUtil.getAllKeyListByValueName("/gameconfig/wx_app_sec.properties");//获取所有的游戏id
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<list.size();i++){
			Map<String,String> map = new HashMap<String,String>();
			String serverCode=list.get(i);
			String cityServer = PropertiesUtil.getPropertie(serverCode, "/gameconfig/wx_app_sec.properties") + "";
			Map<String, String> cityServerMap = JsonUtil.json2Map(cityServer);
			String AppID=cityServerMap.get("appid");
			String AppSecret = cityServerMap.get("secret");
			map.put("appid", AppID);
			map.put("secret",AppSecret);
			listArea.add(map);
		}
		return listArea;
	}
	
	/**
	 * 
	 * 生成公衆號的token值
	 * @param appid
	 * @param secret
	 * @return
	 */
	public static String getTocken(String appid, String secret) {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
	    String re =null; 
	    try {
	    	re = HttpClientUtil.doGetRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	  
	    Map<String,String> tokenMap=JsonUtil.json2Map(re);
	    String access_token = tokenMap.get("access_token");
		return access_token;
	} 
	
	/**
	 * 獲取玩家信息
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static String getnickName(String access_token, String openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
		String re = null;
		try {
			re = HttpClientUtil.doGetRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
		/*JSONObject userInfo = JSONObject.parseObject(re);
		System.out.println(userInfo);
		return userInfo.getString("nickname");*/
	}
	
	/**
	 * 獲取玩家信息
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static String getKfInfo(String access_token) {
		String url = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=" + access_token + "&lang=zh_CN";
		String re = null;
		try {
			re = HttpClientUtil.doGetRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject kfInfo = JSONObject.parseObject(re);
		System.out.println(kfInfo);
		return kfInfo.getString("kf_nick");
	}
	
	public static String httpClientPost(String access_token, String json) {
		String serverUrl = "https://api.weixin.qq.com/customservice/msgrecord/getrecord?access_token=" + access_token;
		HttpClient httpClient = new DefaultHttpClient();
		String content = null;
		try {
			HttpPost post = new HttpPost(serverUrl);
			StringEntity postingString = new StringEntity(json);// json传递
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			HttpResponse response = httpClient.execute(post);
			content = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void main(String[] args) {
//		String tk = getTocken("wx55197c12abe10c0c","4c625ed92041b0cdd0477ac82db50cea");
//		System.out.println(tk);
//		System.out.println(getnickName(tk, "ocoeKxLGArqVALHg-_fDm6nXvijU"));
		List<Map<String,String>> ar = area();
		for(Map<String,String> a : ar){
			String ap = a.get("appid").toString();
			String se = a.get("secret").toString();
			String tk = getTocken(ap, se);
			System.out.println(ap);
			getKfInfo(tk);
		}
		
	}

}
