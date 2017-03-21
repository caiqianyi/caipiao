package com.ct.soa.core.redis;

import java.util.Map;

public interface IRedisHash{
	/**
	 * 添加一个value到hash中。如果value已经存在于hash中，则返回false。
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param k key值
	 * @param f field值
	 * @param v value值
	 * @return
	 */
	public Boolean hSet(RedisSpace space,String k,String f,Object v);
	
	/**
	 * 添加一个value到hash store中，如果field不存在。
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param k key值
	 * @param f field值
	 * @param v value值
	 * @return
	 */
	public Boolean hSetNx(RedisSpace space,String k,String f,Object v);
	
	/**
	 * 取得hash中的value，如何hash不存在，或者key不存在返回null。
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param k key值
	 * @param f field值
	 * @return
	 */
	public Object hGet(RedisSpace space,String k,String f);
	/**
	 * 取得hash表的长度。
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param key
	 * @return
	 */
	public Long hLen(RedisSpace space,String key);
	
	/**
	 * 删除指定的元素。
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param key
	 * @return
	 */
	public Long hDel(RedisSpace space,String key);
	
	/**
	 * 取得hash表中的keys，以Map<String,Object>形式返回。
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param key
	 * @return
	 */
	public Map<String,Object> hGetAll(RedisSpace space,String key);
	
	/**
	 * 验证hash表中是否存在指定的key-value
	 * @param space
	 *            见枚举类RedisSpace定义
	 * @param k
	 * @param f
	 * @return
	 */
	public Boolean hExists(RedisSpace space,String k,String f);
}
