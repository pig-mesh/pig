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
package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * API密钥管理
 *
 * @author lengleng
 * @date 2026-03-23
 */
@Data
@Schema(description = "API密钥")
@EqualsAndHashCode(callSuper = true)
public class SysApiKey extends Model<SysApiKey> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	@Schema(description = "所属用户ID")
	private Long userId;

	@Schema(description = "所属用户名")
	private String username;

	@Schema(description = "Key名称")
	private String name;

	@Schema(description = "SHA-256哈希值")
	private String apiKeyHash;

	@Schema(description = "IP白名单")
	private String allowedIps;

	@Schema(description = "过期时间")
	private LocalDateTime expiresAt;

	@Schema(description = "状态：0-正常，1-禁用")
	private String status;

	@Schema(description = "最后使用时间")
	private LocalDateTime lastUsedAt;

	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建者")
	private String createBy;

	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新者")
	private String updateBy;

	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

}
