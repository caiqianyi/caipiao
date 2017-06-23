package com.ct.common.config;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
 
/**
 * Base class for JavaBean-style components that need to load properties
 * load classpath properties support
 * 2017-05-02
 * @author caiqianyi
 */
public class PropertiesSupport implements InitializingBean, FactoryBean<Properties>{
 
	Logger logger = LoggerFactory.getLogger(PropertiesSupport.class);

	private String profile;
	 
    private Properties properties = new Properties();
 
    public Properties getObject() throws Exception {
        return properties;
    }
 
    public Class<?> getObjectType() {
        return properties.getClass();
    }
 
    public boolean isSingleton() {
        return true;
    }
 
    public void afterPropertiesSet() throws Exception {
    	Assert.notNull(profile);
    	this.properties = PropertiesManager.smartloadProperties(profile);
    }

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getProfile() {
		return profile;
	}
    
}