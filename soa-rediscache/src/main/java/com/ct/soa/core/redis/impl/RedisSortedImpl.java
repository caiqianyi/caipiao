package com.ct.soa.core.redis.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Repository;

import com.ct.commons.utils.SerializeUtil;
import com.ct.soa.core.redis.IRedisSorted;
import com.ct.soa.core.redis.RedisSpace;

/**
 * IRedisSortedSet实现
 * 
 */
@Repository("redisSorted")
public class RedisSortedImpl extends RedisCacheManager implements IRedisSorted {
	
	public final static String NAMESPACE= "redis.sorted.set";
	
	private String getZSetGlobalKey(RedisSpace space,String key){
		return checkKey(space.getValue()+"."+NAMESPACE+"."+ key);
	}
	
	@Override
	public String zAdd(RedisSpace space,String key, final double score, final Object value) {
		// TODO Auto-generated method stub
		final String newk = getZSetGlobalKey(space,key);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				byte[] v = SerializeUtil.serialize(value);
				return con.zAdd(k, score, v);
			}
		});
		if(result)
			return newk;
		return null;
	}
	@Override
	public List<Object> zRangeByScore(RedisSpace space,String key,final double min, final double max) {
		// TODO Auto-generated method stub
		final String newk = getZSetGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<List<Object>>() {
			public List<Object> doInRedis(RedisConnection con) throws DataAccessException {
				List<Object> list = new ArrayList<Object>();
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				Set<byte[]> set = con.zRangeByScore(k, min, max);
				if(set != null && !set.isEmpty()){
					for(byte[] rval : set){
						list.add(SerializeUtil.unserialize(rval));
					}
				}
				return list;
			}
		});
	}
	
	@Override
	public Long zCount(RedisSpace space,String key, final double min, final double max) {
		final String newk = getZSetGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				return con.zCount(k, min, max);
			}
		});
	}
	
	@Override
	public Long zDel(RedisSpace space,String key,final Object val) {
		final String newk = getZSetGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				byte[] vbs = SerializeUtil.serialize(val);
				return con.zRem(k, vbs);
			}
		});
	}
	@Override
	public Long zSize(RedisSpace space,String key) {
		final String newk = getZSetGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				return con.zCard(k);//返回在指定的键存储在集合中的元素的数量。
			}
		});
	}
	
	@Override
	public Double zScore(RedisSpace space,String key, final Object value) {
		final String newk = getZSetGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<Double>() {
			public Double doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				byte[] vbs = SerializeUtil.serialize(value);
				return con.zScore(k, vbs);//返回在指定的键存储在集合中的元素的数量。
			}
		});
	}
}
