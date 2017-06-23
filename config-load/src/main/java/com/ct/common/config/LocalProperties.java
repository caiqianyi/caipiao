package com.ct.common.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
 
/**
 * load classpath env properties
 * 2017-05-02
 * @author caiqianyi
 */
public class LocalProperties extends PropertiesSupport{
 
	static Logger logger = LoggerFactory.getLogger(LocalProperties.class);
	
    public static Properties loadProperties(String profile){
		String path = EnvLoader.getActivePropfiles(profile);
		logger.info("loading local properties classpath:{}", path);
		String locationPattern = "classpath*:"+path;
		Properties properties = loadUniquePatternProperties(locationPattern);
		if(properties == null){
			properties= loadActiveProperties(profile);
		}
    	return properties;
    }
    
    public static Properties loadActiveProperties(String profile){
    	Properties properties = null;
    	try {
    		String path = EnvLoader.getActivePropfiles(profile);
    		logger.info("loading local properties classpath:{}", path);
    		properties = new Properties();
    		InputStream in = LocalProperties.class.getResourceAsStream(path);
			properties.load(in);
			in.close();
    	} catch (IOException e) {
    		logger.error("loadProperty",e);
    		e.printStackTrace();
    	}
    	return properties;
    }
    
    public static Properties[] loadPatternProperties(String locationPattern){
    	org.springframework.core.io.Resource[] resourcep;
		try {
			resourcep = new PathMatchingResourcePatternResolver().getResources(locationPattern);
			if(resourcep.length == 0) return null;
			Properties[] props = new Properties[resourcep.length];
			for(int i=0;i<resourcep.length;i++){
				props[i] = new Properties();
				props[i].load(resourcep[i].getInputStream());
			}
			return props;
		} catch (IOException e) {
			throw new RuntimeException("加载文件"+locationPattern+"失败， " + e.toString());
		}
    }
    
    public static Properties loadUniquePatternProperties(String locationPattern){
    	Properties[] props = loadPatternProperties(locationPattern);
    	if(props== null){
    		logger.error("‘"+locationPattern+"’ loading fail,file not found!");
    		return null;
    	}
    	if(props.length > 1){
    		logger.error("‘"+locationPattern+"’ loading fail,exist multiple file! props length:{}",props.length);
    		return null;
    	}
    	return props[0];
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	String profile = this.getProfile();
    	Assert.notNull(profile);
	    this.setProperties(loadProperties(profile));
    }
}