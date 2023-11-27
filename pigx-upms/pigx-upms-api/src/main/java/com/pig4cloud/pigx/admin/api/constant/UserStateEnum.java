package com.pig4cloud.pigx.admin.api.constant;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author lengleng
 * @date 2023/11/14
 */
public enum UserStateEnum {

	/**
	 * 正常
	 */
	NORMAL("0", "正常"),
	/**
	 * 锁定
	 */
	LOCK("9", "锁定");

	@Getter
	private final String code;

	private final String desc;

	UserStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
