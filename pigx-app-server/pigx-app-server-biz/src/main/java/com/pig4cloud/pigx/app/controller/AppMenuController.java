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
import com.pig4cloud.pigx.app.api.entity.AppMenu;
import com.pig4cloud.pigx.app.service.AppMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appmenu")
@Api(value = "appmenu", tags = "菜单权限表管理")
public class AppMenuController {

	private final AppMenuService appMenuService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param appMenu 菜单权限表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('app_appmenu_view')")
	public R getAppMenuPage(Page page, AppMenu appMenu) {
		return R.ok(appMenuService.page(page, Wrappers.query(appMenu)));
	}

	/**
	 * 通过id查询菜单权限表
	 * @param menuId id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{menuId}")
	@PreAuthorize("@pms.hasPermission('app_appmenu_view')")
	public R getById(@PathVariable("menuId") Long menuId) {
		return R.ok(appMenuService.getById(menuId));
	}

	/**
	 * 新增菜单权限表
	 * @param appMenu 菜单权限表
	 * @return R
	 */
	@ApiOperation(value = "新增菜单权限表", notes = "新增菜单权限表")
	@SysLog("新增菜单权限表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('app_appmenu_add')")
	public R save(@RequestBody AppMenu appMenu) {
		return R.ok(appMenuService.save(appMenu));
	}

	/**
	 * 修改菜单权限表
	 * @param appMenu 菜单权限表
	 * @return R
	 */
	@ApiOperation(value = "修改菜单权限表", notes = "修改菜单权限表")
	@SysLog("修改菜单权限表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('app_appmenu_edit')")
	public R updateById(@RequestBody AppMenu appMenu) {
		return R.ok(appMenuService.updateById(appMenu));
	}

	/**
	 * 通过id删除菜单权限表
	 * @param menuId id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除菜单权限表", notes = "通过id删除菜单权限表")
	@SysLog("通过id删除菜单权限表")
	@DeleteMapping("/{menuId}")
	@PreAuthorize("@pms.hasPermission('app_appmenu_del')")
	public R removeById(@PathVariable Long menuId) {
		return R.ok(appMenuService.removeById(menuId));
	}

	/**
	 * 导出excel 表格
	 * @param appMenu 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('app_appmenu_export')")
	public List<AppMenu> export(AppMenu appMenu) {
		return appMenuService.list(Wrappers.query(appMenu));
	}

}
