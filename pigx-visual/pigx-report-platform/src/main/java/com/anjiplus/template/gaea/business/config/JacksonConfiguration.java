package com.anjiplus.template.gaea.business.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author lengleng
 * @date 2022/2/13
 */
@Configuration
public class JacksonConfiguration {

	private static final String ASIA_SHANGHAI = "Asia/Shanghai";

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
			builder.locale(Locale.CHINA);
			builder.timeZone(TimeZone.getTimeZone(ASIA_SHANGHAI));
			builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
			builder.serializerByType(Long.class, ToStringSerializer.instance);
		};
	}

}
