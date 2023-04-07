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
import com.pig4cloud.pigx.act.service.ProcessService;
import com.pig4cloud.pigx.common.core.constant.enums.ResourceTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
@Tag(description = "process", name = "process表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ProcessController {

	private final ProcessService processService;

	@GetMapping
	@Operation(summary = "条件查询", description = "条件查询")
	@PreAuthorize("@pms.hasPermission('oa_process_view')")
	public R list(@RequestParam Map<String, Object> params) {
		return R.ok(processService.getProcessByPage(params));
	}

	@GetMapping(value = "/resource/{proInsId}/{procDefId}/{resType}")
	@Operation(summary = "查询", description = "查询")
	@PreAuthorize("@pms.hasPermission('oa_process_view')")
	public ResponseEntity resourceRead(@PathVariable String procDefId, @PathVariable String proInsId,
			@PathVariable String resType) {
		HttpHeaders headers = new HttpHeaders();

		if (ResourceTypeEnum.XML.getType().equals(resType)) {
			headers.setContentType(MediaType.APPLICATION_XML);
		}
		else {
			headers.setContentType(MediaType.IMAGE_PNG);
		}

		InputStream resourceAsStream = processService.readResource(procDefId, proInsId, resType);
		return new ResponseEntity(IoUtil.readBytes(resourceAsStream), headers, HttpStatus.CREATED);
	}

	@PutMapping("/status/{procDefId}/{status}")
	@Operation(summary = "修改", description = "修改")
	@PreAuthorize("@pms.hasPermission('oa_process_edit')")
	public R updateState(@PathVariable String procDefId, @PathVariable String status) {
		return R.ok(processService.updateStatus(status, procDefId));
	}

	@DeleteMapping
	@Operation(summary = "删除流程", description = "删除流程")
	@PreAuthorize("@pms.hasPermission('oa_process_del')")
	public R deleteProcIns(@RequestBody String[] ids) {
		return R.ok(processService.removeProcIns(ids));
	}

}
