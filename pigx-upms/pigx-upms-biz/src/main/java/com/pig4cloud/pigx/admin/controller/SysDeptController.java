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

package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.vo.DeptExcelVO;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.RequestExcel;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Tag(description = "dept", name = "部门管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysDeptController {

	private final SysDeptService sysDeptService;

	/**
	 * 通过ID查询
	 * @param id ID
	 * @return SysDept
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Long id) {
		return R.ok(sysDeptService.getById(id));
	}

	/**
	 * 查询全部部门
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(sysDeptService.list());
	}

	/**
	 * 返回树形菜单集合
	 * @param deptName 部门名称
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	public R getTree(String deptName, Long parentId) {
		return R.ok(sysDeptService.selectTree(deptName, parentId));
	}

	/**
	 * 添加
	 * @param sysDept 实体
	 * @return success/false
	 */
	@SysLog("添加部门")
	@PostMapping
	@HasPermission("sys_dept_add")
	public R save(@Valid @RequestBody SysDept sysDept) {
		return R.ok(sysDeptService.save(sysDept));
	}

	/**
	 * 删除
	 * @param id ID
	 * @return success/false
	 */
	@SysLog("删除部门")
	@DeleteMapping("/{id}")
	@HasPermission("sys_dept_del")
	public R removeById(@PathVariable Long id) {
		return R.ok(sysDeptService.removeDeptById(id));
	}

	/**
	 * 编辑
	 * @param sysDept 实体
	 * @return success/false
	 */
	@SysLog("编辑部门")
	@PutMapping
	@HasPermission("sys_dept_edit")
	public R update(@Valid @RequestBody SysDept sysDept) {
		sysDept.setUpdateTime(LocalDateTime.now());
		return R.ok(sysDeptService.updateById(sysDept));
	}

	/**
	 * 查收子级列表
	 * @return 返回子级
	 */
	@GetMapping(value = "/getDescendantList/{deptId}")
	public R getDescendantList(@PathVariable Long deptId) {
		return R.ok(sysDeptService.listDescendant(deptId));
	}

	@GetMapping(value = "/leader/{deptId}")
	public R getAllDeptLeader(@PathVariable Long deptId) {
		return R.ok(sysDeptService.listDeptLeader(deptId));
	}

	/**
	 * 导出部门
	 * @return
	 */
	@ResponseExcel
	@GetMapping("/export")
	public List<DeptExcelVO> export() {
		return sysDeptService.listExcelVo();
	}

	/**
	 * 导入部门
	 * @param excelVOList
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("import")
	public R importDept(@RequestExcel List<DeptExcelVO> excelVOList, BindingResult bindingResult) {
		return sysDeptService.importDept(excelVOList, bindingResult);
	}

	/**
	 * 查询全部部门包含用户
	 * @param parentDeptId 父部门ID
	 * @param type 查询类型
	 */
	@GetMapping("/org")
	public R listOrgTree(Long parentDeptId, String type) {
		return R.ok(sysDeptService.listOrgTree(parentDeptId, type));
	}

	/**
	 * 模糊搜索用户
	 * @param username 用户名/拼音/首字母
	 * @return 匹配到的用户
	 */
	@GetMapping("/org/user/search")
	public R getOrgTreeUser(@RequestParam String username) {
		return R.ok(sysDeptService.getOrgTreeUser(username));
	}

}
