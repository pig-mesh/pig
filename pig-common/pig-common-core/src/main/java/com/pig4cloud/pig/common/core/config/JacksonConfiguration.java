package com.pig4cloud.pig.common.core.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pig4cloud.pig.common.core.jackson.PigJavaTimeModule;
import com.pig4cloud.pig.common.core.jackson.PigLongModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TimeZone;

/**
 * JacksonConfig 配置时间转换规则
 * {@link com.pig4cloud.pig.common.core.jackson.PigJavaTimeModule}、默认时区等
 *
 * @author L.cm
 * @author lengleng
 * @author lishangbu
 * @date 2020-06-15
 */
@Slf4j
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@SuppressWarnings("removal")
public class JacksonConfiguration implements WebMvcConfigurer {

	private static final String ASIA_SHANGHAI = "Asia/Shanghai";

	private final ObjectProvider<ObjectMapper> objectMapperProvider;

	public JacksonConfiguration(ObjectProvider<ObjectMapper> objectMapperProvider) {
		this.objectMapperProvider = objectMapperProvider;
	}

	/**
	 *
	 * 贡献 Jackson 2 的全局配置到 Spring 的 Jackson2ObjectMapperBuilder。
	 * <p>
	 * 必须使用
	 * {@link org.springframework.boot.jackson2.autoconfigure.Jackson2ObjectMapperBuilderCustomizer}
	 * 而不是直接定义 {@link ObjectMapper}： 直接 new ObjectMapper 会绕过 builder pipeline，导致其它模块通过
	 * customizer 注册的能力 （例如 pig-common-xss 的 String 反序列化 XSS 清洗）静默失效。
	 */
	@Bean
	public org.springframework.boot.jackson2.autoconfigure.Jackson2ObjectMapperBuilderCustomizer pigJackson2Customizer() {
		// 序列化能力按职责拆分到独立 module：
		// PigJavaTimeModule —— Java 8 时间类型格式化
		// PigLongModule —— Long/long -> String，防 JS 精度丢失
		return builder -> builder.locale(Locale.CHINA)
			.timeZone(TimeZone.getTimeZone(ASIA_SHANGHAI))
			.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)
			.modulesToInstall(new PigJavaTimeModule(), new PigLongModule())
			.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	/**
	 * 增加GET请求参数中时间类型转换 {@link com.pig4cloud.pig.common.core.jackson.PigJavaTimeModule}
	 * <ul>
	 * <li>HH:mm:ss -> LocalTime</li>
	 * <li>yyyy-MM-dd -> LocalDate</li>
	 * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
	 * </ul>
	 * @param registry
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
		registrar.setDateFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
		registrar.registerFormatters(registry);
	}

	/**
	 * 避免form 提交 context-type 不规范中文乱码
	 * @return Filter
	 */
	@Bean
	public OrderedCharacterEncodingFilter characterEncodingFilter() {
		OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(StandardCharsets.UTF_8.name());
		filter.setForceEncoding(true);
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filter;
	}

	/**
	 * 用 Jackson 2 的
	 * {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter}
	 * 替换 Spring Boot 4 默认注册的 Jackson 3 {@link JacksonJsonHttpMessageConverter}，确保 Spring
	 * MVC 走 Jackson 2 链路。
	 * <p>
	 * 注入的 {@link ObjectMapper} 已被所有
	 * {@link org.springframework.boot.jackson2.autoconfigure.Jackson2ObjectMapperBuilderCustomizer}
	 * （包括 pig-common-xss 的 {@code xssJacksonCustomizer}）处理过，因此本 converter 同时具备 XSS
	 * 反序列化清洗、时间格式、Long 字符串化等能力。
	 * <p>
	 * 替换策略：用 {@link ListIterator#set} 原地替换 Jackson 3 converter；找不到时把 Jackson 2 converter
	 * 插到队首作为 fallback。不做 clear，以免破坏 byte[]、String、multipart 等其它默认 converter。
	 * @param converters 消息转换器列表
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		ObjectMapper objectMapper = objectMapperProvider.getIfAvailable();
		if (objectMapper == null) {
			log.warn("ObjectMapper 不可用，跳过 Spring MVC Jackson 2 替换；保留 Spring Boot 默认 converter 链。");
			return;
		}

		org.springframework.http.converter.json.MappingJackson2HttpMessageConverter jackson2Converter = new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(
				objectMapper);
		ListIterator<HttpMessageConverter<?>> it = converters.listIterator();
		boolean replaced = false;
		while (it.hasNext()) {
			if (it.next() instanceof JacksonJsonHttpMessageConverter) {
				it.set(jackson2Converter);
				replaced = true;
			}
		}
		if (!replaced) {
			converters.add(0, jackson2Converter);
		}
		log.info("已配置 Spring MVC 使用 Jackson 2 HttpMessageConverter，version={}",
				jackson2Converter.getObjectMapper().version());
	}

}
