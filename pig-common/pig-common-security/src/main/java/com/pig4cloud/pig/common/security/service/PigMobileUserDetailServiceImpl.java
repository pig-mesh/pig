package com.pig4cloud.pig.common.security.service;

import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author aeizzz
 */
@Slf4j
@RequiredArgsConstructor
public class PigMobileUserDetailServiceImpl implements PigUserDetailsService {

	private final UserDetailsService pigDefaultUserDetailsServiceImpl;

	private final RemoteUserService remoteUserService;

	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String phone) {
		R<UserInfo> result = remoteUserService.social(phone);
		return getUserDetails(RetOps.of(result).getData());
	}

	@Override
	public UserDetails loadUserByUser(PigUser pigUser) {
		return pigDefaultUserDetailsServiceImpl.loadUserByUsername(pigUser.getUsername());
	}

	/**
	 * 支持所有的 mobile 类型
	 * @param clientId 目标客户端
	 * @param grantType 授权类型
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return SecurityConstants.GRANT_MOBILE.equals(grantType);
	}

}
