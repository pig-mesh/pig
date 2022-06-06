package com.pig4cloud.pig.common.security.util;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author jumuning
 * @description ScopeException 异常信息
 */
public class ScopeException extends OAuth2AuthenticationException {

	/**
	 * Constructs a <code>ScopeException</code> with the specified message.
	 * @param msg the detail message.
	 */
	public ScopeException(String msg) {
		super(new OAuth2Error(msg), msg);
	}

	/**
	 * Constructs a {@code ScopeException} with the specified message and root cause.
	 * @param msg the detail message.
	 * @param cause root cause
	 */
	public ScopeException(String msg, Throwable cause) {
		super(new OAuth2Error(msg), cause);
	}

}
