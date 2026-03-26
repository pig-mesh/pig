package com.pig4cloud.pig.admin.api.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Clarity 数据拉取状态
 *
 * @author lengleng
 * @date 2026-03-31
 */
@Getter
@RequiredArgsConstructor
public enum ClarityFetchStatus {

	PENDING("pending", "占位中"),
	SUCCESS("success", "成功"),
	FAILED("failed", "失败");

	@EnumValue
	@JsonValue
	private final String value;

	private final String desc;

}
