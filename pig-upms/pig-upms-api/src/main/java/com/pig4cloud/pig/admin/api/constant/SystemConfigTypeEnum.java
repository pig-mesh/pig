package com.pig4cloud.pig.admin.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统配置枚举
 *
 * @author lengleng
 * @date 2024/7/14
 */
@RequiredArgsConstructor
public enum SystemConfigTypeEnum {

	/**
	 * 存储
	 */
	STORAGE("storage"),
	/**
	 * 短信
	 */
	SMS("sms"),
	/**
	 * 网站
	 */
	WEBSITE("website"),
	/**
	 * 邮件
	 */
	EMAIL("email"),

	/**
	 * webhook
	 */
	WEBHOOK("webhook"),

	/**
	 * 飞书
	 */
	FEISHU("feishu");

	@Getter
	private final String value;

}
