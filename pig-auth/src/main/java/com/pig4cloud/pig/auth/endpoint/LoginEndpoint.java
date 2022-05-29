//package com.pig4cloud.pig.auth.endpoint;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.extra.spring.SpringUtil;
//import com.pig4cloud.pig.common.core.constant.SecurityConstants;
//import com.pig4cloud.pig.common.core.util.R;
//import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServletServerHttpResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.*;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
//import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
//import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
//import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
//import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
//import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.temporal.ChronoUnit;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * 登录端点
// *
// * @author lengleng
// * @date 2022/5/27
// */
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/oauth")
//public class LoginEndpoint {
//
//	private final OAuth2AuthorizationService tokenService;
//
//	private final RegisteredClientRepository registeredClientRepository;
//
//	private final OAuth2TokenGenerator tokenGenerator;
//
//	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
//
//	private final OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
//
//	private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
//
//	@SneakyThrows
//	@PostMapping("/token")
//	public void login(HttpServletRequest request, HttpServletResponse response, String username, String password) {
//
//		// 获取请求header 中的basic 信息
//		UsernamePasswordAuthenticationToken clientAuthentication = authenticationConverter.convert(request);
//		RegisteredClient client = registeredClientRepository.findByClientId(clientAuthentication.getName());
//
//		// 根据用户名查询用户
//		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil
//				.getBeansOfType(PigUserDetailsService.class);
//		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values().stream()
//				.filter(service -> service.support(client.getClientId(), AuthorizationGrantType.PASSWORD.getValue()))
//				.max(Comparator.comparingInt(Ordered::getOrder));
//		UserDetails userDetails = optional.get().loadUserByUsername(username);
//
//		// 生成accessToken
//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
//				null);
//		DefaultOAuth2TokenContext.Builder builder = DefaultOAuth2TokenContext.builder().registeredClient(client)
//				.principal(authenticationToken).tokenType(OAuth2TokenType.ACCESS_TOKEN)
//				.authorizationGrantType(AuthorizationGrantType.PASSWORD);
//		OAuth2Token generatedAccessToken = this.tokenGenerator.generate(builder.build());
//		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
//				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
//				generatedAccessToken.getExpiresAt(), builder.build().getAuthorizedScopes());
//
//		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(client)
//				.principalName(authenticationToken.getName()).authorizationGrantType(AuthorizationGrantType.PASSWORD);
//		if (generatedAccessToken instanceof ClaimAccessor) {
//			authorizationBuilder.token(accessToken,
//					(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
//							((ClaimAccessor) generatedAccessToken).getClaims()));
//		}
//		else {
//			authorizationBuilder.accessToken(accessToken);
//		}
//
//		// 创建刷新令牌
//		OAuth2Token generatedRefreshToken = this.refreshTokenGenerator
//				.generate(builder.tokenType(OAuth2TokenType.REFRESH_TOKEN)
//						.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN).build());
//		authorizationBuilder.refreshToken((OAuth2RefreshToken) generatedRefreshToken);
//		OAuth2Authorization authorization = authorizationBuilder.build();
//
//		// 保存认证信息
//		tokenService.save(authorization);
//
//		// 对外输出
//		Map<String, Object> additionalParameters = new HashMap<>();
//		additionalParameters.put("license", "pig");
//		additionalParameters.put(SecurityConstants.DETAILS_USER, userDetails);
//		OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken = new OAuth2AccessTokenAuthenticationToken(
//				client, authenticationToken, accessToken, (OAuth2RefreshToken) generatedRefreshToken,
//				additionalParameters);
//		sendAccessTokenResponse(response, oAuth2AccessTokenAuthenticationToken);
//
//	}
//
//	@GetMapping("/info")
//	public OAuth2Authorization info(String token) {
//		return tokenService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
//	}
//
//	private void sendAccessTokenResponse(HttpServletResponse response, Authentication authentication)
//			throws IOException {
//
//		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
//
//		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
//		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
//		Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();
//
//		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
//				.tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
//		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
//			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
//		}
//		if (refreshToken != null) {
//			builder.refreshToken(refreshToken.getTokenValue());
//		}
//		if (!CollectionUtils.isEmpty(additionalParameters)) {
//			builder.additionalParameters(additionalParameters);
//		}
//		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
//		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
//		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
//	}
//
//	@DeleteMapping("/logout")
//	public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
//		if (StrUtil.isBlank(authHeader)) {
//			return R.ok();
//		}
//
//		String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
//		OAuth2Authorization oAuth2Authorization = tokenService.findByToken(tokenValue, OAuth2TokenType.ACCESS_TOKEN);
//		tokenService.remove(oAuth2Authorization);
//		return R.ok();
//	}
//
//}
