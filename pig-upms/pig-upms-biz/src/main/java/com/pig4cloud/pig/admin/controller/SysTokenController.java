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

package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.feign.RemoteTokenService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 令牌管理控制器：提供令牌的分页查询和删除功能
 *
 * @author lengleng
 * @date 2025/05/30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-token")
@Tag(description = "token", name = "令牌管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTokenController {

	private final RemoteTokenService remoteTokenService;

	/**
	 * 获取分页token信息
	 * @param params 请求参数集合
	 * @return 包含token分页信息的响应结果
	 */
	@PostMapping("/page")
	@HasPermission("sys_token_del")
	@Operation(summary = "获取分页token信息", description = "获取分页token信息")
	public R getTokenPage(@RequestBody Map<String, Object> params) {
		return remoteTokenService.getTokenPage(params);
	}

	/**
	 * 根据token数组删除token
	 * @param tokens 需要删除的token数组
	 * @return 操作结果，成功返回success，失败返回false
	 */
	@SysLog("删除用户token")
	@DeleteMapping("/delete")
	@HasPermission("sys_token_del")
	@Operation(summary = "删除用户token", description = "删除用户token")
	public R removeById(@RequestBody String[] tokens) {
		for (String token : tokens) {
			remoteTokenService.removeTokenById(token);
		}
		return R.ok();
	}

}
