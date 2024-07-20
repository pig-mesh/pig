package com.pig4cloud.pigx.codegen.controller;
/*
 *      Copyright (c) 2018-2025, luolin All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: luolin (766488893@qq.com)
 */

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.codegen.entity.GenCreateTable;
import com.pig4cloud.pigx.codegen.service.GenCreateTableService;
import com.pig4cloud.pigx.codegen.util.vo.GenCreateTableVO;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自动创建表管理
 *
 * @author luolin
 * @date 2022-09-23 21:56:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/create-table")
@Tag(description = "create-table", name = "自动创建表管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenCreateTableController {

	private final GenCreateTableService createTableService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param createTable 自动创建表管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute GenCreateTable createTable,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<GenCreateTable> query = Wrappers.query(createTable);
		return R.ok(createTableService.page(page, query));
	}

	/**
	 * 通过id查询自动创建表管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(createTableService.getById(id));
	}

	/**
	 * 新增自动创建表管理
	 * @param createTableVO 自动创建表管理
	 * @return R
	 */
	@Operation(summary = "新增自动创建表管理", description = "新增自动创建表管理")
	@PostMapping
	@HasPermission("codegen_table_add")
	public R save(@RequestBody GenCreateTableVO createTableVO) {
		// 切换目标数据源
		DynamicDataSourceContextHolder.push(createTableVO.getDsName());
		createTableService.createTable(createTableVO);
		GenCreateTable createTable = new GenCreateTable();
		BeanUtil.copyProperties(createTableVO, createTable);
		DynamicDataSourceContextHolder.clear();
		return R.ok(createTableService.saveOrUpdate(createTable));
	}

	/**
	 * 修改自动创建表管理
	 * @param createTable 自动创建表管理
	 * @return R
	 */
	@Operation(summary = "修改自动创建表管理", description = "修改自动创建表管理")
	@PutMapping
	@HasPermission("codegen_table_add")
	public R updateById(@RequestBody GenCreateTable createTable) {
		return R.ok(createTableService.updateById(createTable));
	}

	/**
	 * 通过id删除自动创建表管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除自动创建表管理", description = "通过id删除自动创建表管理")
	@DeleteMapping("/{id}")
	@HasPermission("codegen_table_add")
	public R removeById(@PathVariable Long id) {
		return R.ok(createTableService.removeById(id));
	}

	/**
	 * 查询租户所有自动创建表管理
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(createTableService.list());
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	@HasPermission("codegen_table_add")
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(createTableService.removeByIds(ids));
	}

}
