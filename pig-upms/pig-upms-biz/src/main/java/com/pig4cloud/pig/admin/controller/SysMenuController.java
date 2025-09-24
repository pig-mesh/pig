/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pig.admin.controller;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pig4cloud.pig.admin.api.entity.SysMenu;
import com.pig4cloud.pig.admin.service.SysMenuService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.util.SecurityUtils;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * 菜单管理控制器
 *
 * @author lengleng
 * @date 2025/05/30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Tag(description = "menu", name = "菜单管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMenuController {

	private final SysMenuService sysMenuService;

	/**
	 * 获取当前用户的树形菜单集合
	 * @param type 菜单类型
	 * @param parentId 父菜单ID
	 * @return 包含菜单数据的响应对象
	 */
	@GetMapping
	@Operation(summary = "获取当前用户的树形菜单集合", description = "获取当前用户的树形菜单集合")
	public R getUserMenu(String type, Long parentId) {
		// 获取符合条件的菜单
		Set<SysMenu> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
		return R.ok(sysMenuService.filterMenu(all, type, parentId));
	}

	/**
	 * 获取树形菜单集合
	 * @param parentId 父节点ID
	 * @param menuName 菜单名称
	 * @param type 菜单类型
	 * @return 包含树形菜单的响应结果
	 */
	@GetMapping(value = "/tree")
	@Operation(summary = "获取树形菜单集合", description = "获取树形菜单集合")
	public R getMenuTree(Long parentId, String menuName, String type) {
		return R.ok(sysMenuService.getMenuTree(parentId, menuName, type));
	}

	/**
	 * 根据角色ID获取菜单树
	 * @param roleId 角色ID
	 * @return 包含菜单ID列表的响应结果
	 */
	@GetMapping("/tree/{roleId}")
	@Operation(summary = "根据角色ID获取菜单树", description = "根据角色ID获取菜单树")
	public R getRoleTree(@PathVariable Long roleId) {
		return R.ok(sysMenuService.findMenuByRoleId(roleId).stream().map(SysMenu::getMenuId).toList());
	}

	/**
	 * 通过ID查询菜单的详细信息
	 * @param id 菜单ID
	 * @return 包含菜单详细信息的响应对象
	 */
	@GetMapping("/{id}")
	@Operation(summary = "通过ID查询菜单的详细信息", description = "通过ID查询菜单的详细信息")
	public R getById(@PathVariable Long id) {
		return R.ok(sysMenuService.getById(id));
	}

	/**
	 * 新增菜单
	 * @param sysMenu 菜单信息
	 * @return 操作结果
	 */
	@SysLog("新增菜单")
	@PostMapping
	@HasPermission("sys_menu_add")
	@Operation(summary = "新增菜单", description = "新增菜单")
	public R saveMenu(@Valid @RequestBody SysMenu sysMenu) {
		sysMenuService.save(sysMenu);
		return R.ok(sysMenu);
	}

	/**
	 * 根据菜单ID删除菜单
	 * @param id 要删除的菜单ID
	 * @return 操作结果，成功返回success，失败返回false
	 */
	@SysLog("根据菜单ID删除菜单")
	@DeleteMapping("/{id}")
	@HasPermission("sys_menu_del")
	@Operation(summary = "根据菜单ID删除菜单", description = "根据菜单ID删除菜单")
	public R removeById(@PathVariable Long id) {
		return sysMenuService.removeMenuById(id);
	}

	/**
	 * 更新菜单
	 * @param sysMenu 菜单对象
	 * @return 操作结果
	 */
	@SysLog("更新菜单")
	@PutMapping
	@HasPermission("sys_menu_edit")
	@Operation(summary = "更新菜单", description = "更新菜单")
	public R updateMenu(@Valid @RequestBody SysMenu sysMenu) {
		return R.ok(sysMenuService.updateMenuById(sysMenu));
	}

}
