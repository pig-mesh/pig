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
package com.pig4cloud.pigx.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.*;
import com.pig4cloud.pigx.admin.config.ClientDetailsInitRunner;
import com.pig4cloud.pigx.admin.mapper.SysRoleMenuMapper;
import com.pig4cloud.pigx.admin.mapper.SysTenantMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.data.tenant.TenantBroker;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户
 *
 * mybatis-plus 3.4.3.3 特殊处理 https://github.com/baomidou/mybatis-plus/pull/3592
 *
 * @author lengleng
 * @date 2019-05-15 15:55:41
 */
@Service
@AllArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final SysOauthClientDetailsService clientServices;

	private final SysDeptRelationService deptRelationService;

	private final SysUserRoleService userRoleService;

	private final SysRoleMenuMapper roleMenuMapper;

	private final SysDictItemService dictItemService;

	private final SysPublicParamService paramService;

	private final SysUserService userService;

	private final SysRoleService roleService;

	private final SysMenuService menuService;

	private final SysDeptService deptService;

	private final SysDictService dictService;

	private final SysTenantMenuService sysTenantMenuService;

	/**
	 * 获取正常状态租户
	 * <p>
	 * 1. 状态正常 2. 开始时间小于等于当前时间 3. 结束时间大于等于当前时间
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstants.TENANT_DETAILS)
	public List<SysTenant> getNormalTenant() {
		return baseMapper
				.selectList(Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getStatus, CommonConstants.STATUS_NORMAL));
	}

	/**
	 * 保存租户
	 * <p>
	 * 1. 保存租户 2. 初始化权限相关表 - sys_user - sys_role - sys_menu - sys_user_role -
	 * sys_role_menu - sys_dict - sys_dict_item - sys_client_details - sys_public_params
	 * @param sysTenant 租户实体
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.TENANT_DETAILS)
	public Boolean saveTenant(SysTenant sysTenant) {
		this.save(sysTenant);
		// 查询系统默认租户配置参数
		Long defaultId = ParamResolver.getLong("TENANT_DEFAULT_ID", 1L);
		String defaultDeptName = ParamResolver.getStr("TENANT_DEFAULT_DEPTNAME", "租户默认部门");
		String defaultUsername = ParamResolver.getStr("TENANT_DEFAULT_USERNAME", "admin");
		String defaultPassword = ParamResolver.getStr("TENANT_DEFAULT_PASSWORD", "123456");
		String defaultRoleCode = ParamResolver.getStr("TENANT_DEFAULT_ROLECODE", "ROLE_ADMIN");
		String defaultRoleName = ParamResolver.getStr("TENANT_DEFAULT_ROLENAME", "租户默认角色");
		String userDefaultRoleCode = ParamResolver.getStr("USER_DEFAULT_ROLECODE", "GENERAL_USER");
		String userDefaultRoleName = ParamResolver.getStr("USER_DEFAULT_ROLENAME", "普通用户");

		List<SysDict> dictList = new ArrayList<>(32);
		List<Long> dictIdList = new ArrayList<>(32);
		List<SysDictItem> dictItemList = new ArrayList<>(64);
		List<SysMenu> menuList = new ArrayList<>(128);
		List<SysOauthClientDetails> clientDetailsList = new ArrayList<>(16);
		List<SysPublicParam> publicParamList = new ArrayList<>(64);

		TenantBroker.runAs(defaultId, (id) -> {
			// 查询系统内置字典
			dictList.addAll(dictService.list());
			// 查询系统内置字典项目
			dictIdList.addAll(dictList.stream().map(SysDict::getId).collect(Collectors.toList()));
			dictItemList.addAll(
					dictItemService.list(Wrappers.<SysDictItem>lambdaQuery().in(SysDictItem::getDictId, dictIdList)));
			// 查询当前租户菜单
			menuList.addAll(menuService.list());
			// 查询客户端配置
			clientDetailsList.addAll(clientServices.list());
			// 查询系统参数配置
			publicParamList.addAll(paramService.list());
		});

		// 保证插入租户为新的租户
		TenantBroker.applyAs(sysTenant.getId(), (id -> {

			// 插入部门
			SysDept dept = new SysDept();
			dept.setName(defaultDeptName);
			dept.setParentId(0L);
			deptService.save(dept);
			// 维护部门关系
			deptRelationService.insertDeptRelation(dept);
			// 构造初始化用户
			SysUser user = new SysUser();
			user.setUsername(defaultUsername);
			user.setPassword(ENCODER.encode(defaultPassword));
			user.setDeptId(dept.getDeptId());
			userService.save(user);

			// 构造普通用户角色
			SysRole roleDefault = new SysRole();
			roleDefault.setRoleCode(userDefaultRoleCode);
			roleDefault.setRoleName(userDefaultRoleName);
			roleService.save(roleDefault);

			// 构造新角色 管理员角色
			SysRole role = new SysRole();
			role.setRoleCode(defaultRoleCode);
			role.setRoleName(defaultRoleName);
			roleService.save(role);
			// 用户角色关系
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(user.getUserId());
			userRole.setRoleId(role.getRoleId());
			userRoleService.save(userRole);
			// 插入新的菜单
			saveTenantMenu(menuList, CommonConstants.MENU_TREE_ROOT_ID, CommonConstants.MENU_TREE_ROOT_ID);

			SysTenantMenu tenantMenu = sysTenantMenuService.getById(sysTenant.getMenuId());

			List<SysMenu> newMenuList = menuService
					.list(Wrappers.<SysMenu>lambdaQuery().in(SysMenu::getMenuId, tenantMenu.getMenuIds()));

			// 查询全部菜单,构造角色菜单关系
			List<SysRoleMenu> roleMenuList = newMenuList.stream().map(menu -> {
				SysRoleMenu roleMenu = new SysRoleMenu();
				roleMenu.setRoleId(role.getRoleId());
				roleMenu.setMenuId(menu.getMenuId());
				return roleMenu;
			}).collect(Collectors.toList());
			roleMenuList.forEach(ele -> roleMenuMapper.insert(ele));
			// 插入系统字典
			dictService.saveBatch(dictList.stream().peek(d -> d.setId(null)).collect(Collectors.toList()));
			// 处理字典项最新关联的字典ID
			List<SysDictItem> itemList = dictList.stream().flatMap(dict -> dictItemList.stream()
					.filter(item -> item.getDictType().equals(dict.getDictType())).peek(item -> {
						item.setDictId(dict.getId());
						item.setId(null);
					})).collect(Collectors.toList());

			// 插入客户端
			clientServices.saveBatch(clientDetailsList.stream().peek(d -> d.setId(null)).collect(Collectors.toList()));
			// 插入系统配置
			paramService
					.saveBatch(publicParamList.stream().peek(d -> d.setPublicId(null)).collect(Collectors.toList()));
			return dictItemService.saveBatch(itemList);
		}));

		SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(sysTenant));

		return Boolean.TRUE;
	}

	/**
	 * 保存新的租户菜单，维护成新的菜单
	 * @param menuList 菜单列表
	 * @param originParentId 原始上级
	 * @param targetParentId 目标上级
	 */
	private void saveTenantMenu(List<SysMenu> menuList, Long originParentId, Long targetParentId) {
		menuList.stream().filter(menu -> menu.getParentId().equals(originParentId)).forEach(menu -> {
			// 保存菜单原始menuId， 方便查询子节点使用
			Long originMenuId = menu.getMenuId();
			menu.setMenuId(null);
			menu.setParentId(targetParentId);
			menuService.save(menu);
			// 查找此节点的子节点，然后子节点的重新插入父节点更改为新的menuId
			saveTenantMenu(menuList, originMenuId, menu.getMenuId());
		});
	}

}
