package com.pig4cloud.pigx.common.excel.annotation;

import com.pig4cloud.pigx.common.excel.head.HeadGenerator;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic Sheet
 * @Description
 * @date 2021/4/29 15:03
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {

	int sheetNo() default -1;

	/**
	 * sheet name
	 */
	String sheetName();

	/**
	 * 包含字段
	 */
	String[] includes() default {};

	/**
	 * 排除字段
	 */
	String[] excludes() default {};

	/**
	 * 头生成器
	 */
	Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

}
