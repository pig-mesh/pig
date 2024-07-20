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
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysTenant;
import com.pig4cloud.pigx.admin.service.SysMenuService;
import com.pig4cloud.pigx.admin.service.SysTenantService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.data.tenant.TenantBroker;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户管理
 *
 * @author lengleng
 * @date 2019-05-15 15:55:41
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant")
@Tag(description = "tenant", name = "租户管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTenantController {

	private final SysTenantService sysTenantService;

	private final SysMenuService sysMenuService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysTenant 租户
	 * @return
	 */
	@GetMapping("/page")
	public R getSysTenantPage(@ParameterObject Page page, @ParameterObject SysTenant sysTenant) {
		return R.ok(sysTenantService.page(page, Wrappers.<SysTenant>lambdaQuery()
			.like(StrUtil.isNotBlank(sysTenant.getName()), SysTenant::getName, sysTenant.getName())));
	}

	/**
	 * 通过ID 查询租户信息
	 * @param id ID
	 * @return R
	 */
	@GetMapping("/details/{id}")
	public R getById(@PathVariable Long id) {
		return R.ok(sysTenantService.getById(id));
	}

	/**
	 * 查询租户信息
	 * @param query 查询条件
	 * @return 租户信息
	 */
	@GetMapping("/details")
	public R getDetails(@ParameterObject SysTenant query) {
		return R.ok(sysTenantService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 新增租户
	 * @param sysTenant 租户
	 * @return R
	 */
	@SysLog("新增租户")
	@PostMapping
	@HasPermission("sys_systenant_add")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public R save(@RequestBody SysTenant sysTenant) {
		return R.ok(sysTenantService.saveTenant(sysTenant));
	}

	/**
	 * 修改租户
	 * @param sysTenant 租户
	 * @return R
	 */
	@SysLog("修改租户")
	@PutMapping
	@HasPermission("sys_systenant_edit")
	public R updateById(@RequestBody SysTenant sysTenant) {
		return R.ok(sysTenantService.updateTenant(sysTenant));
	}

	/**
	 * 通过id删除租户
	 * <p>
	 * 为了保证安全,这里只删除租户表的数据，不删除基础表中的租户初始化数据。
	 * @param ids id 列表
	 * @return R
	 */
	@SysLog("删除租户")
	@DeleteMapping
	@HasPermission("sys_systenant_del")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(sysTenantService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 查询全部有效的租户
	 * @return
	 */
	@Inner(value = false)
	@GetMapping("/list")
	public R list() {
		List<SysTenant> tenants = sysTenantService.getNormalTenant()
			.stream()
			.filter(tenant -> tenant.getStartTime().isBefore(LocalDateTime.now()))
			.filter(tenant -> tenant.getEndTime().isAfter(LocalDateTime.now()))
			.collect(Collectors.toList());
		return R.ok(tenants);
	}

	/**
	 * 导出excel 表格
	 * @return
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_systenant_export")
	public List<SysTenant> export(SysTenant sysTenant, Long[] ids) {
		return sysTenantService
			.list(Wrappers.lambdaQuery(sysTenant).in(ArrayUtil.isNotEmpty(ids), SysTenant::getId, ids));
	}

	@GetMapping(value = "/tree/menu")
	public R getTree() {
		Long defaultId = ParamResolver.getLong("TENANT_DEFAULT_ID", 1L);
		List<Tree<Long>> trees = new ArrayList<>();
		TenantBroker.runAs(defaultId, (id) -> {
			trees.addAll(sysMenuService.treeMenu(null, null, null));
		});

		return R.ok(trees);
	}

}
