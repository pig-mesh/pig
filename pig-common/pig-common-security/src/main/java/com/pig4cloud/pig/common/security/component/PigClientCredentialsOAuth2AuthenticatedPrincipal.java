package com.pig4cloud.pig.common.security.component;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.Map;

/**
 * 客户端模式凭证认证主体实现类
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RequiredArgsConstructor
public class PigClientCredentialsOAuth2AuthenticatedPrincipal implements OAuth2AuthenticatedPrincipal {

	private final Map<String, Object> attributes;

	private final Collection<GrantedAuthority> authorities;

	private final String name;

	/**
	 * 获取属性集合
	 * @return 属性键值对集合
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	/**
	 * 获取用户权限集合
	 * @return 用户权限集合
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
	 * 获取名称
	 * @return 当前对象的名称
	 */
	@Override
	public String getName() {
		return this.name;
	}

}
