package com.pig4cloud.pig.auth.endpoint;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 登录端点
 *
 * @author lengleng
 * @date 2022/5/27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/password")
public class LoginEndpoint {

	private final OAuth2AuthorizationService tokenService;

	private final RegisteredClientRepository registeredClientRepository;

	private final OAuth2TokenGenerator tokenGenerator;

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

	private final OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

	@SneakyThrows
	@RequestMapping("/login")
	public void login(HttpServletResponse response, HttpServletRequest request) {

		RegisteredClient client = registeredClientRepository.findByClientId("pig");

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin",
				"123");
		// @formatter:off
		DefaultOAuth2TokenContext.Builder builder = DefaultOAuth2TokenContext.builder()
				.registeredClient(client)
				.principal(authenticationToken)
				.tokenType(OAuth2TokenType.ACCESS_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD);
		// @formatter:on
		OAuth2Token generatedAccessToken = this.tokenGenerator.generate(builder.build());
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), builder.build().getAuthorizedScopes());

		// @formatter:off
		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(client)
				.principalName("pig")
				.principalName(authenticationToken.getName())
				.authorizationGrantType(AuthorizationGrantType.PASSWORD);
		// @formatter:on
		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(accessToken,
					(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
							((ClaimAccessor) generatedAccessToken).getClaims()));
		}
		else {
			authorizationBuilder.accessToken(accessToken);
		}

		OAuth2Token generatedRefreshToken = this.refreshTokenGenerator
				.generate(builder.tokenType(OAuth2TokenType.REFRESH_TOKEN)
						.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN).build());
		authorizationBuilder.refreshToken((OAuth2RefreshToken) generatedRefreshToken);
		OAuth2Authorization authorization = authorizationBuilder.build();
		tokenService.save(authorization);

		Map<String, Object> additionalParameters = new HashMap<>();

		additionalParameters.put("license", "pig");

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil
				.getBeansOfType(PigUserDetailsService.class);
		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support(client.getClientId(), "password"))
				.max(Comparator.comparingInt(Ordered::getOrder));

		UserDetails admin = optional.get().loadUserByUsername("admin");

		additionalParameters.put(SecurityConstants.DETAILS_USER, admin);

		OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken = new OAuth2AccessTokenAuthenticationToken(
				client, authenticationToken, accessToken, (OAuth2RefreshToken) generatedRefreshToken,
				additionalParameters);
		sendAccessTokenResponse(request, response, oAuth2AccessTokenAuthenticationToken);

	}

	@GetMapping("/info")
	public OAuth2Authorization info(String token) {
		return tokenService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
	}

	private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
		Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
				.tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}
		if (!CollectionUtils.isEmpty(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}
		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}

}
