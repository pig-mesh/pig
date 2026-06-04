package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.text.NamingCase;

import java.util.Locale;

/**
 * 命名规则处理，针对驼峰，下划线等处理
 *
 * @author lengleng
 * @date 2023/1/31
 */
public class NamingCaseTool {

	/**
	 * 传入字段获取的get方法
	 * @param in 字段名称
	 * @return
	 */
	public static String getProperty(String in) {
		return "get" + NamingCase.toPascalCase(in);
	}

	public static String setProperty(String in) {
		return "set" + NamingCase.toPascalCase(in);
	}

	/**
	 * 首字母大写
	 * @param in 字段
	 * @return 首字母大写
	 */
	public static String pascalCase(String in) {
		return NamingCase.toPascalCase(in);
	}

	/**
	 * 将字符串转换为小写
	 * @param in 输入字符串
	 * @return 转换为小写后的字符串
	 */
	public static String lowerCase(String in) {
		return in.toLowerCase(Locale.ROOT);
	}

}
