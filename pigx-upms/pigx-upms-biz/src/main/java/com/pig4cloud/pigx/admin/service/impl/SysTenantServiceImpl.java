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

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
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
import com.pig4cloud.pigx.common.data.datascope.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.data.tenant.TenantBroker;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
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
 * <p>
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

	private final SysUserRoleService userRoleService;

	private final SysRoleMenuMapper roleMenuMapper;

	private final SysDictItemService dictItemService;

	private final SysPublicParamService paramService;

	private final SysUserService userService;

	private final SysRoleService roleService;

	private final SysMenuService menuService;

	private final SysDeptService deptService;

	private final SysDictService dictService;

	private final CacheManager cacheManager;

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
			dictIdList.addAll(dictList.stream().map(SysDict::getId).toList());
			dictItemList.addAll(
					dictItemService.list(Wrappers.<SysDictItem>lambdaQuery().in(SysDictItem::getDictId, dictIdList)));
			List<SysMenu> newMenuList = menuService.list(Wrappers.<SysMenu>lambdaQuery()
				.in(SysMenu::getMenuId, StrUtil.split(sysTenant.getMenuId(), CharUtil.COMMA)));
			// 查询当前租户菜单
			menuList.addAll(newMenuList);
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
			roleDefault.setDsType(DataScopeTypeEnum.SELF_LEVEL.getType());
			roleService.save(roleDefault);

			// 构造新角色 管理员角色
			SysRole role = new SysRole();
			role.setRoleCode(defaultRoleCode);
			role.setRoleName(defaultRoleName);
			role.setDsType(DataScopeTypeEnum.ALL.getType());
			roleService.save(role);
			// 用户角色关系
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(user.getUserId());
			userRole.setRoleId(role.getRoleId());
			userRoleService.save(userRole);
			// 插入新的菜单
			saveTenantMenu(menuList, CommonConstants.MENU_TREE_ROOT_ID, CommonConstants.MENU_TREE_ROOT_ID);

			// 重新查询出所有的菜单关联角色
			List<SysMenu> list = menuService.list();

			// 查询全部菜单,构造角色菜单关系
			List<SysRoleMenu> roleMenuList = list.stream().map(menu -> {
				SysRoleMenu roleMenu = new SysRoleMenu();
				roleMenu.setRoleId(role.getRoleId());
				roleMenu.setMenuId(menu.getMenuId());
				return roleMenu;
			}).toList();
			roleMenuList.forEach(roleMenuMapper::insert);
			// 插入系统字典
			dictService.saveBatch(dictList.stream().peek(d -> d.setId(null)).collect(Collectors.toList()));
			// 处理字典项最新关联的字典ID
			List<SysDictItem> itemList = dictList.stream()
				.flatMap(dict -> dictItemList.stream()
					.filter(item -> item.getDictType().equals(dict.getDictType()))
					.peek(item -> {
						item.setDictId(dict.getId());
						item.setId(null);
					}))
				.collect(Collectors.toList());

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
	 * 修改租户
	 * @param tenantDTO
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public Boolean updateTenant(SysTenant tenantDTO) {
		SysTenant tenant = baseMapper.selectById(tenantDTO.getId());
		// 更新租户数据
		updateById(tenantDTO);

		// 如果没有修改租户套餐
		Long defaultId = ParamResolver.getLong("TENANT_DEFAULT_ID", 1L);
		if (defaultId.equals(tenantDTO.getId())) {
			return Boolean.TRUE;
		}
		if (tenant.getMenuId().equals(tenantDTO.getMenuId())) {
			return Boolean.TRUE;
		}

		List<SysMenu> sysMenuList = TenantBroker.applyAs(defaultId,
				id -> menuService.list(Wrappers.<SysMenu>lambdaQuery()
					.in(SysMenu::getMenuId, StrUtil.split(tenantDTO.getMenuId(), CharUtil.COMMA))));

		TenantBroker.runAs(tenantDTO.getId(), (tenantId -> {
			// 查询当前租户的所有菜单
			List<SysMenu> menuList = menuService.list(Wrappers.emptyWrapper());

			// 套餐功能对比和已有菜单对比 （新增）
			List<SysMenu> addMenuList = new ArrayList<>();
			List<SysMenu> delMenuList = new ArrayList<>();

			// 判断新增的菜单列表
			menuExist(sysMenuList, menuList, addMenuList);

			// 判断删除的菜单列表
			menuExist(menuList, sysMenuList, delMenuList);

			// 新增的菜单
			this.saveTenantMenu(addMenuList, CommonConstants.MENU_TREE_ROOT_ID, CommonConstants.MENU_TREE_ROOT_ID);
			// 套餐删除的菜单
			List<Long> menuIdList = delMenuList.stream().map(SysMenu::getMenuId).toList();
			menuService.removeBatchByIds(menuIdList);

			// 清空菜单权限
			cacheManager.getCache(CacheConstants.MENU_DETAILS).clear();
		}));

		return Boolean.TRUE;
	}

	/**
	 * 判断菜单是否存在
	 * @param sysMenuList 要判断的菜单列表
	 * @param menuList 已有菜单列表
	 * @param addMenuList 待添加的菜单列表
	 */
	private void menuExist(List<SysMenu> sysMenuList, List<SysMenu> menuList, List<SysMenu> addMenuList) {
		for (SysMenu sysMenu : sysMenuList) {

			if (StrUtil.isNotBlank(sysMenu.getPath())) {
				// 根据菜单path名称，查询套餐菜单是否存在
				if (menuList.stream().noneMatch(menu -> sysMenu.getPath().equals(menu.getPath()))) {
					addMenuList.add(sysMenu);
				}
			}

			if (StrUtil.isNotBlank(sysMenu.getPermission())) {
				// 根据菜单permission名称，查询套餐菜单是否存在
				if (menuList.stream().noneMatch(menu -> sysMenu.getPermission().equals(menu.getPermission()))) {
					addMenuList.add(sysMenu);
				}
			}

		}
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
