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

package com.pig4cloud.pigx.common.sensitive.util;

import cn.hutool.core.util.StrUtil;

/**
 * 脱敏工具类
 *
 * @author mayee
 * @version v1.0
 **/
public class DesensitizedUtils {

	/**
	 * 对字符串进行脱敏操作
	 * @param origin 原始字符串
	 * @param prefixNoMaskLen 左侧需要保留几位明文字段
	 * @param suffixNoMaskLen 右侧需要保留几位明文字段
	 * @param maskStr 用于遮罩的字符串, 如'*'
	 * @return 脱敏后结果
	 */
	public static String desValue(String origin, int prefixNoMaskLen, int suffixNoMaskLen, String maskStr) {
		if (origin == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = origin.length(); i < n; i++) {
			if (i < prefixNoMaskLen) {
				sb.append(origin.charAt(i));
				continue;
			}
			if (i > (n - suffixNoMaskLen - 1)) {
				sb.append(origin.charAt(i));
				continue;
			}
			sb.append(maskStr);
		}
		return sb.toString();
	}

	/**
	 * 【中文姓名】只显示最后一个汉字，其他隐藏为星号，比如：**梦
	 * @param fullName 姓名
	 * @return 结果
	 */
	public static String chineseName(String fullName) {
		if (fullName == null) {
			return null;
		}
		return desValue(fullName, 0, 1, "*");
	}

	/**
	 * 【身份证号】显示前六位, 四位，其他隐藏。共计18位或者15位，比如：340304*******1234
	 * @param id 身份证号码
	 * @return 结果
	 */
	public static String idCardNum(String id) {
		return desValue(id, 6, 4, "*");
	}

	/**
	 * 【固定电话】后四位，其他隐藏，比如 ****1234
	 * @param num 固定电话
	 * @return 结果
	 */
	public static String fixedPhone(String num) {
		return desValue(num, 0, 4, "*");
	}

	/**
	 * 【手机号码】前三位，后四位，其他隐藏，比如135****6810
	 * @param num 手机号码
	 * @return 结果
	 */
	public static String mobilePhone(String num) {
		return desValue(num, 3, 4, "*");
	}

	/**
	 * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
	 * @param address 地址
	 * @return 结果
	 */
	public static String address(String address) {
		return desValue(address, 6, 0, "*");
	}

	/**
	 * 【电子邮箱 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
	 * @param email 电子邮箱
	 * @return 结果
	 */
	public static String email(String email) {
		if (email == null) {
			return null;
		}
		int index = StrUtil.indexOf(email, '@');
		if (index <= 1) {
			return email;
		}
		String preEmail = desValue(email.substring(0, index), 1, 0, "*");
		return preEmail + email.substring(index);

	}

	/**
	 * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：622260**********1234
	 * @param cardNum 银行卡号
	 * @return 结果
	 */
	public static String bankCard(String cardNum) {
		return desValue(cardNum, 6, 4, "*");
	}

	/**
	 * 【密码】密码的全部字符都用*代替，比如：******
	 * @param password 密码
	 * @return 结果
	 */
	public static String password(String password) {
		if (password == null) {
			return null;
		}
		return "******";
	}

	/**
	 * 【密钥】密钥除了最后三位，全部都用*代替，比如：***xdS 脱敏后长度为6，如果明文长度不足三位，则按实际长度显示，剩余位置补*
	 * @param key 密钥
	 * @return 结果
	 */
	public static String key(String key) {
		if (key == null) {
			return null;
		}
		int viewLength = 6;
		StringBuilder tmpKey = new StringBuilder(desValue(key, 0, 3, "*"));
		if (tmpKey.length() > viewLength) {
			return tmpKey.substring(tmpKey.length() - viewLength);
		}
		else if (tmpKey.length() < viewLength) {
			int buffLength = viewLength - tmpKey.length();
			for (int i = 0; i < buffLength; i++) {
				tmpKey.insert(0, "*");
			}
			return tmpKey.toString();
		}
		else {
			return tmpKey.toString();
		}
	}

	public static String ipv4(String origin) {
		return StrUtil.subBefore(origin, '.', true) + ".*";
	}

}
