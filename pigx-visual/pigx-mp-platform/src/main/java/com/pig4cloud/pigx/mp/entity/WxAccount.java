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
package com.pig4cloud.pigx.mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import com.pig4cloud.pigx.common.sensitive.annotation.Sensitive;
import com.pig4cloud.pigx.common.sensitive.core.SensitiveTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 公众号账户
 *
 * @author lengleng
 * @date 2019-03-26 22:07:53
 */
@Data
@TenantTable
@EqualsAndHashCode(callSuper = true)
public class WxAccount extends Model<WxAccount> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 公众号名称
	 */
	private String name;

	/**
	 * 公众号账户
	 */
	private String account;

	/**
	 * 公众号appid
	 */
	private String appid;

	/**
	 * 公众号密钥
	 */
	@Sensitive(type = SensitiveTypeEnum.KEY)
	private String appsecret;

	/**
	 * 公众号url
	 */
	private String url;

	/**
	 * 公众号token
	 */
	private String token;

	/**
	 * 加密密钥
	 */
	private String aeskey;

	/**
	 * 二维码图片URL
	 */
	private String qrUrl;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 是否删除 -1：已删除 0：正常
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 租户
	 */
	private Long tenantId;

}
