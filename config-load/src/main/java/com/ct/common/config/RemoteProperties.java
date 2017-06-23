package com.ct.common.config;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
 
/**
 * load remote properties
 * 2017-05-02
 * @author caiqianyi
 */
public class RemoteProperties extends PropertiesSupport {
 
	static Logger logger = LoggerFactory.getLogger(RemoteProperties.class);
 
    private static synchronized String getRemoteHost(){
    	String remoteHost = null;
    	switch (EnvLoader.getEnv()) {
		case "prod":
			//to do set prod remote host
			remoteHost = "http://10.162.81.131:7369/";
			break;
		case "uat":
			//to do set prod remote host
			remoteHost = "http://10.162.81.131:7369/";
			break;
		case "test":
			remoteHost = "http://120.76.194.194:7369/";
		default:
			//to do set dev remote host
			remoteHost = "http://192.168.10.241:9027/";
			break;
		}
    	logger.debug("remote host:{}",remoteHost);
    	return remoteHost;
    }
    
    private static String getAuthString(){
    	String authString = null;
    	switch (EnvLoader.getEnv()) {
		case "prod":
			//to do set prod remote host
			authString = "config:QWERtyuiP";
			break;
		case "uat":
			//to do set prod remote host
			authString = "config:QWERtyuiP";
			break;
		default:
			//to do set dev remote host
			authString = "config:QWERtyuiP";
			break;
		}
    	logger.debug("authString:{}",authString);
    	return authString;
    }
    
    public static Properties loadProperties(String profile){
    	Properties properties = null;
    	try {
    		String host = getRemoteHost(),project = EnvLoader.getProject(),env = EnvLoader.getEnv();
    		
    		switch (env) {
			case "dev":
				env = "/dev";
				break;
			case "test":
				env = "/test";
				break;
			case "uat":
				env = profile.startsWith("/pay") ? "/uat":"/product";
				break;
			case "prod":
				env = "/dev";
				break;
			default:
				break;
			}
    		String url = host + project + env + "/master" + profile;
    		logger.info("loading remote properties url:{}", url);
    		properties = new Properties();
    		URL u = new URL(url);
    		HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
    		String authString = getAuthString();
    	    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
    	    String authStringEnc = new String(authEncBytes);
    		httpConn.setRequestProperty("Authorization", "Basic "+authStringEnc);//Y29uZmlnOlFXRVJ0eXVpUA==
    		properties.load(httpConn.getInputStream());
    	} catch (IOException e) {
    		logger.error("loadProperty",e);
    		e.printStackTrace();
    	}
    	return properties;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	String profile = this.getProfile();
    	Assert.notNull(profile);
	    this.setProperties(loadProperties(profile));
    }
    
    public static void main(String[] args) {
    	loadProperties("/test.properties");
	}
}