package com.pig4cloud.pig.common.core.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lengleng
 * @date 2026-03-27
 * <p>
 * 公共参数类型
 */
@Getter
@RequiredArgsConstructor
public enum ParamTypeEnum {

	/**
	 * 默认
	 */
	DEFAULT("0", "默认"),

	/**
	 * 检索
	 */
	SEARCH("1", "检索"),

	/**
	 * 原文
	 */
	ORIGINAL("2", "原文"),

	/**
	 * 报表
	 */
	REPORT("3", "报表"),

	/**
	 * 安全
	 */
	SECURITY("4", "安全"),

	/**
	 * 文档
	 */
	DOCUMENT("5", "文档"),

	/**
	 * 消息
	 */
	MESSAGE("6", "消息"),

	/**
	 * 其他
	 */
	OTHER("9", "其他"),

	/**
	 * 网站配置
	 */
	SITE_CONFIG("10", "网站配置");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
