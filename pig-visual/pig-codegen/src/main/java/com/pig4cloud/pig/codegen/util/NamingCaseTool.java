package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.text.NamingCase;

/**
 * 命名规则处理工具类，提供驼峰、下划线等命名格式转换功能
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class NamingCaseTool {

	/**
	 * 根据字段名生成对应的get方法名
	 * @param in 字段名称
	 * @return 生成的get方法名
	 */
	public static String getProperty(String in) {
		return String.format("get%s", NamingCase.toPascalCase(in));
	}

	/**
	 * 根据输入字符串生成setter方法名
	 * @param in 输入字符串
	 * @return 生成的setter方法名
	 */
	public static String setProperty(String in) {
		return String.format("set%s", NamingCase.toPascalCase(in));
	}

	/**
	 * 将字符串转换为帕斯卡命名格式（首字母大写）
	 * @param in 输入字符串
	 * @return 首字母大写的字符串
	 */
	public static String pascalCase(String in) {
		return String.format(NamingCase.toPascalCase(in));
	}

}
