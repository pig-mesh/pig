package com.pig4cloud.pig.common.security.feign;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Collection;

/**
 * oauth2 feign token传递
 *
 * 重新 OAuth2FeignRequestInterceptor ，官方实现部分常见不适用
 *
 * @author lengleng
 * @date 2022/5/29
 */
@Slf4j
@RequiredArgsConstructor
public class PigOAuthRequestInterceptor implements RequestInterceptor {

	/**
	 * Create a template with the header of provided name and extracted extract </br>
	 *
	 * 1. 如果使用 非web 请求，header 区别 </br>
	 *
	 * 2. 根据authentication 还原请求token
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {
		Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
		// 带from 请求直接跳过
		if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
			return;
		}

		// 非web 请求直接跳过
		if (!WebUtils.getRequest().isPresent()) {
			return;
		}
		HttpServletRequest request = WebUtils.getRequest().get();
		// 避免请求参数的 query token 无法传递

		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		template.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokenInfo.getTokenValue()));

	}

}
