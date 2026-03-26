package com.pig4cloud.pig.common.feign.core;

import com.pig4cloud.pig.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * @author lengleng
 * @date 2025/3/26
 * <p>
 * feign 语言环境透传拦截器
 */
public class PigFeignLanguageInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		HttpServletRequest request = WebUtils.getRequest();
		if (request == null) {
			return;
		}

		String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if (StringUtils.hasText(language)) {
			template.header(HttpHeaders.ACCEPT_LANGUAGE, language);
		}
	}

}
