package com.pig4cloud.pig.common.feign.core;

import com.pig4cloud.pig.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author lengleng
 * @date 2025/3/26
 * <p>
 * feign 语言环境透传拦截器
 */
public class PigFeignLanguageInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		Optional<HttpServletRequest> requestOptional = WebUtils.getRequest();
		if (requestOptional.isEmpty()) {
			return;
		}

		String language = requestOptional.get().getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if (StringUtils.hasText(language)) {
			template.header(HttpHeaders.ACCEPT_LANGUAGE, language);
		}
	}

}
