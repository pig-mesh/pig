package com.pig4cloud.pigx.test;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.test.annotation.WithMockOAuth2User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author lengleng
 * @date 2020/9/22
 * <p>
 * oauth2 上下文生成处理器
 */
public class WithMockSecurityContextFactory implements WithSecurityContextFactory<WithMockOAuth2User> {

	private final String TOKEN_URL = "http://pigx-auth/oauth/token";

	@Override
	public SecurityContext createSecurityContext(WithMockOAuth2User oAuth2User) {
		// 0. 初始化环境
		TenantContextHolder.setTenantId(oAuth2User.tenant());

		// 1. 请求认证中心获取token
		String token = getToken(oAuth2User);

		// 2. 解析认证中心返回用户
		OAuth2Authentication authentication = getUser(token);

		// 3. 构建 oauth2 上下文
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);

		// 4. 上下文保存 token
		DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(token);
		OAuth2ClientContext clientContext = SpringContextHolder.getBean(OAuth2ClientContext.class);
		clientContext.setAccessToken(accessToken);
		return context;
	}

	/**
	 * 请求认证中心获取token
	 * @param oAuth2User 账号、密码
	 * @return String token
	 */
	private String getToken(final WithMockOAuth2User oAuth2User) {
		OAuth2ProtectedResourceDetails clientProperties = SpringContextHolder
				.getBean(OAuth2ProtectedResourceDetails.class);

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(HttpHeaders.AUTHORIZATION, HttpUtil.buildBasicAuth(clientProperties.getClientId(),
				clientProperties.getClientSecret(), StandardCharsets.UTF_8));

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("username", oAuth2User.username());
		requestBody.add("password", oAuth2User.password());
		requestBody.add("grant_type", "password");
		requestBody.put("scope", clientProperties.getScope());

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

		RestTemplate restTemplate = SpringContextHolder.getBean(RestTemplate.class);

		// 优先获取配置文件URI 配置
		String result;
		if (StrUtil.isBlank(clientProperties.getAccessTokenUri())) {
			result = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class).getBody();
		}
		else {
			result = new RestTemplate()
					.exchange(clientProperties.getAccessTokenUri(), HttpMethod.POST, requestEntity, String.class)
					.getBody();
		}

		return JSONUtil.parseObj(result).getStr("access_token");
	}

	/**
	 * 使用token 获取用户详情
	 * @param token token
	 * @return user详细
	 */
	private OAuth2Authentication getUser(final String token) {
		ResourceServerTokenServices tokenServices = SpringContextHolder.getBean(ResourceServerTokenServices.class);
		return tokenServices.loadAuthentication(token);
	}

}
