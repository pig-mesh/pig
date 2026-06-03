package com.pig4cloud.pig.common.core.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lengleng
 * @date 2020-11-18
 * <p>
 * 验证码状态
 */
@Getter
@RequiredArgsConstructor
public enum CaptchaFlagTypeEnum {

	/**
	 * 开启验证码
	 */
	ON("1", "开启验证码"),

	/**
	 * 关闭验证码
	 */
	OFF("0", "关闭验证码");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
