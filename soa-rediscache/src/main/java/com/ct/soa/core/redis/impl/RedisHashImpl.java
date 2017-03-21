package com.ct.soa.core.redis.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Repository;

import com.ct.commons.utils.SerializeUtil;
import com.ct.soa.core.redis.IRedisHash;
import com.ct.soa.core.redis.RedisSpace;

@Repository("redisHash")
public class RedisHashImpl extends RedisCacheManager implements
		IRedisHash {
	
	public final static String NAMESPACE = "redis.hash.set";
	
	private String getGlobalKey(RedisSpace space,String key){
		return checkKey(space.getValue()+"."+NAMESPACE+"."+ key);
	}

	@Override
	public Boolean hSet(RedisSpace space, String k, final String f, final Object v) {
		// TODO Auto-generated method stub
		final String newk = getGlobalKey(space,k);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(newk);
				byte[] value = SerializeUtil.serialize(v);
				byte[] field = redisTemplate.getStringSerializer().serialize(f);
				return con.hSet(key, field, value);
			}
		});
	}

	@Override
	public Boolean hSetNx(RedisSpace space, String k, final String f, final Object v) {
		final String newk = getGlobalKey(space,k);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(newk);
				byte[] value = SerializeUtil.serialize(v);
				byte[] field = redisTemplate.getStringSerializer().serialize(f);
				return con.hSetNX(key, field, value);
			}
		});
	}

	@Override
	public Object hGet(RedisSpace space, String k, final String f) {
		final String newk = getGlobalKey(space,k);
		return redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection con) throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(newk);
				byte[] field = redisTemplate.getStringSerializer().serialize(f);
				return con.hGet(key, field);
			}
		});
	}

	@Override
	public Long hLen(RedisSpace space, String key) {
		final String newk = getGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection con) throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(newk);
				return con.hLen(key);
			}
		});
	}

	@Override
	public Long hDel(RedisSpace space, String key) {
		final String newk = getGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				return con.hDel(k);
			}
		});
	}

	@Override
	public Map<String, Object> hGetAll(RedisSpace space, String key) {
		final String newk = getGlobalKey(space,key);
		return redisTemplate.execute(new RedisCallback<Map<String, Object>>() {
			public Map<String, Object> doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(newk);
				Map<String, Object> result = new HashMap<String,Object>();
				
				Map<byte[],byte[]> map = con.hGetAll(k);
				for(byte[] rk : map.keySet()){
					result.put(redisTemplate.getStringSerializer().deserialize(rk), SerializeUtil.unserialize(map.get(rk)));
				}
				return result;
			}
		});
	}

	@Override
	public Boolean hExists(RedisSpace space, String k, final String f) {
		final String newk = getGlobalKey(space,k);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(newk);
				byte[] field = redisTemplate.getStringSerializer().serialize(f);
				return con.hExists(key,field);
			}
		});
	}

}
