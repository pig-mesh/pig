package com.pig4cloud.pigx.common.security.service;

import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author aeizzz
 */
@Slf4j
@RequiredArgsConstructor
public class PigxMobileUserDetailServiceImpl implements PigxUserDetailsService {


	private final UserDetailsService pigxDefaultUserDetailsServiceImpl;
	private final RemoteUserService remoteUserService;
	private final CacheManager cacheManager;

	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String phone) {
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		if (cache != null && cache.get(phone) != null) {
			return cache.get(phone, PigxUser.class);
		}

		R<UserInfo> result = remoteUserService.social(phone, SecurityConstants.FROM_IN);

		UserDetails userDetails = getUserDetails(result);
		if (cache != null) {
			cache.put(phone, userDetails);
		}
		return getUserDetails(result);
	}

	@Override
	public UserDetails loadUserByUser(PigxUser pigxUser) {
		return pigxDefaultUserDetailsServiceImpl.loadUserByUsername(pigxUser.getUsername());
	}


	/**
	 * 支持所有的 mobile 类型
	 *
	 * @param clientId  目标客户端
	 * @param grantType 授权类型
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return SecurityConstants.GRANT_MOBILE.equals(grantType) && !SecurityConstants.CLIENT_MINI.equals(clientId);
	}
}
