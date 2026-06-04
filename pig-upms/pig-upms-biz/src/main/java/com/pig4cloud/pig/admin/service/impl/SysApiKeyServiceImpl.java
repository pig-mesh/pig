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

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.codec.Base62;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.ApiKeyCreateDTO;
import com.pig4cloud.pig.admin.api.entity.SysApiKey;
import com.pig4cloud.pig.admin.mapper.SysApiKeyMapper;
import com.pig4cloud.pig.admin.service.SysApiKeyService;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * API密钥管理 Service 实现
 *
 * @author lengleng
 * @date 2026-03-23
 */
@Service
@RequiredArgsConstructor
public class SysApiKeyServiceImpl extends ServiceImpl<SysApiKeyMapper, SysApiKey> implements SysApiKeyService {

	private static final String API_KEY_PREFIX = "sk-";

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private static final int MAX_KEYS_PER_USER = 5;

	private final SysUserService userService;

	/**
	 * 校验密码、数量上限，生成并持久化 API Key
	 */
	@Override
	public R<String> createApiKey(ApiKeyCreateDTO dto) {
		String username = SecurityUtils.getUser().getUsername();
		R checkResult = userService.checkPassword(username, dto.getPassword());
		if (!(boolean) checkResult.getData()) {
			return R.failed("密码验证失败");
		}

		Long userId = SecurityUtils.getUser().getId();
		long count = this.count(Wrappers.<SysApiKey>lambdaQuery().eq(SysApiKey::getUserId, userId));
		if (count >= MAX_KEYS_PER_USER) {
			return R.failed("每个用户最多创建" + MAX_KEYS_PER_USER + "个API密钥");
		}

		byte[] randomBytes = new byte[32];
		SECURE_RANDOM.nextBytes(randomBytes);
		String rawKey = API_KEY_PREFIX + Base62.encode(randomBytes);

		SysApiKey apiKey = new SysApiKey();
		apiKey.setName(dto.getName());
		apiKey.setAllowedIps(dto.getAllowedIps());
		apiKey.setExpiresAt(dto.getExpiresAt());
		apiKey.setApiKeyHash(sha256(rawKey));
		apiKey.setUserId(userId);
		apiKey.setUsername(username);
		apiKey.setStatus("0");
		this.save(apiKey);

		return R.ok(rawKey);
	}

	/**
	 * 校验归属权，白名单更新字段，清除缓存
	 */
	@Override
	public R<Void> updateApiKey(SysApiKey apiKey) {
		SysApiKey existing = this.getById(apiKey.getId());
		if (existing == null || !existing.getUserId().equals(SecurityUtils.getUser().getId())) {
			return R.failed("无权操作");
		}
		SysApiKey update = new SysApiKey();
		update.setId(apiKey.getId());
		update.setName(apiKey.getName());
		update.setAllowedIps(apiKey.getAllowedIps());
		update.setExpiresAt(apiKey.getExpiresAt());
		update.setStatus(apiKey.getStatus());
		this.updateById(update);
		evictApiKeyCache(existing.getApiKeyHash());
		return R.ok();
	}

	/**
	 * 校验归属权，删除记录，清除缓存
	 */
	@Override
	public R<Void> deleteApiKey(Long id) {
		SysApiKey existing = this.getById(id);
		if (existing == null || !existing.getUserId().equals(SecurityUtils.getUser().getId())) {
			return R.failed("无权操作");
		}
		this.removeById(id);
		evictApiKeyCache(existing.getApiKeyHash());
		return R.ok();
	}

	/**
	 * 批量清除缓存后批量删除
	 */
	@Override
	public R<Boolean> deleteBatchApiKey(Long[] ids) {
		CollUtil.toList(ids).forEach(id -> {
			SysApiKey existing = this.getById(id);
			if (existing != null) {
				evictApiKeyCache(existing.getApiKeyHash());
			}
		});
		return R.ok(this.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 管理员分页查询，按 username 模糊过滤
	 */
	@Override
	public R page(Page page, SysApiKey query) {
		return R.ok(this.page(page,
				Wrappers.<SysApiKey>lambdaQuery()
					.like(StrUtil.isNotBlank(query.getUsername()), SysApiKey::getUsername, query.getUsername())
					.orderByDesc(SysApiKey::getCreateTime)));
	}

	@Override
	@CacheEvict(value = CacheConstants.API_KEY_DETAILS, key = "#hash")
	public void evictApiKeyCache(String hash) {
		// 缓存清除由 @CacheEvict 注解驱动
	}

	public static String sha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 algorithm not available", e);
		}
	}

}
