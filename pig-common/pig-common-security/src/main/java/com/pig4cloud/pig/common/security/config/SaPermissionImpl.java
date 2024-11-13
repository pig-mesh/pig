package com.pig4cloud.pig.common.security.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 鉴权数据源
 *
 * @author lengleng
 * @date 2024/7/22
 */
@RequiredArgsConstructor
public class SaPermissionImpl implements StpInterface {

	private final RemoteUserService remoteUserService;

	private final CacheManager cacheManager;

	/**
	 * 获取菜单权限列表
	 */
	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		String username = loginId.toString();

		// 从缓存中获取用户信息
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		if (Objects.nonNull(cache) && Objects.nonNull(cache.get(username))) {
			UserInfo userInfo = cache.get(username, UserInfo.class);
			return Arrays.stream(userInfo.getPermissions()).toList();
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(loginId.toString());
		return CollUtil.newArrayList(remoteUserService.info(userDTO).getData().getPermissions());
	}

	/**
	 * 获取角色权限列表
	 */
	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		String username = loginId.toString();

		// 从缓存中获取用户信息
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		if (Objects.nonNull(cache) && Objects.nonNull(cache.get(username))) {
			UserInfo userInfo = cache.get(username, UserInfo.class);
			return Arrays.stream(userInfo.getRoles()).map(String::valueOf).toList();
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(loginId.toString());
		Long[] roles = remoteUserService.info(userDTO).getData().getRoles();
		return Arrays.stream(roles).map(Object::toString).toList();
	}

}
