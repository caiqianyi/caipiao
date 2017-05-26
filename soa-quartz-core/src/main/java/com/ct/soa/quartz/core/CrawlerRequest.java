package com.ct.soa.quartz.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.Request;
import cn.edu.hfut.dmic.webcollector.net.Response;

public class CrawlerRequest implements Request {
	
	private Logger logger = LoggerFactory.getLogger(CrawlerRequest.class);
		
	private Map<String,String> header;
	
    private URL url;
    
    @Override  
    public URL getURL() {  
        return url;  
    }  

    @Override  
    public void setURL(URL url) {  
        this.url = url;  
    } 

	@Override
	public Response getResponse(CrawlDatum datum) throws Exception {
		HttpResponse response = new HttpResponse(url);  
		  
        /*通过httpclient来获取http请求的响应信息*/  
        HttpClient client = new DefaultHttpClient();  
        HttpGet httpGet = new HttpGet(getURL().toString());  
        
        if(header != null){
        	for(String key : header.keySet()){
        		httpGet.setHeader(key,header.get(key));
        	}
        	
        }
        
        /*这里用的是httpclient的HttpResponse，与WebCollector中的HttpResponse无关*/  
        org.apache.http.HttpResponse httpClientResponse = client.execute(httpGet);
        
        /* 
     	将httpclient获取的http响应头信息放入Response 
     	Response接口中要求http头是Map<String,List<String>>类型，所以需要做个转换 
        */  
	    Map<String, List<String>> headers = new HashMap<String, List<String>>();  
	    for (Header header : httpClientResponse.getAllHeaders()) {  
	        List<String> values = new ArrayList<String>();  
	        values.add(header.getValue());  
	        headers.put(header.getName(), values);  
	    }  
	    response.setHeaders(headers);
        
        HttpEntity entity = httpClientResponse.getEntity();  

        /*设置http响应码，必须设置http响应码，否则会影响抓取器对抓取状态的判断*/  
        response.setCode(httpClientResponse.getStatusLine().getStatusCode());  

        /*设置http响应内容，为网页(文件)的byte数组*/  
        response.setContent(EntityUtils.toByteArray(entity));  

        /* 
	         这里返回的是HttpResponse类型，它的getContentType()方法会自动从getHeader()方法中 
	         获取网页响应的content-type,如果自定义Response，一定要实现getContentType()方法，因 
	         为网页解析器的生成需要依赖content-type 
         */  
        
        return response; 
	}

	public void setHeader(Map<String,String> header) {
		this.header = header;
	}

}
