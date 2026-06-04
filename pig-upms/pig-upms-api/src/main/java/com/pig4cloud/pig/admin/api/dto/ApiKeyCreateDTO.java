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

package com.pig4cloud.pig.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建 API 密钥请求体
 *
 * @author lengleng
 * @date 2026-03-23
 */
@Data
@Schema(description = "创建API密钥请求")
public class ApiKeyCreateDTO {

	@Schema(description = "Key名称")
	private String name;

	@Schema(description = "IP白名单，多个以逗号分隔")
	private String allowedIps;

	@Schema(description = "过期时间")
	private LocalDateTime expiresAt;

	@NotBlank(message = "当前密码不能为空")
	@Schema(description = "当前登录用户密码（用于二次验证）")
	private String password;

}
