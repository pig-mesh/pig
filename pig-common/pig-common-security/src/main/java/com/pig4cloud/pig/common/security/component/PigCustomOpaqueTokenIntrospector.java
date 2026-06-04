package com.pig4cloud.pig.common.security.component;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.entity.SysApiKey;
import com.pig4cloud.pig.admin.api.feign.RemoteApiKeyService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.security.service.PigDefaultUserDetailsServiceImpl;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author lengleng
 * @date 2022/5/28
 */
@Slf4j
@RequiredArgsConstructor
public class PigCustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private final OAuth2AuthorizationService authorizationService;

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		// API-KEY 前缀检测：sk- 开头走独立处理逻辑
		if (token.startsWith("sk-")) {
			return handleApiKeyToken(token);
		}

		OAuth2Authorization oldAuthorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (Objects.isNull(oldAuthorization)) {
			throw new InvalidBearerTokenException(token);
		}

		// 客户端模式默认返回
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(oldAuthorization.getAuthorizationGrantType())) {
			return new PigClientCredentialsOAuth2AuthenticatedPrincipal(
					Objects.requireNonNull(oldAuthorization.getAccessToken().getClaims()),
					AuthorityUtils.NO_AUTHORITIES, oldAuthorization.getPrincipalName());
		}

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringContextHolder
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

		// 检查用户账号状态
		if (Objects.nonNull(userDetails)) {
			boolean isLocked = !userDetails.isAccountNonLocked();
			boolean isExpired = !userDetails.isAccountNonExpired();

			if (isLocked) {
				log.warn("用户账号 {} 已被锁定，拒绝访问", userDetails.getUsername());
				throw new InvalidBearerTokenException(token);
			}

			if (isExpired) {
				log.warn("用户账号 {} 已过期，拒绝访问", userDetails.getUsername());
				throw new InvalidBearerTokenException(token);
			}
		}

		// 注入客户端信息，方便上下文中获取
		PigUser pigUser = (PigUser) userDetails;

		Objects.requireNonNull(pigUser)
			.getAttributes()
			.put(SecurityConstants.CLIENT_ID, oldAuthorization.getRegisteredClientId());
		return pigUser;
	}

	/**
	 * 处理 API-KEY 类型的 token 1. 计算 SHA-256 哈希 2. 查询 API-KEY 记录 3. 校验状态、过期时间、IP白名单 4.
	 * 加载用户信息并返回
	 */
	private OAuth2AuthenticatedPrincipal handleApiKeyToken(String token) {
		String hash = sha256(token);

		// 优先从缓存获取，避免远程调用
		CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);
		Cache cache = cacheManager.getCache(CacheConstants.API_KEY_DETAILS);
		RemoteApiKeyService remoteApiKeyService = SpringContextHolder.getBean(RemoteApiKeyService.class);
		SysApiKey apiKey = null;
		if (cache != null && cache.get(hash) != null) {
			apiKey = cache.get(hash, SysApiKey.class);
		}

		if (apiKey == null) {
			R<SysApiKey> result = remoteApiKeyService.getByHash(hash);
			if (result == null || result.getData() == null) {
				throw new InvalidBearerTokenException("Invalid API Key");
			}
			apiKey = result.getData();
			// 回填缓存
			if (cache != null) {
				cache.put(hash, apiKey);
			}
		}

		// 校验过期时间
		if (apiKey.getExpiresAt() != null && apiKey.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new InvalidBearerTokenException("API Key has expired");
		}

		// 校验 IP 白名单
		if (apiKey.getAllowedIps() != null && !apiKey.getAllowedIps().isEmpty()) {
			String requestIp = WebUtils.getIP();
			if (requestIp == null || !isIpAllowed(requestIp, apiKey.getAllowedIps())) {
				throw new InvalidBearerTokenException("IP not allowed for this API Key");
			}
		}

		// 通过 apiKey 中的 username 直接加载用户详情，无需二次远程调用
		UserDetails userDetails = SpringContextHolder.getBean(PigDefaultUserDetailsServiceImpl.class)
			.loadUserByUsername(apiKey.getUsername());
		if (!(userDetails instanceof PigUser pigUser)) {
			throw new InvalidBearerTokenException("API Key owner not found");
		}

		if (!pigUser.isAccountNonLocked()) {
			log.warn("用户账号 {} 已被锁定，拒绝 API Key 访问", pigUser.getUsername());
			throw new InvalidBearerTokenException("Account is locked");
		}
		if (!pigUser.isAccountNonExpired()) {
			log.warn("用户账号 {} 已过期，拒绝 API Key 访问", pigUser.getUsername());
			throw new InvalidBearerTokenException("Account has expired");
		}

		// 异步更新最后使用时间，不阻塞主流程
		final Long apiKeyId = apiKey.getId();
		CompletableFuture.runAsync(() -> {
			try {
				remoteApiKeyService.updateLastUsed(apiKeyId);
			}
			catch (Exception ex) {
				log.warn("Failed to update API Key last_used_at: {}", ex.getMessage());
			}
		});

		pigUser.getAttributes().put(SecurityConstants.CLIENT_ID, "apikey");
		return pigUser;
	}

	private boolean isIpAllowed(String requestIp, String allowedIps) {
		return Arrays.stream(allowedIps.split(StrUtil.COMMA)).map(String::trim).anyMatch(requestIp::equals);
	}

	private static String sha256(String input) {
		try {
			byte[] hash = java.security.MessageDigest.getInstance("SHA-256")
				.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		}
		catch (java.security.NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}

}
