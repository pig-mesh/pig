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

package com.pig4cloud.pigx.app.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.app.api.entity.AppRoleMenu;
import com.pig4cloud.pigx.app.service.AppRoleMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色菜单表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/approlemenu")
@Api(value = "approlemenu", tags = "角色菜单表管理")
public class AppRoleMenuController {

	private final AppRoleMenuService appRoleMenuService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param appRoleMenu 角色菜单表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('app_approlemenu_view')")
	public R getAppRoleMenuPage(Page page, AppRoleMenu appRoleMenu) {
		return R.ok(appRoleMenuService.page(page, Wrappers.query(appRoleMenu)));
	}

	/**
	 * 通过id查询角色菜单表
	 * @param roleId id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{roleId}")
	@PreAuthorize("@pms.hasPermission('app_approlemenu_view')")
	public R getById(@PathVariable("roleId") Long roleId) {
		return R.ok(appRoleMenuService.getById(roleId));
	}

	/**
	 * 新增角色菜单表
	 * @param appRoleMenu 角色菜单表
	 * @return R
	 */
	@ApiOperation(value = "新增角色菜单表", notes = "新增角色菜单表")
	@SysLog("新增角色菜单表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('app_approlemenu_add')")
	public R save(@RequestBody AppRoleMenu appRoleMenu) {
		return R.ok(appRoleMenuService.save(appRoleMenu));
	}

	/**
	 * 修改角色菜单表
	 * @param appRoleMenu 角色菜单表
	 * @return R
	 */
	@ApiOperation(value = "修改角色菜单表", notes = "修改角色菜单表")
	@SysLog("修改角色菜单表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('app_approlemenu_edit')")
	public R updateById(@RequestBody AppRoleMenu appRoleMenu) {
		return R.ok(appRoleMenuService.updateById(appRoleMenu));
	}

	/**
	 * 通过id删除角色菜单表
	 * @param roleId id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除角色菜单表", notes = "通过id删除角色菜单表")
	@SysLog("通过id删除角色菜单表")
	@DeleteMapping("/{roleId}")
	@PreAuthorize("@pms.hasPermission('app_approlemenu_del')")
	public R removeById(@PathVariable Long roleId) {
		return R.ok(appRoleMenuService.removeById(roleId));
	}

	/**
	 * 导出excel 表格
	 * @param appRoleMenu 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('app_approlemenu_export')")
	public List<AppRoleMenu> export(AppRoleMenu appRoleMenu) {
		return appRoleMenuService.list(Wrappers.query(appRoleMenu));
	}

}
