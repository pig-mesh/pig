package com.pig4cloud.pig.common.data.resolver;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
<<<<<<< HEAD:pig-common/pig-common-data/src/main/java/com/pig4cloud/pig/common/data/resolver/ParamResolver.java
import com.pig4cloud.pig.admin.api.feign.RemoteParamService;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
=======
import com.pig4cloud.pigx.admin.api.feign.RemoteParamService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
>>>>>>> 2a156df33 (feat(upms): 网站配置项抽取到后台管理，支持站点配置聚合与缓存):pigx-common/pigx-common-data/src/main/java/com/pig4cloud/pigx/common/data/resolver/ParamResolver.java
import lombok.experimental.UtilityClass;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2020/5/12
 * <p>
 * 系统参数配置解析器
 */
@UtilityClass
public class ParamResolver {

	/**
	 * 根据多个key 查询value 配置 结果使用hutool 的maputil 进行包装处理 MapUtil.getBool(result,key)
	 * @param keys key
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getMap(String... keys) {
		// 校验入参是否合法
		if (keys == null) {
			throw new IllegalArgumentException("参数不合法");
		}

		Cache cache = paramsCache();

		Map<String, Object> result = new LinkedHashMap<>();
		List<String> missedKeys = new ArrayList<>();

		for (String k : keys) {
			Cache.ValueWrapper wrapper = (cache != null) ? cache.get(k) : null;
			if (wrapper != null) {
				result.put(k, wrapper.get());
			}
			else {
				missedKeys.add(k);
			}
		}

		if (!missedKeys.isEmpty()) {
			RemoteParamService remoteParamService = SpringContextHolder.getBean(RemoteParamService.class);
			Map<String, Object> fetched = remoteParamService.getByKeys(missedKeys.toArray(new String[0])).getData();
			if (fetched != null) {
				result.putAll(fetched);
				if (cache != null) {
					fetched.forEach(cache::put);
				}
			}
		}

		return result;
	}

	/**
	 * 根据key 查询value 配置
	 * @param key key
	 * @param defaultVal 默认值
	 * @return value
	 */
	public Long getLong(String key, Long... defaultVal) {
		return checkAndGet(key, Long.class, defaultVal);
	}

	/**
	 * 根据key 查询value 配置
	 * @param key key
	 * @param defaultVal 默认值
	 * @return value
	 */
	public String getStr(String key, String... defaultVal) {
		return checkAndGet(key, String.class, defaultVal);
	}

	private <T> T checkAndGet(String key, Class<T> clazz, T... defaultVal) {
		// 校验入参是否合法
		if (StrUtil.isBlank(key) || defaultVal.length > 1) {
			throw new IllegalArgumentException("参数不合法");
		}

		// 先查缓存
		Cache cache = paramsCache();
		if (cache != null) {
			String cached = cache.get(key, String.class);
			if (StrUtil.isNotBlank(cached)) {
				return Convert.convert(clazz, cached);
			}
		}

		// 缓存未命中，走 Feign
		RemoteParamService remoteParamService = SpringContextHolder.getBean(RemoteParamService.class);
		String result = remoteParamService.getByKey(key).getData();

		// 非空结果写入缓存
		if (cache != null && StrUtil.isNotBlank(result)) {
			cache.put(key, result);
		}

		if (StrUtil.isNotBlank(result)) {
			return Convert.convert(clazz, result);
		}
		if (defaultVal.length == 1) {
			return Convert.convert(clazz, defaultVal[0]);
		}
		return null;
	}

	private Cache paramsCache() {
		return SpringContextHolder.getBean(CacheManager.class).getCache(CacheConstants.PARAMS_DETAILS);
	}

}
