package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashSet;
import java.util.List;

/**
 * @author lengleng
 * @date 2023/2/7
 */
public class DictTool {

	/**
	 * 将字段列表转换为带有双引号的逗号分隔的字符串
	 * @return 带有双引号的逗号分隔的字符串
	 */
	public static String quotation(List<String> fields) {

		return CollUtil.join(new HashSet<>(fields), StrUtil.COMMA, s -> String.format("'%s'", s));
	}

	/**
	 * 将字段列表转换为逗号分隔的字符串
	 * @return 逗号分隔的字符串
	 */
	public static String format(List<String> fields) {
		return CollUtil.join(new HashSet<>(fields), StrUtil.COMMA);
	}

}
