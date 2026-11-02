package com.pig4cloud.pig.auth.support.sms;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 短信授权流程内部使用的认证令牌，与表单用户名密码认证区分。 验证码仍由短信授权入口的 ValidateCodeFilter 校验。
 */
public final class SmsAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public SmsAuthenticationToken(String phone) {
		super(phone, null);
	}

}
