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
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.app.api.dto.AppUserDTO;
import com.pig4cloud.pigx.app.api.vo.AppUserExcelVO;
import com.pig4cloud.pigx.common.core.exception.ErrorCodes;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.app.api.entity.AppUser;
import com.pig4cloud.pigx.app.service.AppUserService;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app用户表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appuser")
@Api(value = "appuser", tags = "app用户表管理")
public class AppUserController {

	private final AppUserService appUserService;

	@Inner
	@GetMapping("/info/{username}")
	public R info(@PathVariable String username) {
		AppUser user = appUserService.getOne(Wrappers.<AppUser>query().lambda().eq(AppUser::getUsername, username));
		if (user == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.APP_USER_USERINFO_EMPTY, username));
		}
		return R.ok(appUserService.findUserInfo(user));
	}

	/**
	 * 获取当前用户全部信息
	 * @return 用户信息
	 */
	@GetMapping(value = { "/info" })
	public R info() {
		String username = SecurityUtils.getUser().getUsername();
		AppUser user = appUserService.getOne(Wrappers.<AppUser>query().lambda().eq(AppUser::getUsername, username));
		if (user == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
		}
		return R.ok(appUserService.findUserInfo(user));
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param appUserDTO app用户表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getAppUserPage(Page page, AppUserDTO appUserDTO) {
		return R.ok(appUserService.getUsersWithRolePage(page, appUserDTO));
	}

	/**
	 * 通过id查询app用户表
	 * @param userId id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{userId}")
	public R getById(@PathVariable("userId") Long userId) {
		return R.ok(appUserService.getById(userId));
	}

	/**
	 * 新增app用户表
	 * @param appUser app用户表
	 * @return R
	 */
	@ApiOperation(value = "新增app用户表", notes = "新增app用户表")
	@SysLog("新增app用户表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('app_appuser_add')")
	public R save(@RequestBody AppUserDTO appUser) {
		return R.ok(appUserService.saveUser(appUser));
	}

	/**
	 * 修改app用户表
	 * @param appUser app用户表
	 * @return R
	 */
	@ApiOperation(value = "修改app用户表", notes = "修改app用户表")
	@SysLog("修改app用户表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('app_appuser_edit')")
	public R updateById(@RequestBody AppUserDTO appUser) {
		return R.ok(appUserService.updateUser(appUser));
	}

	/**
	 * 通过id删除app用户表
	 * @param userId id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除app用户表", notes = "通过id删除app用户表")
	@SysLog("通过id删除app用户表")
	@DeleteMapping("/{userId}")
	@PreAuthorize("@pms.hasPermission('app_appuser_del')")
	public R removeById(@PathVariable Long userId) {
		return R.ok(appUserService.deleteUserById(userId));
	}

	/**
	 * 导出excel 表格
	 * @param appUser 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('app_appuser_export')")
	public List<AppUserExcelVO> export(AppUserDTO appUser) {
		return appUserService.listUser(appUser);
	}

}
