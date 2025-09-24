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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.vo.RoleExcelVO;
import com.pig4cloud.pig.admin.api.vo.RoleVO;
import com.pig4cloud.pig.admin.service.SysRoleService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器：提供角色相关的增删改查及权限管理功能
 *
 * @author lengleng
 * @date 2025/05/30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Tag(description = "role", name = "角色管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysRoleController {

	private final SysRoleService sysRoleService;

	/**
	 * 通过ID查询角色信息
	 * @param id 角色ID
	 * @return 包含角色信息的响应对象
	 */
	@GetMapping("/details/{id}")
	@Operation(summary = "通过ID查询角色信息", description = "通过ID查询角色信息")
	public R getById(@PathVariable Long id) {
		return R.ok(sysRoleService.getById(id));
	}

	/**
	 * 查询角色详细信息
	 * @param query 角色查询条件对象
	 * @return 包含角色信息的响应结果
	 */
	@GetMapping("/details")
	@Operation(summary = "查询角色详细信息", description = "查询角色详细信息")
	public R getDetails(@ParameterObject SysRole query) {
		return R.ok(sysRoleService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 添加角色
	 * @param sysRole 角色信息
	 * @return 操作结果，成功返回success，失败返回false
	 */
	@SysLog("添加角色")
	@PostMapping
	@HasPermission("sys_role_add")
	@Operation(summary = "添加角色", description = "添加角色")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public R saveRole(@Valid @RequestBody SysRole sysRole) {
		return R.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色信息
	 * @param sysRole 角色信息
	 * @return 操作结果，成功返回success，失败返回false
	 */
	@SysLog("修改角色信息")
	@PutMapping
	@HasPermission("sys_role_edit")
	@Operation(summary = "修改角色信息", description = "修改角色信息")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public R updateRole(@Valid @RequestBody SysRole sysRole) {
		return R.ok(sysRoleService.updateById(sysRole));
	}

	/**
	 * 根据ID数组删除角色
	 * @param ids 角色ID数组
	 * @return 操作结果
	 */
	@SysLog("删除角色")
	@DeleteMapping
	@HasPermission("sys_role_del")
	@Operation(summary = "根据ID数组删除角色", description = "根据ID数组删除角色")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(sysRoleService.removeRoleByIds(ids));
	}

	/**
	 * 获取角色列表
	 * @return 包含角色列表的响应结果
	 */
	@GetMapping("/list")
	@Operation(summary = "获取角色列表", description = "获取角色列表")
	public R listRoles() {
		return R.ok(sysRoleService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询角色信息
	 * @param page 分页对象
	 * @param role 查询条件对象
	 * @return 包含分页结果的响应对象
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询角色信息", description = "分页查询角色信息")
	public R getRolePage(Page page, SysRole role) {
		return R.ok(sysRoleService.page(page, Wrappers.<SysRole>lambdaQuery()
			.like(StrUtil.isNotBlank(role.getRoleName()), SysRole::getRoleName, role.getRoleName())));
	}

	/**
	 * 更新角色菜单
	 * @param roleVo 角色VO对象
	 * @return 操作结果，成功返回success，失败返回false
	 */
	@SysLog("更新角色菜单")
	@PutMapping("/menu")
	@HasPermission("sys_role_perm")
	@Operation(summary = "更新角色菜单", description = "更新角色菜单")
	public R saveRoleMenus(@RequestBody RoleVO roleVo) {
		return R.ok(sysRoleService.updateRoleMenus(roleVo));
	}

	/**
	 * 通过角色ID列表查询角色信息
	 * @param roleIdList 角色ID列表
	 * @return 包含查询结果的响应对象
	 */
	@PostMapping("/getRoleList")
	@Operation(summary = "通过角色ID列表查询角色信息", description = "通过角色ID列表查询角色信息")
	public R getRoleList(@RequestBody List<Long> roleIdList) {
		return R.ok(sysRoleService.listRolesByRoleIds(roleIdList, CollUtil.join(roleIdList, StrUtil.UNDERLINE)));
	}

	/**
	 * 导出角色数据到Excel表格
	 * @return 角色数据列表
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_role_export")
	@Operation(summary = "导出角色数据到Excel表格", description = "导出角色数据到Excel表格")
	public List<RoleExcelVO> exportRoles() {
		return sysRoleService.listRoles();
	}

	/**
	 * 导入角色
	 * @param excelVOList 角色Excel数据列表
	 * @param bindingResult 数据校验结果
	 * @return 导入结果
	 */
	@PostMapping("/import")
	@HasPermission("sys_role_export")
	@Operation(summary = "导入角色数据", description = "导入角色数据")
	public R importRole(@RequestExcel List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
		return sysRoleService.importRole(excelVOList, bindingResult);
	}

}
