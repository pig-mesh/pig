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

import com.pig4cloud.pigx.act.dto.ModelForm;
import com.pig4cloud.pigx.act.service.ModelService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
@Tag(description = "model", name = "model表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ModelController {

	private final ModelService modelService;

	@PostMapping(value = "/insert")
	@Operation(summary = "新增Model", description = "新增Model")
	@PreAuthorize("@pms.hasPermission('oa_model_add')")
	public R<Boolean> insertForm(@RequestBody @Valid ModelForm form) {
		modelService.create(form.getName(), form.getKey(), form.getDesc(), form.getCategory());
		return R.ok(Boolean.TRUE);
	}

	@PostMapping
	@Operation(summary = "新增Model", description = "新增Model")
	@PreAuthorize("@pms.hasPermission('oa_model_add')")
	public R createModel(@RequestParam String name, @RequestParam String key, @RequestParam String desc,
			@RequestParam String category) {
		return R.ok(modelService.create(name, key, desc, category));
	}

	@GetMapping
	@Operation(summary = "条件查询", description = "条件查询")
	@PreAuthorize("@pms.hasPermission('oa_model_view')")
	public R getModelPage(@RequestParam Map<String, Object> params) {
		return R.ok(modelService.getModelPage(params));
	}

	@DeleteMapping
	@Operation(summary = "删除模型", description = "删除模型")
	@PreAuthorize("@pms.hasPermission('oa_model_del')")
	public R removeModelById(@RequestBody String[] ids) {
		return R.ok(modelService.removeModelById(ids));

	}

	@PostMapping("/deploy/{id}")
	@Operation(summary = "通过id查询", description = "通过id查询")
	@PreAuthorize("@pms.hasPermission('oa_model_view')")
	public R deploy(@PathVariable("id") String id) {
		return modelService.deploy(id) ? R.ok() : R.failed();
	}

}
