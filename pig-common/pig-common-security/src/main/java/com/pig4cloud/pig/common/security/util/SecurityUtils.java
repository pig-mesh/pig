/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.security.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import lombok.experimental.UtilityClass;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取当前用户
	 * @return {@link PigUser} 当前登录的用户信息
	 */
	public PigUser getUser() {
		Object loginId = StpUtil.getLoginId();
		if (ObjectUtil.isNull(loginId)) {
			return null;
		}

		String username = loginId.toString();
		Cache cache = SpringUtil.getBean(CacheManager.class).getCache(CacheConstants.USER_DETAILS);
		PigUser pigUser = getCachedPigUser(cache, username);
		if (ObjectUtil.isNotNull(pigUser)) {
			return pigUser;
		}

		UserInfo userInfo = fetchUserInfoFromRemote(username);
		return convertToPigUser(loginId, userInfo);
	}

	/**
	 * 获取当前用户角色ID列表
	 * @return {@link List<Long>} 当前用户的角色ID列表
	 */
	public List<Long> getRoles() {
		Object loginId = StpUtil.getLoginId();
		if (ObjectUtil.isNull(loginId)) {
			return null;
		}

		String username = loginId.toString();
		Cache cache = SpringUtil.getBean(CacheManager.class).getCache(CacheConstants.USER_DETAILS);
		List<Long> roles = getCachedRoles(cache, username);
		if (ObjectUtil.isNotNull(roles)) {
			return roles;
		}

		UserInfo userInfo = fetchUserInfoFromRemote(username);
		return Arrays.asList(userInfo.getRoles());
	}

	/**
	 * 从缓存中获取 PigUser
	 */
	private PigUser getCachedPigUser(Cache cache, String username) {
		if (ObjectUtil.isNotNull(cache) && ObjectUtil.isNotNull(cache.get(username))) {
			UserInfo userInfo = cache.get(username, UserInfo.class);
			return convertToPigUser(username, userInfo);
		}
		return null;
	}

	/**
	 * 从缓存中获取角色列表
	 */
	private List<Long> getCachedRoles(Cache cache, String username) {
		if (ObjectUtil.isNotNull(cache) && ObjectUtil.isNotNull(cache.get(username))) {
			UserInfo userInfo = cache.get(username, UserInfo.class);
			return Arrays.asList(userInfo.getRoles());
		}
		return null;
	}

	/**
	 * 从远程服务获取用户信息
	 */
	public UserInfo fetchUserInfoFromRemote(String username) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		return SpringUtil.getBean(RemoteUserService.class).info(userDTO).getData();
	}

	/**
	 * 将 UserInfo 转换为 PigUser
	 */
	private PigUser convertToPigUser(Object loginId, UserInfo userInfo) {
		PigUser pigUser = new PigUser();
		pigUser.setUsername(loginId.toString());
		pigUser.setId(userInfo.getSysUser().getUserId());
		pigUser.setDeptId(userInfo.getSysUser().getDeptId());
		return pigUser;
	}

}
