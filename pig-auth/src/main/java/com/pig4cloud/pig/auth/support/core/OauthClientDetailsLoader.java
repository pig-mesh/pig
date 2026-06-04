package com.pig4cloud.pig.auth.support.core;

import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.api.feign.RemoteClientDetailsService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * OAuth 客户端详情加载器。
 * <p>
 * 按需通过 {@link RemoteClientDetailsService} 远程查询客户端详情，首次查库后写入 Spring Cache，
 * 后续命中缓存，替代原先启动时全量预热到 Redis 的方式。复用 {@link CacheConstants#CLIENT_DETAILS_KEY} 缓存，与
 * {@code PigRemoteRegisteredClientRepository} 共享同一份按 clientId 缓存的
 * {@link SysOauthClientDetails}，写操作只需统一失效一处。
 *
 * @author pig
 */
@Component
@RequiredArgsConstructor
public class OauthClientDetailsLoader {

	private final RemoteClientDetailsService clientDetailsService;

	/**
	 * 按 clientId 查询客户端详情，首次查库后写入 Spring Cache，后续命中缓存。
	 * <p>
	 * 必须作为独立 Bean 被外部调用，方可触发 {@link Cacheable} 代理；{@code unless} 避免把查不到的 null
	 * 结果写入缓存而污染共享的客户端缓存。
	 * @param clientId 客户端ID
	 * @return 客户端详情，不存在时返回 {@code null}
	 */
	@Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
	public SysOauthClientDetails getByClientId(String clientId) {
		return RetOps.of(clientDetailsService.getClientDetailsById(clientId)).getData().orElse(null);
	}

}
