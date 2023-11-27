package com.pig4cloud.pigx.admin.api.constant;

import lombok.Getter;

/**
 * @author lengleng
 * @date 2023/11/14
 * <p>
 * 日程状态枚举
 */
public enum ScheduleStateEnum {

	/**
	 * 计划中
	 */
	SCHEDULE("0", "计划中"),

	/**
	 * 未开始
	 */
	NOT_START("1", "未开始"),

	/**
	 * 结束
	 */
	END("3", "结束");

	@Getter
	private String code;

	private String desc;

	ScheduleStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
