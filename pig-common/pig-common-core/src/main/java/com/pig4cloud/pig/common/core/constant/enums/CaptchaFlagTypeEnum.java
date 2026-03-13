package com.pig4cloud.pig.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2020-11-18
 * <p>
 * 验证码状态
 */
@Getter
@AllArgsConstructor
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
	private String type;

	/**
	 * 描述
	 */
	private String description;

}
