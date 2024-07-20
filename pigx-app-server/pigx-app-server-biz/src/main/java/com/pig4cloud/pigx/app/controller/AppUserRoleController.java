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
import com.pig4cloud.pigx.app.api.entity.AppUserRole;
import com.pig4cloud.pigx.app.service.AppUserRoleService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户角色表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appuserrole")
@Tag(description = "appuserrole", name = "用户角色表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppUserRoleController {

	private final AppUserRoleService appUserRoleService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param appUserRole 用户角色表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("app_appuserrole_view")
	public R getAppUserRolePage(Page page, AppUserRole appUserRole) {
		return R.ok(appUserRoleService.page(page, Wrappers.query(appUserRole)));
	}

	/**
	 * 通过id查询用户角色表
	 * @param userId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{userId}")
	@HasPermission("app_appuserrole_view")
	public R getById(@PathVariable("userId") Long userId) {
		return R.ok(appUserRoleService.getById(userId));
	}

	/**
	 * 新增用户角色表
	 * @param appUserRole 用户角色表
	 * @return R
	 */
	@Operation(summary = "新增用户角色表", description = "新增用户角色表")
	@SysLog("新增用户角色表")
	@PostMapping
	@HasPermission("app_appuserrole_add")
	public R save(@RequestBody AppUserRole appUserRole) {
		return R.ok(appUserRoleService.save(appUserRole));
	}

	/**
	 * 修改用户角色表
	 * @param appUserRole 用户角色表
	 * @return R
	 */
	@Operation(summary = "修改用户角色表", description = "修改用户角色表")
	@SysLog("修改用户角色表")
	@PutMapping
	@HasPermission("app_appuserrole_edit")
	public R updateById(@RequestBody AppUserRole appUserRole) {
		return R.ok(appUserRoleService.updateById(appUserRole));
	}

	/**
	 * 通过id删除用户角色表
	 * @param userId id
	 * @return R
	 */
	@Operation(summary = "通过id删除用户角色表", description = "通过id删除用户角色表")
	@SysLog("通过id删除用户角色表")
	@DeleteMapping("/{userId}")
	@HasPermission("app_appuserrole_del")
	public R removeById(@PathVariable Long userId) {
		return R.ok(appUserRoleService.removeById(userId));
	}

	/**
	 * 导出excel 表格
	 * @param appUserRole 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("app_appuserrole_export")
	public List<AppUserRole> export(AppUserRole appUserRole) {
		return appUserRoleService.list(Wrappers.query(appUserRole));
	}

}
