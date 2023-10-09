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

package com.pig4cloud.pigx.common.core.sensitive;

/**
 * 敏感信息枚举类
 *
 * @author mayee
 * @version v1.0
 **/
public enum SensitiveTypeEnum {

	/**
	 * 自定义
	 */
	CUSTOMER,
	/**
	 * 用户名, 刘*华, 徐*
	 */
	CHINESE_NAME,
	/**
	 * 身份证号, 110110********1234
	 */
	ID_CARD,
	/**
	 * 座机号, ****1234
	 */
	FIXED_PHONE,
	/**
	 * 手机号, 176****1234
	 */
	MOBILE_PHONE,
	/**
	 * 地址, 北京********
	 */
	ADDRESS,
	/**
	 * 电子邮件, s*****o@xx.com
	 */
	EMAIL,
	/**
	 * 银行卡, 622202************1234
	 */
	BANK_CARD,
	/**
	 * 密码, 永远是 ******, 与长度无关
	 */
	PASSWORD,
	/**
	 * 密钥, 【密钥】密钥除了最后三位其他都是***, 与长度无关
	 */
	KEY,
	/**
	 * IPV4 类型 113.123.198.176 主机部分打码 113.123.*.123
	 */
	IPV4,

	/**
	 * 中国大陆车牌，包含普通车辆、新能源车辆
	 */
	CAR_LICENSE

}
