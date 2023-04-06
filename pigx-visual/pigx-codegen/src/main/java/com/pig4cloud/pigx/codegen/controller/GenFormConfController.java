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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 表单管理
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/form")
@Tag(description = "form", name = "表单管理")
public class GenFormConfController {

	private final GenFormConfService genRecordService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param formConf 生成记录
	 * @return
	 */
	@Operation(description = "分页查询", summary = "分页查询")
	@GetMapping("/page")
	public R getGenFormConfPage(Page page, GenFormConf formConf) {
		return R.ok(genRecordService.page(page, Wrappers.query(formConf)));
	}

	/**
	 * 通过id查询生成记录
	 * @param id id
	 * @return R
	 */
	@Operation(description = "通过id查询", summary = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(genRecordService.getById(id));
	}

	/**
	 * 新增生成记录
	 * @param formConf 生成记录
	 * @return R
	 */
	@Operation(description = "新增生成记录", summary = "新增生成记录")
	@PostMapping
	public R save(@RequestBody GenFormConf formConf) {
		genRecordService.save(formConf);
		return R.ok(formConf);
	}

	/**
	 * 修改生成记录
	 * @param formConf 生成记录
	 * @return R
	 */
	@Operation(description = "修改生成记录", summary = "修改生成记录")
	@SysLog("修改生成记录")
	@PutMapping
	public R updateById(@RequestBody GenFormConf formConf) {
		return R.ok(genRecordService.updateById(formConf));
	}

	/**
	 * 通过id删除生成记录
	 * @param id id
	 * @return R
	 */
	@Operation(description = "通过id删除生成记录", summary = "通过id删除生成记录")
	@SysLog("通过id删除生成记录")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(genRecordService.removeById(id));
	}

}
