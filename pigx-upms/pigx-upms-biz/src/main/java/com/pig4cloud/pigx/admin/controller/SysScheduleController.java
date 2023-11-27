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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysScheduleEntity;
import com.pig4cloud.pigx.admin.service.SysScheduleService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 日程
 *
 * @author aeizzz
 * @date 2023-03-06 14:26:23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Tag(description = "schedule", name = "日程管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysScheduleController {

	private final SysScheduleService sysScheduleService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysSchedule 日程
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getSchedulePage(@ParameterObject Page page, @ParameterObject SysScheduleEntity sysSchedule) {
		return R.ok(sysScheduleService.getScheduleByScope(page, sysSchedule));
	}

	/**
	 * 通过id查询日程
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(sysScheduleService.getById(id));
	}

	/**
	 * 新增日程
	 * @param sysSchedule 日程
	 * @return R
	 */
	@Operation(summary = "新增日程", description = "新增日程")
	@SysLog("新增日程")
	@PostMapping
	public R save(@RequestBody SysScheduleEntity sysSchedule) {
		return R.ok(sysScheduleService.save(sysSchedule));
	}

	/**
	 * 修改日程
	 * @param sysSchedule 日程
	 * @return R
	 */
	@Operation(summary = "修改日程", description = "修改日程")
	@SysLog("修改日程")
	@PutMapping
	public R updateById(@RequestBody SysScheduleEntity sysSchedule) {
		return R.ok(sysScheduleService.updateById(sysSchedule));
	}

	/**
	 * 通过id删除日程
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除日程", description = "通过id删除日程")
	@SysLog("通过id删除日程")
	@DeleteMapping
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(sysScheduleService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param sysSchedule 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	public List<SysScheduleEntity> export(SysScheduleEntity sysSchedule) {
		return sysScheduleService.list(Wrappers.query(sysSchedule));
	}

	@Operation(summary = "列表查询", description = "列表查询")
	@GetMapping("/list")
	public R list(@RequestParam(required = false) LocalDate startDate,
			@RequestParam(required = false) LocalDate endDate) {
		List<SysScheduleEntity> list = sysScheduleService.selectListByScope(startDate, endDate);
		return R.ok(list);
	}

}
