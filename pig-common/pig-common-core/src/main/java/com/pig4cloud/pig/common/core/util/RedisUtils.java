package com.pig4cloud.pig.common.core.util;

import cn.hutool.core.convert.Convert;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * @author XX
 * @date 2023/05/12
 */
@UtilityClass
public class RedisUtils {

	private static final Long SUCCESS = 1L;

	/**
	 * 指定缓存失效时间
	 * @param key 键
	 * @param time 时间(秒)
	 */
	public boolean expire(String key, long time) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Optional.ofNullable(redisTemplate)
			.filter(template -> time > 0)
			.ifPresent(template -> template.expire(key, time, TimeUnit.SECONDS));
		return true;
	}

	/**
	 * 根据 key 获取过期时间
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpire(Object key) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return Optional.ofNullable(redisTemplate)
			.map(template -> template.getExpire(key, TimeUnit.SECONDS))
			.orElse(-1L);
	}

	/**
	 * 查找匹配key
	 * @param pattern key
	 * @return /
	 */
	public List<String> scan(String pattern) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
		return Optional.ofNullable(redisTemplate).map(template -> {
			RedisConnectionFactory factory = template.getConnectionFactory();
			RedisConnection rc = Objects.requireNonNull(factory).getConnection();
			Cursor<byte[]> cursor = rc.scan(options);
			List<String> result = new ArrayList<>();
			while (cursor.hasNext()) {
				result.add(new String(cursor.next()));
			}
			RedisConnectionUtils.releaseConnection(rc, factory);
			return result;
		}).orElse(Collections.emptyList());
	}

	/**
	 * 分页查询 key
	 * @param patternKey key
	 * @param page 页码
	 * @param size 每页数目
	 * @return /
	 */
	public List<String> findKeysForPage(String patternKey, int page, int size) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
		RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
		RedisConnection rc = Objects.requireNonNull(factory).getConnection();
		Cursor<byte[]> cursor = rc.scan(options);
		List<String> result = new ArrayList<>(size);
		int tmpIndex = 0;
		int fromIndex = page * size;
		int toIndex = page * size + size;
		while (cursor.hasNext()) {
			if (tmpIndex >= fromIndex && tmpIndex < toIndex) {
				result.add(new String(cursor.next()));
				tmpIndex++;
				continue;
			}
			// 获取到满足条件的数据后,就可以退出了
			if (tmpIndex >= toIndex) {
				break;
			}
			tmpIndex++;
			cursor.next();
		}
		RedisConnectionUtils.releaseConnection(rc, factory);
		return result;
	}

	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return Optional.ofNullable(redisTemplate).map(template -> template.hasKey(key)).orElse(false);
	}

	/**
	 * 删除缓存
	 * @param keys 可以传一个值 或多个
	 */
	public void del(String... keys) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		if (keys != null) {
			Arrays.stream(keys).forEach(redisTemplate::delete);
		}
	}

	/**
	 * 获取锁
	 * @param lockKey 锁key
	 * @param value value
	 * @param expireTime：单位-秒
	 * @return boolean
	 */
	public boolean getLock(String lockKey, String value, int expireTime) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return Optional.ofNullable(redisTemplate)
			.map(template -> template.opsForValue().setIfAbsent(lockKey, value, expireTime, TimeUnit.SECONDS))
			.orElse(false);
	}

	/**
	 * 释放锁
	 * @param lockKey 锁key
	 * @param value value
	 * @return boolean
	 */
	public boolean releaseLock(String lockKey, String value) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
		return Optional.ofNullable(redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value))
			.map(Convert::toLong)
			.filter(SUCCESS::equals)
			.isPresent();
	}

	// ============================String=============================

	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	public <T> T get(String key) {
		RedisTemplate<String, T> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 批量获取
	 * @param keys
	 * @return
	 */
	public <T> List<T> multiGet(List<String> keys) {
		RedisTemplate<String, T> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * 普通缓存放入
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String key, Object value) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Optional.ofNullable(redisTemplate).map(template -> {
			template.opsForValue().set(key, value);
			return true;
		});
		return true;
	}

	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key, Object value, long time) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return Optional.ofNullable(redisTemplate).map(template -> {
			if (time > 0) {
				template.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			}
			else {
				template.opsForValue().set(key, value);
			}
			return true;
		}).orElse(false);
	}

	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param time 时间
	 * @param timeUnit 类型
	 * @return true成功 false 失败
	 */
	public <T> boolean set(String key, T value, long time, TimeUnit timeUnit) {
		RedisTemplate<String, T> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Optional.ofNullable(redisTemplate).map(template -> {
			if (time > 0) {
				template.opsForValue().set(key, value, time, timeUnit);
			}
			else {
				template.opsForValue().set(key, value);
			}
			return true;
		});
		return true;
	}

	// ================================Map=================================

	/**
	 * HashGet
	 * @param key 键 不能为null
	 * @param hashKey 项 不能为null
	 * @return 值
	 */
	public <HK, HV> HV hget(String key, HK hashKey) {
		RedisTemplate<String, HV> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.<HK, HV>opsForHash().get(key, hashKey);
	}

	/**
	 * 获取hashKey对应的所有键值
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public <HK, HV> Map<HK, HV> hmget(String key) {
		RedisTemplate<String, HV> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.<HK, HV>opsForHash().entries(key);
	}

	/**
	 * HashSet
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean hmset(String key, Map<String, Object> map) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Optional.ofNullable(redisTemplate).map(template -> {
			template.opsForHash().putAll(key, map);
			return true;
		});
		return true;
	}

	/**
	 * HashSet 并设置时间
	 * @param key 键
	 * @param map 对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public boolean hmset(String key, Map<String, Object> map, long time) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Optional.ofNullable(redisTemplate).map(template -> {
			template.opsForHash().putAll(key, map);
			if (time > 0) {
				template.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		});
		return true;
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, Object value) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return Optional.ofNullable(redisTemplate).map(template -> {
			template.opsForHash().put(key, item, value);
			return true;
		}).orElse(false);
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, Object value, long time) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return Optional.ofNullable(redisTemplate).map(template -> {
			template.opsForHash().put(key, item, value);
			if (time > 0) {
				template.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		}).orElse(false);
	}

	/**
	 * 删除hash表中的值
	 * @param key 键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public void hdel(String key, Object... item) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hHasKey(String key, String item) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * @param key 键
	 * @param item 项
	 * @param by 要增加几(大于0)
	 * @return
	 */
	public double hincr(String key, String item, double by) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 * @param key 键
	 * @param item 项
	 * @param by 要减少记(小于0)
	 * @return
	 */
	public double hdecr(String key, String item, double by) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	// ============================set=============================

	/**
	 * 根据key获取Set中的所有值
	 * @param key 键
	 * @return
	 */
	public <T> Set<T> sGet(String key) {
		RedisTemplate<String, T> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForSet().members(key);
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * @param key 键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key, Object value) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForSet().isMember(key, value);
	}

	/**
	 * 将数据放入set缓存
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSet(String key, Object... values) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForSet().add(key, values);
	}

	/**
	 * 将set数据放入缓存
	 * @param key 键
	 * @param time 时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSetAndTime(String key, long time, Object... values) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Long count = redisTemplate.opsForSet().add(key, values);
		if (time > 0) {
			expire(key, time);
		}
		return count;
	}

	/**
	 * 获取set缓存的长度
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForSet().size(key);
	}

	/**
	 * 移除值为value的
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemove(String key, Object... values) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Long count = redisTemplate.opsForSet().remove(key, values);
		return count;
	}

	/**
	 * 获集合key1和集合key2的差集元素
	 * @param key 键
	 * @return
	 */
	public <T> Set<T> sDifference(String key, String otherKey) {
		RedisTemplate<String, T> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForSet().difference(key, otherKey);
	}

	// ===============================list=================================

	/**
	 * 获取list缓存的内容
	 * @param key 键
	 * @param start 开始
	 * @param end 结束 0 到 -1代表所有值
	 * @return
	 */
	public <T> List<T> lGet(String key, long start, long end) {
		RedisTemplate<String, T> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 获取list缓存的长度
	 * @param key 键
	 * @return
	 */
	public long lGetListSize(String key) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 通过索引 获取list中的值
	 * @param key 键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object lGetIndex(String key, long index) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, Object value) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		redisTemplate.opsForList().rightPush(key, value);
		return true;
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean lSet(String key, Object value, long time) {
		RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		redisTemplate.opsForList().rightPush(key, value);
		if (time > 0) {
			Optional.ofNullable(redisTemplate).ifPresent(template -> template.expire(key, time, TimeUnit.SECONDS));
		}
		return true;
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, List<Object> value) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		redisTemplate.opsForList().rightPushAll(key, value);
		return true;
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<Object> value, long time) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		redisTemplate.opsForList().rightPushAll(key, value);
		if (time > 0) {
			expire(key, time);
		}
		return true;
	}

	/**
	 * 根据索引修改list中的某条数据
	 * @param key 键
	 * @param index 索引
	 * @param value 值
	 * @return /
	 */
	public boolean lUpdateIndex(String key, long index, Object value) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		redisTemplate.opsForList().set(key, index, value);
		return true;
	}

	/**
	 * 移除N个值为value
	 * @param key 键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long lRemove(String key, long count, Object value) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForList().remove(key, count, value);
	}

	/**
	 * 将zSet数据放入缓存
	 * @param key
	 * @param time
	 * @param tuples
	 * @return
	 */
	public long zSetAndTime(String key, long time, Set<ZSetOperations.TypedTuple<Object>> tuples) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		Long count = redisTemplate.opsForZSet().add(key, tuples);
		if (time > 0) {
			expire(key, time);
		}
		return count;

	}

	/**
	 * Sorted set:有序集合获取
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<Object> zRangeByScore(String key, double min, double max) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.rangeByScore(key, min, max);

	}

	/**
	 * Sorted set:有序集合获取 正序
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Object> zRange(String key, long start, long end) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.range(key, start, end);

	}

	/**
	 * Sorted set:有序集合获取 倒叙
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Object> zReverseRange(String key, long start, long end) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.reverseRange(key, start, end);

	}

	/**
	 * 获取zSet缓存的长度
	 * @param key 键
	 * @return
	 */
	public long zGetSetSize(String key) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		return redisTemplate.opsForZSet().size(key);
	}

}
