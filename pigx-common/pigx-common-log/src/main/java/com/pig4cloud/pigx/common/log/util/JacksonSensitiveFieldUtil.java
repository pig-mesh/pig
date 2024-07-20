package com.pig4cloud.pigx.common.log.util;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * Jackson 脱敏工具类
 *
 * @author lengleng
 * @date 2024/07/18
 */
public class JacksonSensitiveFieldUtil {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
    }

    public static void configureSensitiveFields(String[] ignorableFieldNames) {
        FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
                SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
        objectMapper.setFilterProvider(filters);
    }

    public static void registerCustomModule(com.fasterxml.jackson.databind.Module module) {
        objectMapper.registerModule(module);
    }

    /**
     * string -> object
     *
     * @param content   内容
     * @param valueType 值类型
     * @return {@link T }
     */
    @SneakyThrows
    public static <T> T readValue(String content, Class<T> valueType) {
        return objectMapper.readValue(content, valueType);
    }

    /**
     * string -> string
     *
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
     *
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
