package com.pig4cloud.pig.common.log.util;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * Jackson 脱敏工具类
 * <p>
 * 状态字段 {@code objectMapper} 通过 read-copy-write 模式更新，{@link #configureSensitiveFields} 与
 * {@link #registerCustomModule} 都需要在并发调用下保证修改不丢失，因此设为 {@code synchronized}。
 * 读取路径（{@link #readValue} 等）依赖 {@code volatile} 保证可见性，无需加锁。
 * <p>
 * 注：{@link ObjectMapper#copy()} 是 Jackson 2 才支持的能力，这里有意保留 Jackson 2 依赖。
 *
 * @author lengleng
 * @date 2024/07/18
 */
public class JacksonSensitiveFieldUtil {

	@Getter
	private static volatile ObjectMapper objectMapper = JsonMapper.builder()
		.addMixIn(Object.class, PropertyFilterMixIn.class)
		.build();

	public static synchronized void configureSensitiveFields(String[] ignorableFieldNames) {
		FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
				SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
		ObjectMapper mapper = objectMapper.copy();
		mapper.setFilterProvider(filters);
		objectMapper = mapper;
	}

	public static synchronized void registerCustomModule(Module module) {
		ObjectMapper mapper = objectMapper.copy();
		mapper.registerModule(module);
		objectMapper = mapper;
	}

	/**
	 * string -> object
	 * @param content 内容
	 * @param valueType 值类型
	 * @return {@link T }
	 */
	@SneakyThrows
	public static <T> T readValue(String content, Class<T> valueType) {
		return objectMapper.readValue(content, valueType);
	}

	/**
	 * string -> string
	 * @param content 内容
	 * @return {@link String }
	 */
	@SneakyThrows
	public static String readStr(String content) {
		Object o = objectMapper.readValue(content, Object.class);
		return objectMapper.writeValueAsString(o);
	}

	/**
	 * 将值写入字符串
	 * @param value 价值
	 * @return {@link String }
	 */
	@SneakyThrows
	public static String writeValueAsString(Object value) {
		return objectMapper.writeValueAsString(value);
	}

	@JsonFilter("filter properties by name")
	private static class PropertyFilterMixIn {

	}

}
