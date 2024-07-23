package com.pig4cloud.pig.common.security.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author lengleng
 * @date 2024/7/22
 */
@RequiredArgsConstructor
public class SaPermissionImpl implements StpInterface {

	private final RemoteUserService remoteUserService;

	/**
	 * 获取菜单权限列表
	 */
	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(loginId.toString());
		return CollUtil.newArrayList(remoteUserService.info(userDTO).getData().getPermissions());
	}

	/**
	 * 获取角色权限列表
	 */
	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(loginId.toString());
		Long[] roles = remoteUserService.info(userDTO).getData().getRoles();
		return Arrays.stream(roles).map(Object::toString).toList();
	}

}
