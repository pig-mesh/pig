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

package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysI18nEntity;
import com.pig4cloud.pigx.admin.service.SysI18nService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统表-国际化
 *
 * @author PIG
 * @date 2023-02-14 09:07:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/i18n")
@Tag(description = "i18n", name = "系统表-国际化管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysI18nController {

	private final SysI18nService sysI18nService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysI18n 系统表-国际化
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("sys_i18n_view")
	public R getsysI18nPage(@ParameterObject Page page, @ParameterObject SysI18nEntity sysI18n) {
		LambdaQueryWrapper<SysI18nEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(sysI18n.getName()), SysI18nEntity::getName, sysI18n.getName());
		wrapper.like(StrUtil.isNotBlank(sysI18n.getZhCn()), SysI18nEntity::getZhCn, sysI18n.getZhCn());
		wrapper.like(StrUtil.isNotBlank(sysI18n.getEn()), SysI18nEntity::getEn, sysI18n.getEn());
		return R.ok(sysI18nService.page(page, wrapper));
	}

	/**
	 * 通过id查询系统表-国际化
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/details/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(sysI18nService.getById(id));
	}

	@GetMapping("/details")
	public R getDetails(@ParameterObject SysI18nEntity entity) {
		return R.ok(sysI18nService.getOne(Wrappers.query(entity)));
	}

	/**
	 * 新增系统表-国际化
	 * @param sysI18n 系统表-国际化
	 * @return R
	 */
	@Operation(summary = "新增系统表-国际化", description = "新增系统表-国际化")
	@SysLog("新增系统表-国际化")
	@PostMapping
	@CacheEvict(value = CacheConstants.I18N_DETAILS, allEntries = true)
	@HasPermission("sys_i18n_add")
	public R save(@RequestBody SysI18nEntity sysI18n) {
		return R.ok(sysI18nService.save(sysI18n));
	}

	/**
	 * 修改系统表-国际化
	 * @param sysI18n 系统表-国际化
	 * @return R
	 */
	@Operation(summary = "修改系统表-国际化", description = "修改系统表-国际化")
	@SysLog("修改系统表-国际化")
	@PutMapping
	@CacheEvict(value = CacheConstants.I18N_DETAILS, allEntries = true)
	@HasPermission("sys_i18n_edit")
	public R updateById(@RequestBody SysI18nEntity sysI18n) {
		return R.ok(sysI18nService.updateById(sysI18n));
	}

	/**
	 * 通过id删除系统表-国际化
	 * @param ids id 列表
	 * @return R
	 */
	@Operation(summary = "通过id删除系统表-国际化", description = "通过id删除系统表-国际化")
	@SysLog("通过id删除系统表-国际化")
	@DeleteMapping
	@CacheEvict(value = CacheConstants.I18N_DETAILS, allEntries = true)
	@HasPermission("sys_i18n_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(sysI18nService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param sysI18n 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_i18n_export")
	public List<SysI18nEntity> export(SysI18nEntity sysI18n, Long[] ids) {
		return sysI18nService
			.list(Wrappers.lambdaQuery(sysI18n).in(ArrayUtil.isNotEmpty(ids), SysI18nEntity::getId, ids));
	}

	/**
	 * 获取系统I18N配置
	 * @return I18N 配置
	 */
	@Operation(summary = "获取系统配置-国际化", description = "获取系统配置-国际化")
	@Inner(false)
	@GetMapping("/info")
	public R list() {
		return R.ok(sysI18nService.listMap());
	}

	/**
	 * 同步数据
	 * @return R
	 */
	@SysLog("同步数据")
	@PutMapping("/sync")
	public R sync() {
		return sysI18nService.syncI18nCache();
	}

}
