package com.ct.soa.core.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;


/**
 * 需要Redis 2.8 才能支持开启redis http session
 * @author user
 *
 */
@Configuration  
@EnableRedisHttpSession
public class RedisSessionConfig {
	
	private Logger logger = LoggerFactory.getLogger(RedisSessionConfig.class);
	
	@Value("${http.cookie.domain:NULL}")
	private String domainName;
	
	@Bean  
    public CookieHttpSessionStrategy cookieHttpSessionStrategy() {  
        CookieHttpSessionStrategy strategy = new CookieHttpSessionStrategy();
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookiePath("/");//解决项目名，取不到cookie。session不共享问题
        logger.info("http.cookie.domain vlaue:{}",domainName);
        cookieSerializer.setDomainName("NULL".equals(domainName)?null:domainName);
        cookieSerializer.setCookieName("userkey");
        strategy.setCookieSerializer(cookieSerializer);  
        return strategy;  
    }  
}
