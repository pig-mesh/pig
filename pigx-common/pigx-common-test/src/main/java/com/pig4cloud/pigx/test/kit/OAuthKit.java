package com.pig4cloud.pigx.test.kit;

import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * @author lengleng
 * @date 2020/9/22
 * <p>
 * Mocke 工具类
 */
public class OAuthKit {

	/**
	 * mock 请求增加统一请求头
	 * @return
	 */
	public static RequestPostProcessor token() {
		return mockRequest -> {
			OAuth2ClientContext clientContext = SpringContextHolder.getBean(OAuth2ClientContext.class);
			String token = clientContext.getAccessToken().getValue();
			mockRequest.addHeader(HttpHeaders.AUTHORIZATION,
					String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, token));
			return mockRequest;
		};
	}

}
