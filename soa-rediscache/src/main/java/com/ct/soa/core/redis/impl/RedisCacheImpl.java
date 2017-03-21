package com.ct.soa.core.redis.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Repository;

import com.ct.commons.utils.SerializeUtil;
import com.ct.soa.core.redis.IRedisCache;
import com.ct.soa.core.redis.RedisSpace;

/**
 * IRedisCache实现
 * 
 */
@Repository("redisCache")
public class RedisCacheImpl extends RedisCacheManager implements IRedisCache {
	
	private Logger logger = LoggerFactory.getLogger(RedisCacheImpl.class);
	
	/**
	 * 
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param key
	 *            key值
	 * @return
	 */
	private String getGlobalKey(RedisSpace space, String key) {
		String k = space.getValue() + "." + key;
		return checkKey(k);
	}
	
	@Override
	public String getGlobalUnsetKey(RedisSpace space, String key) {
		return space.getValue()+".unset."+ key;
	}
	
	@Override
	public boolean set(RedisSpace space, String key, final Object obj) {
		return set(space, key, obj, null);
	}

	@Override
	public boolean setUnset(RedisSpace space,String key1, String key2, final Object obj, final Long expire) {
		String k = checkKey(getGlobalUnsetKey(space, key1));
		final String ks = k +"."+ key2;
		
		logger.debug("redis unset global set key:"+ks);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(ks);
				byte[] v = SerializeUtil.serialize(obj);

				con.set(k, v);

				if (expire != null && expire > 0) {
					con.expire(k, expire);
				}

				return true;
			}
		}, true);
	}

	@Override
	public boolean setUnset(RedisSpace space,final String key, final Object obj, final Long expire) {
		final String ks = getGlobalUnsetKey(space, key);
		logger.debug("redis unset global set key:"+ks);
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(ks);
				byte[] v = SerializeUtil.serialize(obj);

				con.set(k, v);

				if (expire != null && expire > 0) {
					con.expire(k, expire);
				}

				return true;
			}
		}, true);
	}

	@Override
	public boolean set(RedisSpace space, String key, final Object obj, final Long expire) {
		final String ks = getGlobalKey(space, key);

		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(ks);
				byte[] v = SerializeUtil.serialize(obj);

				con.set(k, v);

				if (expire != null && expire > 0) {
					con.expire(k, expire);
				}

				return true;
			}
		}, true);
	}

	@Override
	public boolean exists(RedisSpace space, String key) {
		return redisTemplate.hasKey(getGlobalKey(space, key));
	}

	@Override
	public void del(RedisSpace space, String... keys) {
		if (keys != null && keys.length > 0) {
			if (keys.length == 1)
				redisTemplate.delete(getGlobalKey(space, keys[0]));
			else
				for (String key : keys) {
					del(space, key);
				}
		}
	}

	@Override
	public boolean update(RedisSpace space, String key, Object obj) {
		return update(space, key, obj, null);
	}

	@Override
	public boolean update(RedisSpace space, String key, final Object obj, final Long expire) {
		final String ks = getGlobalKey(space, key);

		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(ks);
				long t = expire != null && expire > 0 ? expire : con.ttl(k);

				if (obj != null) {
					byte[] v = SerializeUtil.serialize(obj);
					con.set(k, v);
				}

				if (t > 0) {
					con.expire(k, t);
				}

				return true;
			}
		}, true);
	}

	@Override
	public Object get(RedisSpace space, final String key) {
		final String ks = getGlobalKey(space, key);

		return redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(ks);

				if (con.exists(k)) {
					byte[] bytes = con.get(k);
					return SerializeUtil.unserialize(bytes);
				}

				return null;
			}
		});
	}

	@Override
	public Long ttl(RedisSpace space, String key) {
		final String ks = getGlobalKey(space, key);

		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection con) throws DataAccessException {
				byte[] k = redisTemplate.getStringSerializer().serialize(ks);
				return con.ttl(k);
			}
		}, true);
	}

	@Override
	public Set<String> searchKey(final RedisSpace space, final String key) {
		 
		final String ks = space == null ? key : getGlobalKey(space, key) ;
		final Set<String> result = new HashSet<String>();

		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection con) throws DataAccessException {
				Set<byte[]> sets = con.keys(ks.getBytes());
				for (byte[] k : sets) {
					result.add(new String(k));
				}
				return null;
			}
		});

		return result;
	}

	@Override
	public void clear(RedisSpace space, String key) {
		Set<String> keys = searchKey(space, key);

		if (keys != null) {
			for (String k : keys) {
				redisTemplate.delete(k);
			}
		}
	}

	@Override
	public int size(RedisSpace space, String key) {
		Set<String> keys = searchKey(space, key);

		if (keys != null) {
			return keys.size();
		}

		return 0;
	}
	
}
