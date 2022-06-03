package com.pig4cloud.pig.common.security.component;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * @author lengleng
 * @date 2022/5/28
 */
@RequiredArgsConstructor
public class PigCustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private final OAuth2AuthorizationService authorizationService;

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		OAuth2Authorization oldAuthorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil
				.getBeansOfType(PigUserDetailsService.class);

		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support(oldAuthorization.getRegisteredClientId(),
						oldAuthorization.getAuthorizationGrantType().getValue()))
				.max(Comparator.comparingInt(Ordered::getOrder));

		UserDetails userDetails = null;
		try {
			userDetails = optional.get().loadUserByUsername(oldAuthorization.getPrincipalName());
		}
		catch (UsernameNotFoundException notFoundException) {
		}

		PigUser user = (PigUser) userDetails;
		return user;
	}

}
