package com.pig4cloud.pig.common.security.component;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.security.exception.UnauthorizedException;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lengleng
 * @date 2020/9/29
 */
@RequiredArgsConstructor
public class PigLocalResourceServerTokenServices implements ResourceServerTokenServices {

	private final JwtDecoder jwtDecoder;

	private final ClientDetailsService clientDetailsService;

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {

		Jwt decode = jwtDecoder.decode(accessToken);

		String username = decode.getSubject();

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil
				.getBeansOfType(PigUserDetailsService.class);
		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support("pig", "password")).max(Comparator.comparingInt(Ordered::getOrder));

		UserDetails userDetails;
		try {
			userDetails = optional.get().loadUserByUsername(username);
		}
		catch (UsernameNotFoundException notFoundException) {
			throw new UnauthorizedException(String.format("%s username not found", username), notFoundException);
		}
		Authentication userAuthentication = new UsernamePasswordAuthenticationToken(userDetails, "N/A",
				userDetails.getAuthorities());

		ClientDetails client = clientDetailsService.loadClientByClientId("pig");
		OAuth2Request request = new OAuth2Request(new HashMap<>(), client.getClientId(), client.getAuthorities(),
				true,client.getScope(),
				client.getResourceIds(),null,null,null);
		OAuth2Authentication authentication = new OAuth2Authentication(request, userAuthentication);
		authentication.setAuthenticated(true);
		return authentication;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

}
