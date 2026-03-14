package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/**
 * 代码生成工具类
 *
 * @author lengleng
 * @date 2023/2/16
 */
@UtilityClass
public class GenKit {

	/**
	 * 获取功能名 sys_a_b sysAb
	 * @param tableName 表名
	 * @return 功能名
	 */
	public String getFunctionName(String tableName) {
		return StrUtil.toCamelCase(tableName);
	}

	/**
	 * 获取模块名称
	 * @param packageName 包名
	 * @return 功能名
	 */
	public String getModuleName(String packageName) {
		return StrUtil.subAfter(packageName, ".", true);
	}

}
