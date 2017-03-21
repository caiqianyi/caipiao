package com.ct.soa.core.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


/**
 * 需要Redis 2.8 才能支持开启redis http session
 * @author user
 *
 */
@Configuration  
@EnableRedisHttpSession
public class RedisSessionConfig {

}
