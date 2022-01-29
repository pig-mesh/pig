package com.pig4cloud.pig.common.security.grant;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author hzq
 * @since 2021-09-14
 */
public class CustomAppAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	// 验证码/密码
	private String code;

	/**
	 * 授权类型
	 */
	@Getter
	private String grantType;

	public CustomAppAuthenticationToken(String phone, String code, String grantType) {
		super(AuthorityUtils.NO_AUTHORITIES);
		this.principal = phone;
		this.code = code;
		this.grantType = grantType;
	}

	public CustomAppAuthenticationToken(UserDetails sysUser) {
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
