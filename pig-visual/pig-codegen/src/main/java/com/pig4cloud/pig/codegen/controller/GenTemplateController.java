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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pig.codegen.service.GenTemplateService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.xss.core.XssCleanIgnore;
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
 * 模板
 *
 * @author PIG
 * @date 2023-02-21 17:15:44
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
@Tag(description = "template", name = "模板管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTemplateController {

	private final GenTemplateService genTemplateService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param genTemplate 模板
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('codegen_template_view')")
	public R getGenTemplatePage(Page page, GenTemplateEntity genTemplate) {
		LambdaQueryWrapper<GenTemplateEntity> wrapper = Wrappers.<GenTemplateEntity>lambdaQuery()
			.like(genTemplate.getId() != null, GenTemplateEntity::getId, genTemplate.getId())
			.like(StrUtil.isNotEmpty(genTemplate.getTemplateName()), GenTemplateEntity::getTemplateName,
					genTemplate.getTemplateName());
		return R.ok(genTemplateService.page(page, wrapper));
	}

	/**
	 * 查询全部模板
	 * @return
	 */
	@Operation(summary = "查询全部", description = "查询全部")
	@GetMapping("/list")
	@PreAuthorize("@pms.hasPermission('codegen_template_view')")
	public R list() {
		return R.ok(genTemplateService
			.list(Wrappers.<GenTemplateEntity>lambdaQuery().orderByDesc(GenTemplateEntity::getCreateTime)));
	}

	/**
	 * 通过id查询模板
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('codegen_template_view')")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(genTemplateService.getById(id));
	}

	/**
	 * 新增模板
	 * @param genTemplate 模板
	 * @return R
	 */
	@XssCleanIgnore
	@Operation(summary = "新增模板", description = "新增模板")
	@SysLog("新增模板")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('codegen_template_add')")
	public R save(@RequestBody GenTemplateEntity genTemplate) {
		return R.ok(genTemplateService.save(genTemplate));
	}

	/**
	 * 修改模板
	 * @param genTemplate 模板
	 * @return R
	 */
	@XssCleanIgnore
	@Operation(summary = "修改模板", description = "修改模板")
	@SysLog("修改模板")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('codegen_template_edit')")
	public R updateById(@RequestBody GenTemplateEntity genTemplate) {
		return R.ok(genTemplateService.updateById(genTemplate));
	}

	/**
	 * 通过id删除模板
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除模板", description = "通过id删除模板")
	@SysLog("通过id删除模板")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('codegen_template_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(genTemplateService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param genTemplate 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('codegen_template_export')")
	public List<GenTemplateEntity> export(GenTemplateEntity genTemplate) {
		return genTemplateService.list(Wrappers.query(genTemplate));
	}

	/**
	 * 在线更新模板
	 * @return R
	 */
	@Operation(summary = "在线更新模板", description = "在线更新模板")
	@GetMapping("/online")
	@PreAuthorize("@pms.hasPermission('codegen_template_view')")
	public R online() {
		return genTemplateService.onlineUpdate();
	}

	/**
	 * 检查版本
	 * @return {@link R }
	 */
	@Operation(summary = "在线检查模板", description = "在线检查模板")
	@GetMapping("/checkVersion")
	@PreAuthorize("@pms.hasPermission('codegen_template_view')")
	public R checkVersion() {
		return genTemplateService.checkVersion();
	}

}
