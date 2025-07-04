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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenFieldType;
import com.pig4cloud.pig.codegen.service.GenFieldTypeService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 列属性管理控制器
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/fieldtype")
@Tag(description = "fieldtype", name = "列属性管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenFieldTypeController {

	private final GenFieldTypeService fieldTypeService;

	/**
	 * 分页查询字段类型
	 * @param page 分页对象
	 * @param fieldType 字段类型查询条件
	 * @return 分页查询结果
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getFieldTypePage(Page page, GenFieldType fieldType) {
		return R.ok(fieldTypeService.page(page,
				Wrappers.<GenFieldType>lambdaQuery()
					.like(StrUtil.isNotBlank(fieldType.getColumnType()), GenFieldType::getColumnType,
							fieldType.getColumnType())));
	}

	/**
	 * 查询列表
	 * @param fieldType 查询条件
	 * @return 包含查询结果的响应对象
	 */
	@Operation(summary = "查询列表", description = "查询列表")
	@GetMapping("/list")
	public R list(GenFieldType fieldType) {
		return R.ok(fieldTypeService.list(Wrappers.query(fieldType)));
	}

	/**
	 * 通过id查询列属性
	 * @param id 列属性id
	 * @return 包含查询结果的响应对象
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/details/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(fieldTypeService.getById(id));
	}

	/**
	 * 根据查询条件获取字段类型详情
	 * @param query 字段类型查询条件
	 * @return 包含查询结果的响应对象
	 */
	@GetMapping("/details")
	public R getDetails(GenFieldType query) {
		return R.ok(fieldTypeService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 新增列属性
	 * @param fieldType 列属性对象
	 * @return 操作结果
	 */
	@Operation(summary = "新增列属性", description = "新增列属性")
	@SysLog("新增列属性")
	@PostMapping
	public R save(@RequestBody GenFieldType fieldType) {
		return R.ok(fieldTypeService.save(fieldType));
	}

	/**
	 * 修改列属性
	 * @param fieldType 列属性对象
	 * @return 操作结果
	 */
	@Operation(summary = "修改列属性", description = "修改列属性")
	@SysLog("修改列属性")
	@PutMapping
	public R updateById(@RequestBody GenFieldType fieldType) {
		return R.ok(fieldTypeService.updateById(fieldType));
	}

	/**
	 * 通过id批量删除列属性
	 * @param ids 要删除的列属性id数组
	 * @return 操作结果
	 */
	@Operation(summary = "通过id删除列属性", description = "通过id删除列属性")
	@SysLog("通过id删除列属性")
	@DeleteMapping
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(fieldTypeService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel表格
	 * @param fieldType 查询条件
	 * @return excel文件数据列表
	 */
	@ResponseExcel
	@GetMapping("/export")
	public List<GenFieldType> export(GenFieldType fieldType) {
		return fieldTypeService.list(Wrappers.query(fieldType));
	}

}
