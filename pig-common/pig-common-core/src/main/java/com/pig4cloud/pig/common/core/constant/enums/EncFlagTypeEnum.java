package com.pig4cloud.pig.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2020-11-18
 * <p>
 * 密码是否加密传输
 */
@Getter
@AllArgsConstructor
public enum EncFlagTypeEnum {

	/**
	 * 是
	 */
	YES("1", "是"),

	/**
	 * 否
	 */
	NO("0", "否");

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 描述
	 */
	private String description;

}
