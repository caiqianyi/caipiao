package com.ct.soa.core.redis.impl;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.ct.commons.beans.PropertyConfigurer;
import com.ct.soa.core.exception.I18nMessageException;
import com.ct.soa.core.exception.MsgModule;

public class RedisCacheManager {
	
	@Resource
	RedisTemplate<String, Object> redisTemplate;
	
	@Resource
	PropertyConfigurer PropertyConfigurer;

	Properties prop = new Properties();
	
	String checkKey(String k){
		if (prop.containsKey(k)) {
			return k;
		}
		throw new I18nMessageException(MsgModule.GLOBAL, 500, "‘" + k + "’在redis-keys.properties注册表中找不到！");
	}
	
	@Resource
	public void setPropertyConfigurer(PropertyConfigurer propertyConfigurer) {
		PropertyConfigurer = propertyConfigurer;
		prop = PropertyConfigurer.lazyLoadUniqueProperties("classpath*:redis-keys.properties");
	}
}
