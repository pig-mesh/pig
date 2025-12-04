package com.pig4cloud.pig.common.security.component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.StreamUtils;

import com.pig4cloud.pig.common.core.util.SpringContextHolder;

import tools.jackson.databind.ObjectMapper;

/**
 * 扩展OAuth2AccessTokenResponseHttpMessageConverter，支持Long类型转String的Token响应转换
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class PigCustomOAuth2AccessTokenResponseHttpMessageConverter
		extends OAuth2AccessTokenResponseHttpMessageConverter {

	/**
	 * 字符串到对象的映射类型引用
	 */
	// private static final ParameterizedTypeReference<Map<String, Object>>
	// STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {};

	/**
	 * OAuth2访问令牌响应参数转换器，用于将OAuth2AccessTokenResponse转换为Map<String, Object>
	 */
	private Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter = new DefaultOAuth2AccessTokenResponseMapConverter();

	/**
	 * 将OAuth2访问令牌响应写入HTTP输出消息
	 * @param tokenResponse OAuth2访问令牌响应
	 * @param outputMessage HTTP输出消息
	 * @throws HttpMessageNotWritableException 写入响应时发生错误抛出异常
	 */
	protected void writeInternal(OAuth2AccessTokenResponse tokenResponse, HttpOutputMessage outputMessage)
			throws HttpMessageNotWritableException {
		try {
			Map<String, Object> tokenResponseParameters = this.accessTokenResponseParametersConverter
				.convert(tokenResponse);

			ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);

			// 直接将 Map 转换为 JSON 字符串
			String jsonResponse = objectMapper.writeValueAsString(tokenResponseParameters);

			// 设置响应头
			outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			outputMessage.getHeaders().setContentLength(jsonResponse.getBytes(StandardCharsets.UTF_8).length);

			// 写入响应体
			StreamUtils.copy(jsonResponse, StandardCharsets.UTF_8, outputMessage.getBody());
		}
		catch (Exception ex) {
			throw new HttpMessageNotWritableException(
					"An error occurred writing the OAuth 2.0 Access Token Response: " + ex.getMessage(), ex);
		}
	}

}
