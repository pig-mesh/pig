package com.pig4cloud.pig.common.security.component;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 自定义不透明令牌内省器，用于处理OAuth2不透明令牌的验证和用户信息获取
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Slf4j
@RequiredArgsConstructor
public class PigCustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	/**
	 * OAuth2授权服务
	 */
	private final OAuth2AuthorizationService authorizationService;

	/**
	 * 根据token内省获取认证主体信息
	 * @param token 访问令牌
	 * @return OAuth2认证主体信息
	 * @throws InvalidBearerTokenException 当token对应的授权信息不存在时抛出
	 * @throws UsernameNotFoundException 当用户不存在时抛出
	 */
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		OAuth2Authorization oldAuthorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (Objects.isNull(oldAuthorization)) {
			throw new InvalidBearerTokenException(token);
		}

		// 客户端模式默认返回
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(oldAuthorization.getAuthorizationGrantType())) {
			return new DefaultOAuth2AuthenticatedPrincipal(oldAuthorization.getPrincipalName(),
					Objects.requireNonNull(oldAuthorization.getAccessToken().getClaims()),
					AuthorityUtils.NO_AUTHORITIES);
		}

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil
			.getBeansOfType(PigUserDetailsService.class);

		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values()
			.stream()
			.filter(service -> service.support(Objects.requireNonNull(oldAuthorization).getRegisteredClientId(),
					oldAuthorization.getAuthorizationGrantType().getValue()))
			.max(Comparator.comparingInt(Ordered::getOrder));

		UserDetails userDetails = null;
		try {
			Object principal = Objects.requireNonNull(oldAuthorization).getAttributes().get(Principal.class.getName());
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
			Object tokenPrincipal = usernamePasswordAuthenticationToken.getPrincipal();
			userDetails = optional.get().loadUserByUser((PigUser) tokenPrincipal);
		}
		catch (UsernameNotFoundException notFoundException) {
			log.warn("用户不不存在 {}", notFoundException.getLocalizedMessage());
			throw notFoundException;
		}
		catch (Exception ex) {
			log.error("资源服务器 introspect Token error {}", ex.getLocalizedMessage());
		}

		// 注入客户端信息，方便上下文中获取
		PigUser pigUser = (PigUser) userDetails;
		Objects.requireNonNull(pigUser)
			.getAttributes()
			.put(SecurityConstants.CLIENT_ID, oldAuthorization.getRegisteredClientId());
		return pigUser;
	}

}
