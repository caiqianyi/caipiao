package com.ct.soa.core.redis.impl;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.ct.common.config.PropertiesManager;
import com.ct.commons.exception.I18nMessageException;
import com.ct.commons.exception.MsgModule;

public class RedisCacheManager {
	
	@Resource
	RedisTemplate<String, Object> redisTemplate;
	
	@Resource
	private PropertiesManager propertiesManager;

	
	String checkKey(String k){
		String value = propertiesManager.getProperty("/redis-keys.properties", k);
		if (value != null) {
			return k;
		}
		throw new I18nMessageException(MsgModule.GLOBAL, 500, "‘" + k + "’在redis-keys.properties注册表中找不到！");
	}
	
}
