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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.service.GenGroupService;
import com.pig4cloud.pig.codegen.util.vo.GroupVO;
import com.pig4cloud.pig.codegen.util.vo.TemplateGroupDTO;
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
 * 模板分组
 *
 * @author PIG
 * @date 2023-02-21 20:01:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Tag(description = "group", name = "模板分组管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenGroupController {

	private final GenGroupService genGroupService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param genGroup 模板分组
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('codegen_group_view')")
	public R getgenGroupPage(Page page, GenGroupEntity genGroup) {
		LambdaQueryWrapper<GenGroupEntity> wrapper = Wrappers.<GenGroupEntity>lambdaQuery()
			.like(genGroup.getId() != null, GenGroupEntity::getId, genGroup.getId())
			.like(StrUtil.isNotEmpty(genGroup.getGroupName()), GenGroupEntity::getGroupName, genGroup.getGroupName());
		return R.ok(genGroupService.page(page, wrapper));
	}

	/**
	 * 通过id查询模板分组
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('codegen_group_view')")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(genGroupService.getGroupVoById(id));
	}

	/**
	 * 新增模板分组
	 * @param genTemplateGroup 模板分组
	 * @return R
	 */
	@Operation(summary = "新增模板分组", description = "新增模板分组")
	@SysLog("新增模板分组")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('codegen_group_add')")
	public R save(@RequestBody TemplateGroupDTO genTemplateGroup) {
		genGroupService.saveGenGroup(genTemplateGroup);
		return R.ok();
	}

	/**
	 * 修改模板分组
	 * @param groupVo 模板分组
	 * @return R
	 */
	@Operation(summary = "修改模板分组", description = "修改模板分组")
	@SysLog("修改模板分组")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('codegen_group_edit')")
	public R updateById(@RequestBody GroupVO groupVo) {
		genGroupService.updateGroupAndTemplateById(groupVo);
		return R.ok();
	}

	/**
	 * 通过id删除模板分组
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除模板分组", description = "通过id删除模板分组")
	@SysLog("通过id删除模板分组")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('codegen_group_del')")
	public R removeById(@RequestBody Long[] ids) {
		genGroupService.delGroupAndTemplate(ids);
		return R.ok();
	}

	/**
	 * 导出excel 表格
	 * @param genGroup 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('codegen_group_export')")
	public List<GenGroupEntity> export(GenGroupEntity genGroup) {
		return genGroupService.list(Wrappers.query(genGroup));
	}

	/**
	 * @return 响应信息主体
	 */
	@GetMapping("/list")
	@Operation(summary = "查询列表", description = "查询列表")
	public R list() {
		List<GenGroupEntity> list = genGroupService
			.list(Wrappers.<GenGroupEntity>lambdaQuery().orderByDesc(GenGroupEntity::getCreateTime));
		return R.ok(list);
	}

}
