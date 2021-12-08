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

package com.pig4cloud.pigx.codegen.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.codegen.entity.GenFormConf;
import com.pig4cloud.pigx.codegen.service.GenFormConfService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 表单管理
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@RestController
@AllArgsConstructor
@RequestMapping("/form")
@Api(value = "form", tags = "表单管理")
public class GenFormConfController {

	private final GenFormConfService genRecordService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param formConf 生成记录
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getGenFormConfPage(Page page, GenFormConf formConf) {
		return R.ok(genRecordService.page(page, Wrappers.query(formConf)));
	}

	/**
	 * 通过id查询生成记录
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(genRecordService.getById(id));
	}

	/**
	 * 通过id查询生成记录
	 * @param dsName 数据源ID
	 * @param tableName tableName
	 * @return R
	 */
	@ApiOperation(value = "通过tableName查询表单信息")
	@GetMapping("/info")
	public R form(String dsName, String tableName) {
		return R.ok(genRecordService.getForm(dsName, tableName));
	}

	/**
	 * 新增生成记录
	 * @param formConf 生成记录
	 * @return R
	 */
	@ApiOperation(value = "新增生成记录", notes = "新增生成记录")
	@SysLog("新增生成记录")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('gen_form_add')")
	public R save(@RequestBody GenFormConf formConf) {
		return R.ok(genRecordService.save(formConf));
	}

	/**
	 * 修改生成记录
	 * @param formConf 生成记录
	 * @return R
	 */
	@ApiOperation(value = "修改生成记录", notes = "修改生成记录")
	@SysLog("修改生成记录")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('gen_form_edit')")
	public R updateById(@RequestBody GenFormConf formConf) {
		return R.ok(genRecordService.updateById(formConf));
	}

	/**
	 * 通过id删除生成记录
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除生成记录", notes = "通过id删除生成记录")
	@SysLog("通过id删除生成记录")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('gen_form_del')")
	public R removeById(@PathVariable Long id) {
		return R.ok(genRecordService.removeById(id));
	}

}
