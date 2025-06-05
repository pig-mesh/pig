package com.pig4cloud.pig.admin.api.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.feign.RemoteParamService;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import lombok.experimental.UtilityClass;

/**
 * 系统参数配置解析器工具类
 *
 * @author lengleng
 * @date 2025/05/30
 */
@UtilityClass
public class ParamResolver {

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

	/**
	 * 根据key获取远程参数值并转换为指定类型
	 * @param key 参数key
	 * @param clazz 目标类型
	 * @param defaultVal 默认值(可选，最多一个)
	 * @param <T> 泛型类型
	 * @return 转换后的参数值，未找到且无默认值时返回null
	 * @throws IllegalArgumentException 参数不合法时抛出异常
	 */
	private <T> T checkAndGet(String key, Class<T> clazz, T... defaultVal) {
		// 校验入参是否合法
		if (StrUtil.isBlank(key) || defaultVal.length > 1) {
			throw new IllegalArgumentException("参数不合法");
		}

		RemoteParamService remoteParamService = SpringContextHolder.getBean(RemoteParamService.class);

		String result = remoteParamService.getByKey(key).getData();

		if (StrUtil.isNotBlank(result)) {
			return Convert.convert(clazz, result);
		}

		if (defaultVal.length == 1) {
			return Convert.convert(clazz, defaultVal.clone()[0]);

		}
		return null;
	}

}
