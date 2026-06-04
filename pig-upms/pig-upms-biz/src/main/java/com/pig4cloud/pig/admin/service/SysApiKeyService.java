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

package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.dto.ApiKeyCreateDTO;
import com.pig4cloud.pig.admin.api.entity.SysApiKey;
import com.pig4cloud.pig.common.core.util.R;

/**
 * API密钥管理 Service
 *
 * @author lengleng
 * @date 2026-03-23
 */
public interface SysApiKeyService extends IService<SysApiKey> {

	/**
	 * 校验密码、数量上限，生成并持久化 API Key
	 * @return 明文 Key（仅此一次）
	 */
	R<String> createApiKey(ApiKeyCreateDTO dto);

	/**
	 * 校验归属权，白名单更新字段，清除缓存
	 */
	R<Void> updateApiKey(SysApiKey apiKey);

	/**
	 * 校验归属权，删除记录，清除缓存
	 */
	R<Void> deleteApiKey(Long id);

	/**
	 * 批量清除缓存后批量删除
	 */
	R<Boolean> deleteBatchApiKey(Long[] ids);

	/**
	 * 管理员分页查询，按 username 模糊过滤
	 */
	R page(Page page, SysApiKey query);

	/**
	 * 精确清除指定 hash 的缓存
	 */
	void evictApiKeyCache(String hash);

}
