/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.dto.AppUserDTO;
import com.pig4cloud.pigx.app.api.dto.AppUserInfo;
import com.pig4cloud.pigx.app.api.entity.AppMenu;
import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.app.api.entity.AppUser;
import com.pig4cloud.pigx.app.api.entity.AppUserRole;
import com.pig4cloud.pigx.app.api.vo.AppUserExcelVO;
import com.pig4cloud.pigx.app.mapper.AppUserMapper;
import com.pig4cloud.pigx.app.service.AppMenuService;
import com.pig4cloud.pigx.app.service.AppRoleService;
import com.pig4cloud.pigx.app.service.AppUserRoleService;
import com.pig4cloud.pigx.app.service.AppUserService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * app用户表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Service
@AllArgsConstructor
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final AppUserRoleService appUserRoleService;

	private final AppRoleService appRoleService;

	private final AppMenuService appMenuService;

	/**
	 * 更新用户
	 * @param userDTO
	 * @return
	 */
	@Override
	public Boolean updateUser(AppUserDTO userDTO) {

		AppUser appUser = new AppUser();
		BeanUtils.copyProperties(userDTO, appUser);
		if (StrUtil.isNotBlank(userDTO.getPassword())) {
			appUser.setPassword(ENCODER.encode(userDTO.getPassword()));
		}
		this.updateById(appUser);

		appUserRoleService.remove(Wrappers.<AppUserRole>lambdaQuery().eq(AppUserRole::getUserId, appUser.getUserId()));
		userDTO.getRole().forEach(roleId -> {
			AppUserRole appUserRole = new AppUserRole();
			appUserRole.setRoleId(roleId);
			appUserRole.setUserId(userDTO.getUserId());
			appUserRole.insert();
		});
		return Boolean.TRUE;
	}

	/**
	 * 新增用户
	 * @param userDTO
	 * @return
	 */
	@Override
	public Boolean saveUser(AppUserDTO userDTO) {
		AppUser appUser = new AppUser();
		BeanUtils.copyProperties(userDTO, appUser);
		appUser.setDelFlag(CommonConstants.STATUS_NORMAL);
		appUser.setPassword(ENCODER.encode(userDTO.getPassword()));
		baseMapper.insert(appUser);
		// 如果角色为空，赋默认角色
		if (CollUtil.isNotEmpty(userDTO.getRole())) {
			List<AppUserRole> userRoleList = userDTO.getRole().stream().map(roleId -> {
				AppUserRole userRole = new AppUserRole();
				userRole.setUserId(userDTO.getUserId());
				userRole.setRoleId(roleId);
				return userRole;
			}).collect(Collectors.toList());
			appUserRoleService.saveBatch(userRoleList);
		}
		return Boolean.TRUE;
	}

	/**
	 * 查询全部的用户
	 * @param appUser
	 * @return
	 */
	@Override
	public List<AppUserExcelVO> listUser(AppUserDTO appUser) {
		List<AppUser> appUsers = baseMapper.selectList(null);
		List<AppUserExcelVO> appUserExcelVOS = appUsers.stream().map(item -> {
			AppUserExcelVO appUserExcelVO = new AppUserExcelVO();
			BeanUtils.copyProperties(item, appUserExcelVO);
			return appUserExcelVO;
		}).collect(Collectors.toList());
		return appUserExcelVOS;
	}

	@Override
	public IPage getUsersWithRolePage(Page page, AppUserDTO appUserDTO) {
		return baseMapper.getUserVosPage(page, appUserDTO);
	}

	@Override
	public Boolean deleteUserById(Long userId) {
		baseMapper.deleteById(userId);
		appUserRoleService.removeById(Wrappers.<AppUserRole>lambdaQuery().eq(AppUserRole::getUserId, userId));

		return Boolean.TRUE;
	}

	@Override
	public AppUserInfo findUserInfo(AppUser user) {
		AppUserInfo info = new AppUserInfo();
		info.setAppUser(user);
		// 设置角色列表 （ID）
		List<Long> roleIds = appRoleService.findRolesByUserId(user.getUserId()).stream().map(AppRole::getRoleId)
				.collect(Collectors.toList());
		info.setRoles(ArrayUtil.toArray(roleIds, Long.class));

		// 设置权限列表（menu.permission）
		Set<String> permissions = new HashSet<>();
		roleIds.forEach(roleId -> {
			List<String> permissionList = appMenuService.findMenuByRoleId(roleId).stream()
					.filter(menu -> StrUtil.isNotEmpty(menu.getPermission())).map(AppMenu::getPermission)
					.collect(Collectors.toList());
			permissions.addAll(permissionList);
		});
		info.setPermissions(ArrayUtil.toArray(permissions, String.class));
		return info;
	}

}
