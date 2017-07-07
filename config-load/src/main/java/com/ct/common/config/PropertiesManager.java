package com.ct.common.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ct.common.bind.PropertySourceUtils;

/**
 * 读取配置文件管理工具
 * 2017-05-02
 * @author caiqianyi
 *
 */
@Component
public class PropertiesManager {
	
	private Logger logger = LoggerFactory.getLogger(PropertiesManager.class);
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	private static Map<String,Properties> map = new HashMap<String,Properties>();
	private static Date lastLoadDate = null;
	
	
	
	public final static String CONFIG_RELOAD_KEY = "com.xlhy.sys.config.isReload";
	
	public static synchronized Properties smartloadProperties(String profile){
		Properties properties = null;
		boolean isRemote = EnvLoader.isRemote();
		if(map.get(profile) == null){
			if(isRemote)
				properties = RemoteProperties.loadProperties(profile);
			else
				properties = LocalProperties.loadProperties(profile);
			map.put(profile, properties);
		}
		return map.get(profile);
	}
	
	private Properties loadProperties(String profile){//
		Date loadDate = redisTemplate.execute(new RedisCallback<Date>() {
			public Date doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(CONFIG_RELOAD_KEY);
				if (con.exists(k)) {
					byte[] bytes = con.get(k);
					return (Date) unserialize(bytes);
				}
				return null;
			}
		}, true);//缓存开关控制是否重新加载配置文件
		
		if(lastLoadDate == null || (loadDate != null && lastLoadDate.before(loadDate))){//更新配置
			logger.info("===>>[reload properties] lastLoadDate:{} loadDate:{}",lastLoadDate,loadDate);
			map.clear();
			lastLoadDate = loadDate == null ? new Date() : loadDate;
		}
		
		return smartloadProperties(profile);
	}
	
    public String getProperty(String propFile,String param){
    	Properties prop = loadProperties(propFile);
		String jsonStr=prop.getProperty(param); 
		if(StringUtils.isEmpty(jsonStr)){
			return null;
		}
		return  jsonStr.trim();
	}
	
     
     /**
      * 根据value中的某一列，获取key
      * @param filePath
      * @return
      * @throws IOException
      */
     public String getKeyByValueColumn(String filePath,String columnValue)throws IOException {
    	 Properties pps = loadProperties(filePath);
         return PropertySourceUtils.getKeyByValueColumn(pps, columnValue);
         
     }
     // 获取游戏信息
  	public String getGameInfo(String propFile) {
  		Properties prop = loadProperties(propFile);
  		return prop.toString();
  	}
  	
  	private static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
