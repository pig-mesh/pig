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

package com.pig4cloud.pig.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.dto.ApiKeyCreateDTO;
import com.pig4cloud.pig.admin.api.entity.SysApiKey;
import com.pig4cloud.pig.admin.service.SysApiKeyService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * API密钥管理
 *
 * @author lengleng
 * @date 2026-03-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api-key")
@Tag(description = "api-key", name = "API密钥管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysApiKeyController {

	private final SysApiKeyService apiKeyService;

	/**
	 * 创建 API Key
	 * <p>
	 * 业务逻辑由 Service 完成：校验当前密码（防 CSRF）、数量上限（每用户最多 5 个）、 生成 sk- 前缀密钥并只存储 SHA-256 哈希。明文 Key
	 * 仅在本次响应中返回，之后不可再取。
	 * @param dto 创建请求（name、allowedIps、expiresAt、password）
	 * @return 明文 API Key，格式：sk-xxx
	 */
	@SysLog("创建API密钥")
	@PostMapping
	public R<String> create(@Valid @RequestBody ApiKeyCreateDTO dto) {
		return apiKeyService.createApiKey(dto);
	}

	/**
	 * 查询当前用户的 API Key 列表
	 * <p>
	 * 仅返回当前登录用户自己的 Key，按创建时间倒序排列。 列表中不包含明文 Key 和哈希值，仅展示元数据。
	 */
	@GetMapping("/list")
	public R list() {
		Long userId = SecurityUtils.getUser().getId();
		return R.ok(apiKeyService.list(Wrappers.<SysApiKey>lambdaQuery()
			.eq(SysApiKey::getUserId, userId)
			.orderByDesc(SysApiKey::getCreateTime)));
	}

	/**
	 * 管理员分页查询所有用户的 API Key
	 * <p>
	 * 需要 admin_apikey_view 权限。支持按 username 模糊搜索。
	 * @param page 分页参数
	 * @param sysApiKey 查询条件（username 字段用于模糊匹配）
	 */
	@GetMapping("/page")
	@HasPermission("sys_social_details_view")
	public R page(@ParameterObject Page page, @ParameterObject SysApiKey sysApiKey) {
		return apiKeyService.page(page, sysApiKey);
	}

	/**
	 * 更新 API Key 元数据
	 * <p>
	 * 仅允许修改 name、allowedIps、expiresAt、status，不可修改 apiKeyHash。 Service
	 * 层校验归属权，并在更新后精确清除对应缓存。
	 * @param apiKey 待更新字段（必须包含 id）
	 */
	@SysLog("更新API密钥")
	@PutMapping
	public R update(@RequestBody SysApiKey apiKey) {
		return apiKeyService.updateApiKey(apiKey);
	}

	/**
	 * 删除当前用户自己的 API Key
	 * <p>
	 * Service 层校验归属权后删除，并精确清除该 Key 的缓存。
	 * @param id API Key 主键
	 */
	@SysLog("删除API密钥")
	@DeleteMapping("/{id}")
	@HasPermission("sys_social_details_del")
	public R delete(@PathVariable Long id) {
		return apiKeyService.deleteApiKey(id);
	}

	/**
	 * 管理员批量删除 API Key
	 * <p>
	 * 需要 admin_apikey_del 权限。Service 层逐条清除缓存后执行批量删除。
	 * @param ids 待删除的 API Key 主键数组
	 */
	@SysLog("批量删除API密钥")
	@DeleteMapping
	@HasPermission("sys_social_details_del")
	public R deleteBatch(@RequestBody Long[] ids) {
		return apiKeyService.deleteBatchApiKey(ids);
	}

	/**
	 * 内部接口：根据 SHA-256 哈希值查询有效的 API Key
	 * <p>
	 * 仅供 pig-common-security 的 token 校验流程调用，不对外暴露。 结果会被 Spring Cache 缓存（key 为
	 * hash），避免每次请求都查库。
	 * @param hash API Key 的 SHA-256 哈希值
	 * @return 状态正常的 SysApiKey，不存在或已禁用时 data 为 null
	 */
	@Inner
	@GetMapping("/inner/hash/{hash}")
	@Cacheable(value = CacheConstants.API_KEY_DETAILS, key = "#hash", unless = "#result.data == null")
	public R<SysApiKey> getByHash(@PathVariable String hash) {
		return R.ok(apiKeyService.getOne(Wrappers.<SysApiKey>lambdaQuery()
			.eq(SysApiKey::getApiKeyHash, hash)
			.eq(SysApiKey::getStatus, YesNoEnum.NO.getCode())));
	}

	/**
	 * 内部接口：更新 API Key 最后使用时间
	 * <p>
	 * 由 token 校验流程在认证成功后异步调用，记录 Key 的活跃时间。 该操作不影响认证结果，失败时仅打印警告日志。
	 * @param id API Key 主键
	 */
	@Inner
	@PutMapping("/inner/last-used/{id}")
	public R<Boolean> updateLastUsed(@PathVariable Long id) {
		SysApiKey update = new SysApiKey();
		update.setId(id);
		update.setLastUsedAt(LocalDateTime.now());
		return R.ok(apiKeyService.updateById(update));
	}

}
