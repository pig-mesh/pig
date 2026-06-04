package com.pig4cloud.pig.common.security.captcha;

import com.anji.captcha.service.CaptchaService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 验证码自动配置。仅在业务服务显式引入验证码依赖后注册验证码校验器。
 *
 * @author lengleng
 * @date 2026-05-20
 */
@AutoConfigureAfter(name = "com.pig4cloud.pig.common.data.cache.RedisTemplateConfiguration")
@ConditionalOnClass(name = { "com.anji.captcha.service.CaptchaService", "com.anji.captcha.model.vo.CaptchaVO" })
@ConditionalOnBean(RedisTemplate.class)
@Configuration(proxyBeanMethods = false)
public class CaptchaAutoConfiguration {

	/**
	 * 验证码服务配置。仅在 anji-captcha 类型存在后再解析相关方法签名。
	 */
	@Configuration(proxyBeanMethods = false)
	static class CaptchaServiceConfiguration {

		/**
		 * 注册验证码统一校验器。
		 * @param captchaService anji-captcha 校验服务
		 * @param redisTemplate Redis 缓存操作模板
		 * @return 验证码统一校验器
		 */
		@Bean
		@ConditionalOnMissingBean
		CaptchaValidator captchaValidator(CaptchaService captchaService, RedisTemplate<String, Object> redisTemplate) {
			return new CaptchaValidator(captchaService, redisTemplate);
		}

	}

}
