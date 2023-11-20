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

package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenTemplateGroupEntity;
import com.pig4cloud.pig.codegen.service.GenTemplateGroupService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板分组关联表
 *
 * @author PIG
 * @date 2023-02-22 09:25:15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/templateGroup")
@Tag(description = "templateGroup", name = "模板分组关联表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTemplateGroupController {

	private final GenTemplateGroupService genTemplateGroupService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param genTemplateGroup 模板分组关联表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('codegen_templateGroup_view')")
	public R getgenTemplateGroupPage(Page page, GenTemplateGroupEntity genTemplateGroup) {
		LambdaQueryWrapper<GenTemplateGroupEntity> wrapper = Wrappers.lambdaQuery();
		return R.ok(genTemplateGroupService.page(page, wrapper));
	}

	/**
	 * 通过id查询模板分组关联表
	 * @param groupId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{groupId}")
	@PreAuthorize("@pms.hasPermission('codegen_templateGroup_view')")
	public R getById(@PathVariable("groupId") Long groupId) {
		return R.ok(genTemplateGroupService.getById(groupId));
	}

	/**
	 * 新增模板分组关联表
	 * @param genTemplateGroup 模板分组关联表
	 * @return R
	 */
	@Operation(summary = "新增模板分组关联表", description = "新增模板分组关联表")
	@SysLog("新增模板分组关联表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('codegen_templateGroup_add')")
	public R save(@RequestBody GenTemplateGroupEntity genTemplateGroup) {
		return R.ok(genTemplateGroupService.save(genTemplateGroup));
	}

	/**
	 * 修改模板分组关联表
	 * @param genTemplateGroup 模板分组关联表
	 * @return R
	 */
	@Operation(summary = "修改模板分组关联表", description = "修改模板分组关联表")
	@SysLog("修改模板分组关联表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('codegen_templateGroup_edit')")
	public R updateById(@RequestBody GenTemplateGroupEntity genTemplateGroup) {
		return R.ok(genTemplateGroupService.updateById(genTemplateGroup));
	}

	/**
	 * 通过id删除模板分组关联表
	 * @param ids groupId列表
	 * @return R
	 */
	@Operation(summary = "通过id删除模板分组关联表", description = "通过id删除模板分组关联表")
	@SysLog("通过id删除模板分组关联表")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('codegen_templateGroup_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(genTemplateGroupService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param genTemplateGroup 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('codegen_templateGroup_export')")
	public List<GenTemplateGroupEntity> export(GenTemplateGroupEntity genTemplateGroup) {
		return genTemplateGroupService.list(Wrappers.query(genTemplateGroup));
	}

}
