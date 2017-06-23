package com.ct.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载当前运行环境
 * 2017-05-02
 * @author caiqianyi
 *
 */
public class EnvLoader {
	
	static Logger logger = LoggerFactory.getLogger(EnvLoader.class);
	 
    private static String active = null;//环境配置
    
    private static Set <String> filters = new HashSet<String>();
    
    private static List<String> as = Arrays.asList(new String[]{"dev","test","prod","uat"});
    
    private static List<String> envs;
    
    private static Boolean isRemote = null;//是否升级为远程配置文件
    
    static{
    	envs = readFileByLines("/webroot/env.ini");//读取ini文件确定当前配置环境
    	
    	if(envs == null || envs.isEmpty()){
    		throw new Error("‘/webroot/env.ini’ file read fail!!!");
    	}
    	
    	if(StringUtils.isBlank(envs.get(0)) || !as.contains(envs.get(0))){
    		throw new Error("env value is fail!!!");
    	}
    	
    	active = envs.get(0);
    	
    	filters.addAll(getFilterFile(envs));//读取配置文件中过滤配置
    	filters.add("/sms.properties");
    	filters.add("/i18n_messages.properties");
    	filters.add("/verify.properties");
    	filters.add("/public.properties");
    }
    
    public static synchronized String getEnv(){
    	return active;
    }
    
    public static synchronized String getActive(String profile){
    	String env = null;
		switch (active) {
		case "dev":
			env="/dev";
			break;
		case "test":
			env="/test";
			break;
		case "uat":
			env = profile.startsWith("/pay") ? "/uat":"";
			break;
		default:
			env = "";
			break;
		}
    	return env;
    }
    
    public static synchronized boolean isRemote(){
    	if(isRemote != null){
    		return isRemote;
    	}
    	for(String line : envs){
    		String fstr = "remote:";
    		if(line.startsWith(fstr)){
    			String value = line.substring(fstr.length()).trim();
    			isRemote = Boolean.valueOf(value);
				return isRemote;
    		}
    	}
    	return false;
    }
    
    public static synchronized String getActivePropfiles(String profile){
    	return getFilterEnv(profile)+profile;
    }
    
    public static synchronized String getFilterEnv(String profile){
    	String env = getActive(profile);
    	if(filters.contains(profile)){//过滤不需要区分环境配置
    		env = "";
    	}
    	return env;
    }
    
    /**
     * 获取配置文件中 过滤文件名
     * @param envs
     * @return
     */
    private static List<String> getFilterFile(List<String> envs){
    	List<String> list = new ArrayList<String>();
    	for(String line : envs){
    		String fstr = "filter:";
    		if(line.startsWith(fstr)){
    			String file = line.substring(fstr.length()).trim();
    			list.add(file);
    		}
    	}
    	return list;
    }
    
    public static String getProject(){
    	for(String line : envs){
    		String fstr = "project:";
    		if(line.startsWith(fstr)){
    			String name = line.substring(fstr.length()).trim();
    			return name;
    		}
    	}
    	return null;
    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static List<String> readFileByLines(String fileName) {
        BufferedReader reader = null;
        try {
        	File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            
            List<String> buf = new ArrayList<String>();
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	buf.add(tempString);
            }
            reader.close();
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }
    
}
