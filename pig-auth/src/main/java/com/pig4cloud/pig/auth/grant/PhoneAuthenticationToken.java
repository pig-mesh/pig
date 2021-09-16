package com.pig4cloud.pig.auth.grant;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author hzq
 * @since 2021-09-14
 */
public class PhoneAuthenticationToken extends AbstractAuthenticationToken {

	private Object principal;

	// 验证码/密码
	private String code;

	public PhoneAuthenticationToken(String phone, String code) {
		super(AuthorityUtils.NO_AUTHORITIES);
		this.principal = phone;
		this.code = code;
	}

	public PhoneAuthenticationToken(UserDetails sysUser) {
		super(sysUser.getAuthorities());
		this.principal = sysUser;
		super.setAuthenticated(true); // 设置认证成功 必须
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getCredentials() {
		return this.code;
	}

}
