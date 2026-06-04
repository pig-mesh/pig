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

package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.admin.api.entity.SysApiKey;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * API密钥远程调用接口
 *
 * @author lengleng
 * @date 2026-03-23
 */
@FeignClient(contextId = "remoteApiKeyService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteApiKeyService {

	/**
	 * 根据 SHA-256 哈希值查询 API Key（仅限内部调用）
	 * @param hash API Key 的 SHA-256 哈希值
	 * @return 对应的 SysApiKey 记录，不存在时 data 为 null
	 */
	@NoToken
	@GetMapping("/api-key/inner/hash/{hash}")
	R<SysApiKey> getByHash(@PathVariable String hash);

	/**
	 * 更新 API Key 的最后使用时间（仅限内部调用）
	 * @param id API Key 主键
	 * @return 更新是否成功
	 */
	@NoToken
	@PutMapping("/api-key/inner/last-used/{id}")
	R<Boolean> updateLastUsed(@PathVariable Long id);

}
