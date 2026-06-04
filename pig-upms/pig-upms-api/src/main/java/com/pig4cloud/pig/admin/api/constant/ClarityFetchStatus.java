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

package com.pig4cloud.pig.admin.api.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Clarity 数据拉取状态枚举
 *
 * @author lengleng
 * @date 2026-03-31
 */
@Getter
@RequiredArgsConstructor
public enum ClarityFetchStatus {

	/**
	 * 占位中：已插入占位行，API 调用尚未完成
	 */
	PENDING("pending", "占位中"),

	/**
	 * 成功：API 调用成功，数据已写入
	 */
	SUCCESS("success", "成功"),

	/**
	 * 失败：API 调用失败，下次调度可重试
	 */
	FAILED("failed", "失败");

	@EnumValue
	@JsonValue
	private final String value;

	private final String desc;

}
