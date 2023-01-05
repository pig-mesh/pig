/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.dto.SysOauthClientDetailsDTO;
import com.pig4cloud.pigx.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pigx.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/client")
@Tag(description = "client", name = "客户端管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysClientController {

	private final SysOauthClientDetailsService clientDetailsService;

	/**
	 * 通过ID查询
	 * @param clientId clientId
	 * @return SysOauthClientDetails
	 */
	@GetMapping("/{clientId}")
	public R getByClientId(@PathVariable String clientId) {
		return R.ok(clientDetailsService
				.list(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId)));
	}

	/**
	 * 简单分页查询
	 * @param page 分页对象
	 * @param sysOauthClientDetails 系统终端
	 * @return
	 */
	@GetMapping("/page")
	public R getOauthClientDetailsPage(Page page, SysOauthClientDetails sysOauthClientDetails) {
		return R.ok(clientDetailsService.queryPage(page, sysOauthClientDetails));
	}

	/**
	 * 添加
	 * @param clientDetailsDTO 实体
	 * @return success/false
	 */
	@SysLog("添加终端")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_client_add')")
	public R add(@Valid @RequestBody SysOauthClientDetailsDTO clientDetailsDTO) {
		return R.ok(clientDetailsService.saveClient(clientDetailsDTO));
	}

	/**
	 * 删除
	 * @param clientId ID
	 * @return success/false
	 */
	@SysLog("删除终端")
	@DeleteMapping("/{clientId}")
	@PreAuthorize("@pms.hasPermission('sys_client_del')")
	public R removeById(@PathVariable String clientId) {
		return R.ok(clientDetailsService.removeByClientId(clientId));
	}

	/**
	 * 编辑
	 * @param clientDetailsDTO 实体
	 * @return success/false
	 */
	@SysLog("编辑终端")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_client_edit')")
	public R update(@Valid @RequestBody SysOauthClientDetailsDTO clientDetailsDTO) {
		return R.ok(clientDetailsService.updateClientById(clientDetailsDTO));
	}

	@Inner(false)
	@GetMapping("/getClientDetailsById/{clientId}")
	public R getClientDetailsById(@PathVariable String clientId) {
		return R.ok(clientDetailsService.getOne(
				Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId), false));
	}

	/**
	 * 查询全部客户端
	 * @return
	 */
	@Inner(false)
	@GetMapping("/list")
	public R listClients() {
		return R.ok(clientDetailsService.list());
	}

	/**
	 * 同步缓存字典
	 * @return R
	 */
	@SysLog("同步终端")
	@PutMapping("/sync")
	public R sync() {
		return clientDetailsService.syncClientCache();
	}

}
