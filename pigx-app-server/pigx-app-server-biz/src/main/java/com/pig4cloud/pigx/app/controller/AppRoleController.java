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
import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.app.api.vo.AppRoleVO;
import com.pig4cloud.pigx.app.service.AppRoleService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app角色表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/approle")
@Tag(description  = "approle", name =  "app角色表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppRoleController {

	private final AppRoleService appRoleService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param appRole app角色表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getAppRolePage(Page page, AppRole appRole) {
		return R.ok(appRoleService.page(page, Wrappers.query(appRole)));
	}

	/**
	 * 查询全部角色
	 * @return
	 */
	@Operation(summary = "查询全部", description = "查询全部")
	@GetMapping("/list")
	public R list() {
		return R.ok(appRoleService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 通过id查询app角色表
	 * @param roleId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{roleId}")
	@PreAuthorize("@pms.hasPermission('app_approle_view')")
	public R getById(@PathVariable("roleId") Long roleId) {
		return R.ok(appRoleService.getById(roleId));
	}

	/**
	 * 新增app角色表
	 * @param appRole app角色表
	 * @return R
	 */
	@Operation(summary = "新增app角色表", description = "新增app角色表")
	@SysLog("新增app角色表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('app_approle_add')")
	public R save(@RequestBody AppRole appRole) {
		return R.ok(appRoleService.save(appRole));
	}

	/**
	 * 修改app角色表
	 * @param appRole app角色表
	 * @return R
	 */
	@Operation(summary = "修改app角色表", description = "修改app角色表")
	@SysLog("修改app角色表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('app_approle_edit')")
	public R updateById(@RequestBody AppRole appRole) {
		return R.ok(appRoleService.updateById(appRole));
	}

	/**
	 * 通过id删除app角色表
	 * @param roleId id
	 * @return R
	 */
	@Operation(summary = "通过id删除app角色表", description = "通过id删除app角色表")
	@SysLog("通过id删除app角色表")
	@DeleteMapping("/{roleId}")
	@PreAuthorize("@pms.hasPermission('app_approle_del')")
	public R removeById(@PathVariable Long roleId) {
		return R.ok(appRoleService.removeById(roleId));
	}

	/**
	 * 更新角色菜单
	 * @param roleVo 角色对象
	 * @return success、false
	 */
	@SysLog("更新角色菜单")
	@PutMapping("/menu")
	@PreAuthorize("@pms.hasPermission('app_approle_perm')")
	public R saveRoleMenus(@RequestBody AppRoleVO roleVo) {
		return R.ok(appRoleService.updateRoleMenus(roleVo));
	}

	/**
	 * 导出excel 表格
	 * @param appRole 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('app_approle_export')")
	public List<AppRole> export(AppRole appRole) {
		return appRoleService.list(Wrappers.query(appRole));
	}

}
