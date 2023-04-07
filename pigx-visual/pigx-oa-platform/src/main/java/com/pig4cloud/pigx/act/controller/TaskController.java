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

package com.pig4cloud.pigx.act.controller;

import cn.hutool.core.io.IoUtil;
import com.pig4cloud.pigx.act.dto.LeaveBillDto;
import com.pig4cloud.pigx.act.dto.TaskDTO;
import com.pig4cloud.pigx.act.service.ActTaskService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.common.xss.core.XssCleanIgnore;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

	private final ActTaskService actTaskService;

	@GetMapping("/todo")
	@Operation(summary = "条件查询", description = "条件查询")
	public R todo(@RequestParam Map<String, Object> params) {
		return R.ok(actTaskService.getTaskByName(params, SecurityUtils.getUser().getUsername()));
	}

	@GetMapping("/{id}")
	@Operation(summary = "id查询", description = "id查询")
	public R getTaskById(@PathVariable String id) {
		return R.ok(actTaskService.getTaskById(id));
	}

	@PostMapping
	@XssCleanIgnore
	@Operation(summary = "新增", description = "新增")
	@PreAuthorize("@pms.hasPermission('oa_task_add')")
	public R submitTask(@RequestBody LeaveBillDto leaveBillDto) {
		return R.ok(actTaskService.submitTask(leaveBillDto));
	}

	@GetMapping("/view/{id}")
	@Operation(summary = "查询", description = "查询")
	public ResponseEntity viewCurrentImage(@PathVariable String id) {
		InputStream imageStream = actTaskService.viewByTaskId(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity(IoUtil.readBytes(imageStream), headers, HttpStatus.CREATED);
	}

	@GetMapping("/comment/{id}")
	@Operation(summary = "查询", description = "查询")
	@PreAuthorize("@pms.hasPermission('oa_task_view')")
	public R commitList(@PathVariable String id) {
		return R.ok(actTaskService.getCommentByTaskId(id));
	}

	@DeleteMapping
	@Operation(summary = "删除", description = "删除")
	@PreAuthorize("@pms.hasPermission('oa_task_del')")
	public R submitTask(@RequestBody String[] ids) {
		actTaskService.delTasks(ids);
		return R.ok();
	}

	@ResponseExcel
	@GetMapping("/export")
	@Operation(summary = "导出", description = "导出")
	public List<TaskDTO> export(TaskDTO taskDTO) {
		return actTaskService.list(taskDTO);
	}

}
