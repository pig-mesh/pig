package com.pig4cloud.pig.common.security.util;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.io.Serial;

/**
 * OAuth客户端异常类
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class OAuthClientException extends OAuth2AuthenticationException {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 使用指定消息构造OAuthClientException
	 * @param msg 详细消息
	 */
	public OAuthClientException(String msg) {
		super(new OAuth2Error(msg), msg);
	}

	/**
	 * 构造一个带有指定消息和根原因的OAuthClientException
	 * @param msg 详细消息
	 * @param cause 根原因
	 */
	public OAuthClientException(String msg, Throwable cause) {
		super(new OAuth2Error(msg), cause);
	}

}
