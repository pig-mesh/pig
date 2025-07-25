package com.pig4cloud.pig.auth.support.base;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.security.util.OAuth2ErrorCodesExpand;
import com.pig4cloud.pig.common.security.util.ScopeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

/**
 * OAuth2资源所有者基础认证提供者抽象类，用于处理资源所有者密码凭证授权流程
 *
 * @param <T> OAuth2资源所有者基础认证令牌类型
 * @author lengleng
 * @date 2025/05/30
 */
public abstract class OAuth2ResourceOwnerBaseAuthenticationProvider<T extends OAuth2ResourceOwnerBaseAuthenticationToken>
		implements AuthenticationProvider {

	private static final Logger LOGGER = LogManager.getLogger(OAuth2ResourceOwnerBaseAuthenticationProvider.class);

	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	private final AuthenticationManager authenticationManager;

	private final MessageSourceAccessor messages;

	@Deprecated
	private Supplier<String> refreshTokenGenerator;

	/**
	 * 构造一个基于资源所有者密码模式的OAuth2认证提供者
	 * @param authenticationManager 认证管理器
	 * @param authorizationService 授权服务
	 * @param tokenGenerator token生成器
	 * @throws IllegalArgumentException 当authorizationService或tokenGenerator为null时抛出
	 * @since 0.2.3
	 */
	public OAuth2ResourceOwnerBaseAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authenticationManager = authenticationManager;
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;

		// 国际化配置
		this.messages = new MessageSourceAccessor(SpringUtil.getBean("securityMessageSource"), Locale.CHINA);
	}

	/**
	 * 设置刷新令牌生成器
	 * @param refreshTokenGenerator 刷新令牌生成器，不能为null
	 * @deprecated 该方法已废弃
	 */
	@Deprecated
	public void setRefreshTokenGenerator(Supplier<String> refreshTokenGenerator) {
		Assert.notNull(refreshTokenGenerator, "refreshTokenGenerator cannot be null");
		this.refreshTokenGenerator = refreshTokenGenerator;
	}

	/**
	 * 构建用户名密码认证令牌
	 * @param reqParameters 请求参数映射
	 * @return 用户名密码认证令牌
	 */
	public abstract UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters);

	/**
	 * 当前provider是否支持此令牌类型
	 * @param authentication
	 * @return
	 */
	@Override
	public abstract boolean supports(Class<?> authentication);

	/**
	 * 当前的请求客户端是否支持此模式
	 * @param registeredClient
	 */
	public abstract void checkClient(RegisteredClient registeredClient);

	/**
	 * 执行认证操作，遵循与{@link AuthenticationManager#authenticate(Authentication)}相同的契约
	 * @param authentication 认证请求对象
	 * @return 包含凭证的完整认证对象，如果当前认证提供者无法处理传入的认证对象可能返回null
	 * @throws AuthenticationException 认证失败时抛出
	 * @throws OAuth2AuthenticationException 当scope无效或token生成失败时抛出
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		T resouceOwnerBaseAuthentication = (T) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				resouceOwnerBaseAuthentication);

		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
		checkClient(registeredClient);

		Set<String> authorizedScopes;
		// Default to configured scopes
		if (!CollectionUtils.isEmpty(resouceOwnerBaseAuthentication.getScopes())) {
			for (String requestedScope : resouceOwnerBaseAuthentication.getScopes()) {
				if (!registeredClient.getScopes().contains(requestedScope)) {
					throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
				}
			}
			authorizedScopes = new LinkedHashSet<>(resouceOwnerBaseAuthentication.getScopes());
		}
		else {
			authorizedScopes = new LinkedHashSet<>();
		}

		Map<String, Object> reqParameters = resouceOwnerBaseAuthentication.getAdditionalParameters();
		try {

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(reqParameters);

			LOGGER.debug("got usernamePasswordAuthenticationToken=" + usernamePasswordAuthenticationToken);

			Authentication usernamePasswordAuthentication = authenticationManager
				.authenticate(usernamePasswordAuthenticationToken);

			// @formatter:off
			DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
					.registeredClient(registeredClient)
					.principal(usernamePasswordAuthentication)
					.authorizationServerContext(AuthorizationServerContextHolder.getContext())
					.authorizedScopes(authorizedScopes)
					.authorizationGrantType(resouceOwnerBaseAuthentication.getAuthorizationGrantType())
					.authorizationGrant(resouceOwnerBaseAuthentication);
			// @formatter:on

			OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
				.withRegisteredClient(registeredClient)
				.principalName(usernamePasswordAuthentication.getName())
				.authorizationGrantType(resouceOwnerBaseAuthentication.getAuthorizationGrantType())
				// 0.4.0 新增的方法
				.authorizedScopes(authorizedScopes);

			// ----- Access token -----
			OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
			OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
			if (generatedAccessToken == null) {
				OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
						"The token generator failed to generate the access token.", ERROR_URI);
				throw new OAuth2AuthenticationException(error);
			}
			OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
					generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
					generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
			if (generatedAccessToken instanceof ClaimAccessor) {
				authorizationBuilder.id(accessToken.getTokenValue())
					.token(accessToken,
							(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
									((ClaimAccessor) generatedAccessToken).getClaims()))
					// 0.4.0 新增的方法
					.authorizedScopes(authorizedScopes)
					.attribute(Principal.class.getName(), usernamePasswordAuthentication);
			}
			else {
				authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
			}

			// ----- Refresh token -----
			OAuth2RefreshToken refreshToken = null;
			if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
			// Do not issue refresh token to public client
					!clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

				if (this.refreshTokenGenerator != null) {
					Instant issuedAt = Instant.now();
					Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
					refreshToken = new OAuth2RefreshToken(this.refreshTokenGenerator.get(), issuedAt, expiresAt);
				}
				else {
					tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
					OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
					if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
						OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
								"The token generator failed to generate the refresh token.", ERROR_URI);
						throw new OAuth2AuthenticationException(error);
					}
					refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
				}
				authorizationBuilder.refreshToken(refreshToken);
			}

			OAuth2Authorization authorization = authorizationBuilder.build();

			this.authorizationService.save(authorization);

			LOGGER.debug("returning OAuth2AccessTokenAuthenticationToken");

			return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
					refreshToken, Objects.requireNonNull(authorization.getAccessToken().getClaims()));

		}
		catch (Exception ex) {
			LOGGER.error("problem in authenticate", ex);
			throw oAuth2AuthenticationException(authentication, (AuthenticationException) ex);
		}

	}

	/**
	 * 登录异常转换为oauth2异常
	 * @param authentication 身份验证
	 * @param authenticationException 身份验证异常
	 * @return {@link OAuth2AuthenticationException}
	 */
	private OAuth2AuthenticationException oAuth2AuthenticationException(Authentication authentication,
			AuthenticationException authenticationException) {
		if (authenticationException instanceof UsernameNotFoundException) {
			return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USERNAME_NOT_FOUND,
					this.messages.getMessage("JdbcDaoImpl.notFound", new Object[] { authentication.getName() },
							"Username {0} not found"),
					""));
		}
		if (authenticationException instanceof BadCredentialsException) {
			return new OAuth2AuthenticationException(
					new OAuth2Error(OAuth2ErrorCodesExpand.BAD_CREDENTIALS, this.messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), ""));
		}
		if (authenticationException instanceof LockedException) {
			return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_LOCKED, this.messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"), ""));
		}
		if (authenticationException instanceof DisabledException) {
			return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_DISABLE,
					this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"),
					""));
		}
		if (authenticationException instanceof AccountExpiredException) {
			return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_EXPIRED, this.messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"), ""));
		}
		if (authenticationException instanceof CredentialsExpiredException) {
			return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.CREDENTIALS_EXPIRED,
					this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired",
							"User credentials have expired"),
					""));
		}
		if (authenticationException instanceof ScopeException) {
			return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_SCOPE,
					this.messages.getMessage("AbstractAccessDecisionManager.accessDenied", "invalid_scope"), ""));
		}
		return new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.UN_KNOW_LOGIN_ERROR);
	}

	/**
	 * 获取已认证的客户端主体，否则抛出无效客户端异常
	 * @param authentication 认证信息
	 * @return 已认证的客户端主体
	 * @throws OAuth2AuthenticationException 客户端未认证时抛出异常
	 */
	private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
			Authentication authentication) {

		OAuth2ClientAuthenticationToken clientPrincipal = null;

		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}

		if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}

		throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
	}

}
