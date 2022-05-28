package com.pig4cloud.pig.common.security.component;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

/**
 * @author lengleng
 * @date 2022/5/28
 */
@RequiredArgsConstructor
public class CustomjwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final OAuth2AuthorizationService authorizationService;

	@Override
	public final AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
		String principalClaimValue = jwt.getClaimAsString(JwtClaimNames.SUB);

		OAuth2Authorization token = authorizationService.findByToken(jwt.getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);

		return new JwtAuthenticationToken(jwt, authorities, principalClaimValue);
	}

}
